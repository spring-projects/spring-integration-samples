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

package org.springframework.integration.samples.grpc.client;

import io.grpc.ManagedChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import reactor.core.publisher.Flux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.integration.channel.FluxMessageChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.grpc.GrpcHeaders;
import org.springframework.integration.grpc.dsl.Grpc;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

/**
 * Service for demonstrating gRPC client functionality with Spring Integration.
 *
 * @author Glenn Renfro
 */
@Configuration(proxyBeanMethods = false)
public class GrpcClientConfiguration {

	private static final Log LOGGER = LogFactory.getLog(GrpcClientConfiguration.class);

	@Bean
	ManagedChannel managedChannel(GrpcChannelFactory factory,
			@Value("${spring.grpc.client.channels.spring-integration.address}") String grpcServerAddress) {
		return factory.createChannel(grpcServerAddress);
	}

	/**
	 * Create an integration flow for outbound gRPC requests with single responses.
	 * This flow specifies the method name to be used.
	 * @param managedChannel the gRPC managed channel
	 * @return the integration flow
	 */
	@Bean
	IntegrationFlow grpcOutboundFlowSingleResponse(ManagedChannel managedChannel) {
		return flow -> flow
				.handle(Grpc.outboundGateway(managedChannel, SimpleGrpc.class)
						.methodName("SayHello"));
	}

	/**
	 * Create an integration flow for outbound gRPC requests with streaming responses.
	 * This flow does not specify the method name to be used, this means that the method
	 * name is extracted from the message headers.
	 * @param managedChannel the gRPC managed channel
	 * @return the integration flow
	 */
	@Bean
	IntegrationFlow grpcOutboundFlowStreamResponse(ManagedChannel managedChannel) {
		return flow -> flow
				.handle(Grpc.outboundGateway(managedChannel, SimpleGrpc.class));
	}

	/**
	 * Create an application runner that sends a single gRPC request and receives a single response.
	 * @param grpcInputChannelSingleResponse the message channel for single response requests
	 * @return the application runner
	 */
	@Bean
	ApplicationRunner grpcClientSingleResponse(
			@Autowired @Qualifier("grpcOutboundFlowSingleResponse.input") MessageChannel grpcInputChannelSingleResponse) {

		return args -> {
			HelloReply reply = new MessagingTemplate().convertSendAndReceive(grpcInputChannelSingleResponse,
					HelloRequest.newBuilder().setName("Jack").build(),
					HelloReply.class);

			LOGGER.info("Single response reply: " + (reply != null ? reply.getMessage() : "No reply received"));
		};
	}

	/**
	 * Create an application runner that sends a gRPC request and receives a stream of responses.
	 * @param grpcInputChannel the message channel for streaming response requests
	 * @return the application runner
	 */
	@Bean
	ApplicationRunner grpcClientStreamResponse(
			@Autowired @Qualifier("grpcOutboundFlowStreamResponse.input") MessageChannel grpcInputChannel) {

		return args -> {
			FluxMessageChannel grpcStreamOutputChannel = new FluxMessageChannel();
			Flux.from(grpcStreamOutputChannel)
						.map(message -> (HelloReply) message.getPayload())
						.map(HelloReply::getMessage)
						.doOnNext(msg -> LOGGER.info("Stream received reply: " + msg))
						.doOnError(error -> {
							LOGGER.error("Error in stream: " + error.getMessage(), error);
						})
						.subscribe();

				Message<?> requestMessage = MessageBuilder
						.withPayload(HelloRequest.newBuilder().setName("Jane").build())
						.setReplyChannel(grpcStreamOutputChannel)
						.setHeader(GrpcHeaders.SERVICE_METHOD, "StreamHello")
						.build();
				grpcInputChannel.send(requestMessage);
		};
	}

}
