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

package org.springframework.integration.samples.grpc.configuration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import reactor.core.publisher.Flux;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.FluxMessageChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.grpc.dsl.Grpc;
import org.springframework.integration.grpc.outbound.GrpcOutboundGateway;
import org.springframework.integration.grpc.proto.HelloReply;
import org.springframework.integration.grpc.proto.HelloRequest;
import org.springframework.integration.grpc.proto.HelloWorldServiceGrpc;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

/**
 * Configuration class for the gRPC client sample.
 * Configures the gRPC channel, message channels, and integration flows
 * for both single response and streaming response scenarios.
 *
 * @author Glenn Renfro
 */
@Configuration
class ClientHelloWorldConfiguration {

	private static final Log LOG = LogFactory.getLog(ClientHelloWorldConfiguration.class);

	/**
	 * Creates a managed gRPC channel for communication with the server.
	 *
	 * @param host the gRPC server host (default: localhost)
	 * @param port the gRPC server port (default: 9090)
	 * @return the configured managed channel
	 */
	@Bean(destroyMethod = "shutdownNow")
	ManagedChannel managedChannel(@Value("${grpc.server.host:localhost}") String host,
			@Value("${grpc.server.port:9090}") int port) {
		return ManagedChannelBuilder.forAddress(host, port)
				.usePlaintext()
				.build();
	}

	/**
	 * Creates a message channel for single response gRPC requests.
	 *
	 * @return the direct message channel
	 */
	@Bean
	MessageChannel grpcInputChannelSingleResponse() {
		return new DirectChannel();
	}

	/**
	 * Creates a message channel for streaming response gRPC requests.
	 *
	 * @return the direct message channel
	 */
	@Bean
	MessageChannel grpcInputChannelStreamResponse() {
		return new DirectChannel();
	}

	/**
	 * Creates a FluxMessageChannel for output.
	 *
	 * @return the flux message channel
	 */
	@Bean
	FluxMessageChannel grpcStreamOutputChannel() {
		return new FluxMessageChannel();
	}

	/**
	 * Creates an application runner that sends a single gRPC request and receives a single response.
	 *
	 * @param grpcInputChannelSingleResponse the message channel for single response requests
	 * @param replyTimeout the time in milliseconds to await for the response.  Defaults to 10,000 milliseconds.
	 * @return the application runner
	 */
	@Bean
	ApplicationRunner grpcClientSingleResponse(MessageChannel grpcInputChannelSingleResponse,
			@Value("${grpc.client.single.reply.timeout:10000}") long replyTimeout) {
		return args -> {
			HelloRequest request = HelloRequest.newBuilder().setName("Jack").build();
			QueueChannel replyChannel = new QueueChannel();
			Message<?> requestMessage = MessageBuilder.withPayload(request)
					.setReplyChannel(replyChannel)
					.build();
			grpcInputChannelSingleResponse.send(requestMessage);
			Message<?> reply = replyChannel.receive(replyTimeout);
			if (reply != null) {
				LOG.info("Single response reply: " + reply.getPayload());
			}
			else {
				LOG.warn("No reply received");
			}
		};
	}

	/**
	 * Creates an application runner that sends a gRPC request and receives a stream of responses.
	 *
	 * @param grpcInputChannelStreamResponse the message channel for streaming response requests
	 * @param grpcStreamOutputChannel channel that contains the responses
	 * @param replyTimeout the time in seconds to await for the response.  Defaults to 1 second.
	 * @return the application runner
	 */
	@Bean
	ApplicationRunner grpcClientStreamResponse(MessageChannel grpcInputChannelStreamResponse,
			FluxMessageChannel grpcStreamOutputChannel,
			@Value("${grpc.client.stream.reply.timeout:1}") int replyTimeout) {
		return args -> {
			CountDownLatch latch = new CountDownLatch(1);

			Flux.from(grpcStreamOutputChannel)
					.doOnSubscribe(subscription -> {
						HelloRequest request = HelloRequest.newBuilder().setName("Jack").build();
						Message<?> requestMessage = MessageBuilder.withPayload(request).build();
						grpcInputChannelStreamResponse.send(requestMessage);
					})
					.map(message -> (HelloReply) message.getPayload())
					.map(HelloReply::getMessage)
					.doOnNext(msg -> LOG.info("Stream received reply: " + msg))
					.doOnComplete(latch::countDown)
					.doOnError(error -> {
						LOG.error("Error in stream: " + error.getMessage(), error);
						latch.countDown();
					})
					.subscribe();

			latch.await(replyTimeout, TimeUnit.SECONDS);
		};
	}

	/**
	 * Creates an integration flow for outbound gRPC requests with single responses.
	 *
	 * @param managedChannel the gRPC managed channel
	 * @param grpcInputChannelSingleResponse the input message channel
	 * @return the integration flow
	 */
	@Bean
	IntegrationFlow grpcOutboundFlowSingleResponse(ManagedChannel managedChannel,
			MessageChannel grpcInputChannelSingleResponse) {
		return IntegrationFlow.from(grpcInputChannelSingleResponse)
				.handle(Grpc.outboundGateway(managedChannel, HelloWorldServiceGrpc.class)
						.methodName("SayHello"))
				.get();
	}

	/**
	 * Creates an integration flow for outbound gRPC requests with streaming responses.
	 *
	 * @param managedChannel the gRPC managed channel
	 * @param grpcInputChannelStreamResponse the input message channel
	 * @param grpcStreamOutputChannel channel containing the results
	 * @return the integration flow
	 */
	@Bean
	IntegrationFlow grpcOutboundFlowStreamResponse(ManagedChannel managedChannel,
			MessageChannel grpcInputChannelStreamResponse,
			FluxMessageChannel grpcStreamOutputChannel) {
		GrpcOutboundGateway gateway = new GrpcOutboundGateway(managedChannel, HelloWorldServiceGrpc.class);
		gateway.setMethodName("StreamSayHello");

		return IntegrationFlow.from(grpcInputChannelStreamResponse)
				.handle(gateway)
				.channel(grpcStreamOutputChannel)
				.get();
	}

}
