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

package org.springframework.integration.samples.grpc.server;

import reactor.core.publisher.Flux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.grpc.GrpcHeaders;
import org.springframework.integration.grpc.inbound.GrpcInboundGateway;
import org.springframework.messaging.Message;

/**
 * Configures {@link GrpcInboundGateway}.
 * <p>
 * Demonstrates various gRPC communication patterns:
 * <ul>
 * <li>Unary RPC (SayHello)</li>
 * <li>Server streaming RPC (StreamHello)</li>
 * </ul>
 *
 * @author Glenn Renfro
 */
@Configuration(proxyBeanMethods = false)
public class GrpcServerConfiguration {

	/**
	 * Create a gRPC inbound gateway for the Simple service.
	 * @return the configured gRPC inbound gateway
	 */
	@Bean
	GrpcInboundGateway helloWorldService() {
		return new GrpcInboundGateway(SimpleGrpc.SimpleImplBase.class);
	}

	/**
	 * Create the main integration flow that routes gRPC requests to appropriate handlers
	 * based on the service method name.
	 * @param helloWorldService the gRPC inbound gateway
	 * @return the configured integration flow
	 */
	@Bean
	IntegrationFlow grpcIntegrationFlow(GrpcInboundGateway helloWorldService) {
		return IntegrationFlow.from(helloWorldService)
				.route(Message.class, message ->
								message.getHeaders().get(GrpcHeaders.SERVICE_METHOD, String.class),
						router -> router

								.subFlowMapping("SayHello", flow -> flow
										.transform(this::requestReply))

								.subFlowMapping("StreamHello", flow -> flow
										.transform(this::streamReply))
				)
				.get();
	}

	/**
	 * Handle unary and bidirectional streaming requests by creating a simple greeting reply.
	 * @param helloRequest the incoming request
	 * @return the greeting reply
	 */
	private HelloReply requestReply(HelloRequest helloRequest) {
		return newHelloReply("Hello " + helloRequest.getName());
	}

	/**
	 * Handle server streaming requests by emitting multiple greeting replies.
	 * @param helloRequest the incoming request
	 * @return a flux of greeting replies
	 */
	private Flux<HelloReply> streamReply(HelloRequest helloRequest) {
		return Flux.just(
				newHelloReply("Hello Stream " + helloRequest.getName()),
				newHelloReply("Hello again!"));
	}

	/**
	 * Create a new HelloReply with the specified message.
	 * @param message the message to include in the reply
	 * @return the constructed HelloReply
	 */
	private static HelloReply newHelloReply(String message) {
		return HelloReply.newBuilder().setMessage(message).build();
	}

}

