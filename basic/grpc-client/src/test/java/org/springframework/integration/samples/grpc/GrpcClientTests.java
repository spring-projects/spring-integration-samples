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

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.FluxMessageChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.grpc.proto.HelloReply;
import org.springframework.integration.grpc.proto.HelloRequest;
import org.springframework.integration.grpc.proto.HelloWorldServiceGrpc;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Glenn Renfro
 */
@SpringBootTest
class GrpcClientTests {

	private static Server mockGrpcServer;

	private static int serverPort;

	@Autowired
	private MessageChannel grpcInputChannelSingleResponse;

	@Autowired
	private MessageChannel grpcInputChannelStreamResponse;

	@Autowired
	private FluxMessageChannel grpcStreamOutputChannel;

	@DynamicPropertySource
	static void grpcServerProperties(DynamicPropertyRegistry registry) throws IOException {
		mockGrpcServer = ServerBuilder.forPort(0)
				.addService(new MockHelloWorldService())
				.build()
				.start();

		serverPort = mockGrpcServer.getPort();

		registry.add("grpc.server.host", () -> "localhost");
		registry.add("grpc.server.port", () -> serverPort);
	}

	@AfterAll
	static void tearDown() {
		if (mockGrpcServer != null) {
			mockGrpcServer.shutdownNow();
		}
	}

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
	 * Mock gRPC service implementation for testing.
	 */
	private static class MockHelloWorldService extends HelloWorldServiceGrpc.HelloWorldServiceImplBase {

		@Override
		public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
			HelloReply reply = HelloReply.newBuilder()
					.setMessage("Hello " + request.getName())
					.build();
			responseObserver.onNext(reply);
			responseObserver.onCompleted();
		}

		@Override
		public void streamSayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
			HelloReply reply1 = HelloReply.newBuilder()
					.setMessage("Hello " + request.getName())
					.build();
			HelloReply reply2 = HelloReply.newBuilder()
					.setMessage("Hello again!")
					.build();

			responseObserver.onNext(reply1);
			responseObserver.onNext(reply2);
			responseObserver.onCompleted();
		}

	}

	@TestConfiguration
	private static class GrpcClientTestConfiguration {

		@Bean
		public ApplicationRunner grpcClientSingleResponse() {
			return args -> {
				// No-op for tests
			};
		}

		@Bean
		public ApplicationRunner grpcClientStreamResponse() {
			return args -> {
				// No-op for tests
			};
		}

	}
}
