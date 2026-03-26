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

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.grpc.test.autoconfigure.AutoConfigureTestGrpcTransport;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * Integration test for gRPC client functionality.
 *
 * @author Glenn Renfro
 */
@SpringBootTest
@DirtiesContext
@ExtendWith(OutputCaptureExtension.class)
@AutoConfigureTestGrpcTransport
public class GrpcClientTests {

	/**
	 * Verify the gRPC service returns the expected greeting message.
	 * @param output the captured console output
	 */
	@Test
	void verifyMessage(CapturedOutput output) {
		await()
				.atMost(Duration.ofSeconds(5))
				.untilAsserted(() -> {
					assertThat(output.getAll())
							.contains("Single response reply: Hello Jack")
							.contains("Stream received reply: Hello Jane")
							.contains("Stream received reply: Hello Again");
				});
	}

	@TestConfiguration(proxyBeanMethods = false)
	static class TestGrpcServerConfig {

		/**
		 * Create a test gRPC service implementation for the Simple service.
		 * @return the gRPC service implementation
		 */
		@Bean
		SimpleGrpc.SimpleImplBase simpleService() {
			return new SimpleGrpc.SimpleImplBase() {

				@Override
				public void sayHello(HelloRequest request, io.grpc.stub.StreamObserver<HelloReply> responseObserver) {
					HelloReply reply = HelloReply.newBuilder()
							.setMessage("Hello " + request.getName())
							.build();
					responseObserver.onNext(reply);
					responseObserver.onCompleted();
				}

				@Override
				public void streamHello(HelloRequest request,
						io.grpc.stub.StreamObserver<HelloReply> responseObserver) {

					HelloReply reply = HelloReply.newBuilder()
							.setMessage("Hello " + request.getName())
							.build();
					responseObserver.onNext(reply);
					reply = HelloReply.newBuilder()
							.setMessage("Hello Again")
							.build();
					responseObserver.onNext(reply);
					responseObserver.onCompleted();
				}
			};
		}
	}

}
