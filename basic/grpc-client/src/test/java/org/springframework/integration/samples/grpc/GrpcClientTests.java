/*
 * Copyright 2026-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.samples.grpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.grpc.BindableService;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.integration.channel.FluxMessageChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.grpc.GrpcHeaders;
import org.springframework.integration.grpc.inbound.GrpcInboundGateway;
import org.springframework.integration.samples.grpc.proto.HelloReply;
import org.springframework.integration.samples.grpc.proto.HelloRequest;
import org.springframework.integration.samples.grpc.proto.HelloWorldServiceGrpc;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Glenn Renfro
 */
@SpringBootTest
class GrpcClientTests {

	@Autowired
	@Qualifier("grpcOutboundFlowSingleResponse.input")
	private MessageChannel grpcInputChannelSingleResponse;

	@Autowired
	@Qualifier("grpcOutboundFlowStreamResponse.input")
	private MessageChannel grpcInputChannelStreamResponse;

	@Autowired
	private FluxMessageChannel grpcStreamOutputChannel;

	@Test
	void shouldSendSingleRequestAndReceiveSingleResponse() {
		HelloRequest request = HelloRequest.newBuilder()
				.setName("TestUser")
				.build();

		QueueChannel replyChannel = new QueueChannel();
		Message<?> requestMessage = MessageBuilder.withPayload(request)
				.setReplyChannel(replyChannel)
				.build();

		this.grpcInputChannelSingleResponse.send(requestMessage);

		Message<?> reply = replyChannel.receive(5000);

		assertThat(reply).isNotNull();
		assertThat(reply.getPayload()).isInstanceOf(HelloReply.class);

		HelloReply helloReply = (HelloReply) reply.getPayload();
		assertThat(helloReply.getMessage()).isEqualTo("Hello TestUser");
	}

	@Test
	void shouldSendStreamRequestAndReceiveMultipleResponses() throws InterruptedException {
		HelloRequest request = HelloRequest.newBuilder()
				.setName("StreamUser")
				.build();

		List<HelloReply> receivedReplies = new ArrayList<>();
		CountDownLatch latch = new CountDownLatch(1);

		Flux.from(this.grpcStreamOutputChannel)
				.doOnSubscribe(subscription -> {
					Message<?> requestMessage = MessageBuilder.withPayload(request).build();
					this.grpcInputChannelStreamResponse.send(requestMessage);
				}).take(2)
				.map(message -> (HelloReply) message.getPayload())
				.doOnNext(receivedReplies::add)
				.doOnComplete(latch::countDown)
				.doOnError(error -> latch.countDown())
				.subscribe();

		boolean completed = latch.await(10, TimeUnit.SECONDS);

		assertThat(completed).isTrue();
		assertThat(receivedReplies).hasSize(2).extracting(HelloReply::getMessage)
				.containsExactly("Hello StreamUser", "Hello again!");
	}

	/**
	 *
	 * Sets up an in-process gRPC server for testing.
	 */
	@TestConfiguration
	static class TestGrpcServerConfig implements DisposableBean {

		final String serverName = InProcessServerBuilder.generateName();

		final InProcessServerBuilder serverBuilder = InProcessServerBuilder.forName(this.serverName);

		volatile Server server;

		/**
		 * Creates an in-process gRPC channel for testing.
		 * This channel connects to the in-process server.
		 * @return the managed channel
		 */
		@Bean
		@Primary
		ManagedChannel testManagedChannel() {
			return InProcessChannelBuilder.forName(this.serverName).directExecutor().build();
		}

		/**
		 * Bean post processor that automatically registers any BindableService beans
		 * with the in-process gRPC server.
		 * @return the bean post processor
		 */
		@Bean
		BeanPostProcessor bindGrpcServicesPostProcessor() {
			return new BeanPostProcessor() {

				@Override
				public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
					if (bean instanceof BindableService bindableService) {
						TestGrpcServerConfig.this.serverBuilder.addService(bindableService);
					}
					return bean;
				}

			};
		}

		/**
		 * Starts the in-process gRPC server when the application context is refreshed.
		 */
		@EventListener(ContextRefreshedEvent.class)
		void startServer() throws IOException {
			this.server = this.serverBuilder.build().start();
		}

		@Override
		public void destroy() {
			if (this.server != null) {
				this.server.shutdownNow();
			}
		}

		/**
		 * Creates the gRPC inbound gateway for the HelloWorld service.
		 * @return the configured gRPC inbound gateway
		 */
		@Bean
		GrpcInboundGateway helloWorldService() {
			return new GrpcInboundGateway(HelloWorldServiceGrpc.HelloWorldServiceImplBase.class);
		}

		/**
		 * Creates the main integration flow for handling incoming gRPC requests.
		 * @param helloWorldService the gRPC inbound gateway
		 * @return the integration flow
		 */
		@Bean
		IntegrationFlow grpcServerIntegrationFlow(GrpcInboundGateway helloWorldService) {
			return IntegrationFlow.from(helloWorldService)
					.route(Message.class, message ->
									message.getHeaders().get(GrpcHeaders.SERVICE_METHOD, String.class),
							router -> router
									.subFlowMapping("SayHello", flow -> flow
											.transform(this::requestReply))
									.subFlowMapping("StreamSayHello", flow -> flow
											.transform(this::streamReply))
					)
					.get();
		}

		private HelloReply requestReply(HelloRequest helloRequest) {
			return newHelloReply("Hello " + helloRequest.getName());
		}

		private Flux<HelloReply> streamReply(HelloRequest helloRequest) {
			return Flux.just(
					newHelloReply("Hello " + helloRequest.getName()),
					newHelloReply("Hello again!"));
		}

		private static HelloReply newHelloReply(String message) {
			return HelloReply.newBuilder().setMessage(message).build();
		}

	}

}
