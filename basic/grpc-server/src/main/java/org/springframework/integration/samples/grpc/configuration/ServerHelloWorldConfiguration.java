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

import reactor.core.publisher.Flux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.grpc.GrpcHeaders;
import org.springframework.integration.grpc.inbound.GrpcInboundGateway;
import org.springframework.integration.grpc.proto.HelloReply;
import org.springframework.integration.grpc.proto.HelloRequest;
import org.springframework.integration.grpc.proto.HelloWorldServiceGrpc;
import org.springframework.messaging.Message;

/**
 * Configuration class for the gRPC server sample.
 * Configures the gRPC inbound gateway and integration flows for handling
 * gRPC service methods for single request/response and request/streaming responses.
 *
 * @author Glenn Renfro
 */
@Configuration
class ServerHelloWorldConfiguration {

	/**
	 * Creates the main integration flow for handling incoming gRPC requests.
	 * Routes requests to different sub-flows based on the service method name.
	 *
	 * @param helloWorldService the gRPC inbound gateway
	 * @return the integration flow
	 */
	@Bean
	IntegrationFlow grpcIntegrationFlow(GrpcInboundGateway helloWorldService) {
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

	/**
	 * Creates the gRPC inbound gateway for the HelloWorld service.
	 *
	 * @return the configured gRPC inbound gateway
	 */
	@Bean
	GrpcInboundGateway helloWorldService() {
		return new GrpcInboundGateway(HelloWorldServiceGrpc.HelloWorldServiceImplBase.class);
	}

}
