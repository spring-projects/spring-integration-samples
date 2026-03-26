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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.grpc.stub.StreamObserver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.grpc.test.autoconfigure.AutoConfigureTestGrpcTransport;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.grpc.client.ImportGrpcClients;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the gRPC server demonstrating various communication patterns.
 *
 * @author Glenn Renfro
 */
@SpringBootTest
@ImportGrpcClients(
		types = {
				SimpleGrpc.SimpleBlockingStub.class,
				SimpleGrpc.SimpleStub.class
		})
@AutoConfigureTestGrpcTransport
class GrpcServerTests {

	private static final Log LOGGER = LogFactory.getLog(GrpcServerTests.class);

	/**
	 * The blocking stub for the Simple service.
	 */
	@Autowired
	SimpleGrpc.SimpleBlockingStub simpleBlockingStub;

	/**
	 * The asynchronous stub for the Simple service.
	 */
	@Autowired
	SimpleGrpc.SimpleStub simpleStub;

	/**
	 * Test unary RPC where client sends a single request and receives a single response.
	 */
	@Test
	void unary() {
		HelloReply reply = this.simpleBlockingStub.sayHello(HelloRequest.newBuilder().setName("Jack").build());
		assertThat(reply).extracting(HelloReply::getMessage).isEqualTo("Hello Jack");
	}

	/**
	 * Test server streaming RPC where client sends a single request and receives
	 * a stream of responses.
	 * @throws InterruptedException if the test is interrupted while waiting
	 */
	@Test
	void serverStreaming() throws InterruptedException {
		List<HelloReply> replies = new ArrayList<>();
		CountDownLatch replyLatch = new CountDownLatch(1);
		this.simpleStub.streamHello(HelloRequest.newBuilder().setName("Jane").build(),
				replyObserver(replies::add, replyLatch));

		assertThat(replyLatch.await(10_000, TimeUnit.MILLISECONDS)).isTrue();

		assertThat(replies)
				.extracting(HelloReply::getMessage)
				.containsOnly("Hello Stream Jane", "Hello again!");
	}

	/**
	 * Create a stream observer for handling asynchronous gRPC responses.
	 * @param <R> the response type
	 * @param replyConsumer consumer to process each reply
	 * @param completionLatch latch to signal completion
	 * @return the configured stream observer
	 */
	private static <R> StreamObserver<R> replyObserver(Consumer<R> replyConsumer,
			CountDownLatch completionLatch) {

		return new StreamObserver<>() {

			@Override
			public void onNext(R value) {
				replyConsumer.accept(value);
			}

			@Override
			public void onError(Throwable t) {
				LOGGER.error("Error occurred while obtaining items from the stream", t);
				completionLatch.countDown();
			}

			@Override
			public void onCompleted() {
				completionLatch.countDown();
			}

		};
	}

}

