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

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.grpc.GrpcHeaders;
import org.springframework.integration.grpc.inbound.GrpcInboundGateway;
import org.springframework.integration.samples.grpc.proto.HelloReply;
import org.springframework.integration.samples.grpc.proto.HelloRequest;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Glenn Renfro
 */
@SpringBootTest
@DirtiesContext
class GrpcServerTests {

	@Autowired
	private GrpcInboundGateway helloWorldService;

	@Autowired
	private MessageChannel grpcIntegrationFlowInputChannel;

	@Autowired
	private PollableChannel replyChannel;

	@Test
	void testSayHelloMethodRoutingAndTransformation() {
		HelloRequest request = HelloRequest.newBuilder()
				.setName("Direct Test")
				.build();

		Message<?> requestMessage = MessageBuilder.withPayload(request)
				.setHeader(GrpcHeaders.SERVICE_METHOD, "SayHello")
				.setReplyChannel(this.replyChannel)
				.build();

		this.grpcIntegrationFlowInputChannel.send(requestMessage);

		Message<?> reply = this.replyChannel.receive(5000);

		assertThat(reply.getPayload()).isInstanceOfSatisfying(HelloReply.class,
				req -> assertThat(req.getMessage()).isEqualTo("Hello Direct Test"));
	}

	@Test
	void testStreamSayHelloMethodRoutingAndTransformation() {
		HelloRequest request = HelloRequest.newBuilder()
				.setName("Stream Test")
				.build();

		Message<?> requestMessage = MessageBuilder.withPayload(request)
				.setHeader(GrpcHeaders.SERVICE_METHOD, "StreamSayHello")
				.setReplyChannel(this.replyChannel)
				.build();

		this.grpcIntegrationFlowInputChannel.send(requestMessage);

		Message<?> reply = this.replyChannel.receive(5000);

		assertThat(reply.getPayload()).isInstanceOfSatisfying(Flux.class,
				replyResult -> StepVerifier.create((Flux<HelloReply>) replyResult)
						.consumeNextWith(r -> assertThat(r.getMessage()).isEqualTo("Hello Stream Test"))
						.consumeNextWith(r -> assertThat(r.getMessage()).isEqualTo("Hello again!"))
						.verifyComplete());
	}

	/**
	 * Test configuration that provides access to the integration flow's input channel.
	 * This allows tests to send messages directly to the flow, bypassing the gRPC protocol layer.
	 */
	@TestConfiguration
	static class TestChannelConfig {
		@Bean
		MessageChannel grpcIntegrationFlowInputChannel(GrpcInboundGateway helloWorldService) {
			return helloWorldService.getRequestChannel();
		}

		@Bean
		PollableChannel replyChannel() {
			return new QueueChannel();
		}

	}

}
