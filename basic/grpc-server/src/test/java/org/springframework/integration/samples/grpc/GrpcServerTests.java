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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.grpc.server.lifecycle.GrpcServerLifecycle;
import org.springframework.integration.grpc.proto.HelloReply;
import org.springframework.integration.grpc.proto.HelloRequest;
import org.springframework.integration.grpc.proto.HelloWorldServiceGrpc;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Glenn Renfro
 */
@SpringBootTest
@TestPropertySource(properties = "spring.grpc.server.port=0")
class GrpcServerTests {

	@Autowired
	private GrpcServerLifecycle grpcServerLifecycle;

	private int grpcServerPort;

	private ManagedChannel channel;

	private HelloWorldServiceGrpc.HelloWorldServiceBlockingStub blockingStub;

	private HelloWorldServiceGrpc.HelloWorldServiceStub asyncStub;

	@BeforeEach
	void setUp() {
		this.grpcServerPort = this.grpcServerLifecycle.getPort();

		this.channel = ManagedChannelBuilder.forAddress("localhost", this.grpcServerPort)
				.usePlaintext()
				.build();
		this.blockingStub = HelloWorldServiceGrpc.newBlockingStub(this.channel);
		this.asyncStub = HelloWorldServiceGrpc.newStub(this.channel);
	}

	@AfterEach
	void tearDown() throws InterruptedException {
		if (this.channel != null) {
			this.channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
		}
	}

	@Test
	void testSayHelloBlockingCall() {
		HelloRequest request = HelloRequest.newBuilder()
				.setName("Integration Test")
				.build();

		HelloReply response = this.blockingStub.sayHello(request);

		assertThat(response).isNotNull().extracting(HelloReply::getMessage).isEqualTo("Hello Integration Test");
	}

	@Test
	void testStreamSayHello() throws InterruptedException {
		HelloRequest request = HelloRequest.newBuilder()
				.setName("Streaming Test")
				.build();

		List<HelloReply> responses = new ArrayList<>();
		CountDownLatch latch = new CountDownLatch(1);

		StreamObserver<HelloReply> responseObserver = new StreamObserver<HelloReply>() {
			@Override
			public void onNext(HelloReply value) {
				responses.add(value);
			}

			@Override
			public void onError(Throwable t) {
				latch.countDown();
			}

			@Override
			public void onCompleted() {
				latch.countDown();
			}
		};

		this.asyncStub.streamSayHello(request, responseObserver);

		assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
		assertThat(responses).hasSize(2).extracting(HelloReply::getMessage)
				.containsExactly("Hello Streaming Test", "Hello again!");
	}

}
