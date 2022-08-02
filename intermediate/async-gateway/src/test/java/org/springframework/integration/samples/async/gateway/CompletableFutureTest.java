/*
 * Copyright 2002-2022 the original author or authors.
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

package org.springframework.integration.samples.async.gateway;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * @author Oleg Zhurakousky
 * @author Gary Russell
 * @author Artem Bilan
 *
 */
@SpringJUnitConfig
@DirtiesContext
public class CompletableFutureTest {

	private static final Log logger = LogFactory.getLog(CompletableFutureTest.class);

	@Autowired
	private MathGateway gateway;

	@Test
	public void testAsyncGateway() throws Exception {
		Random random = new Random();
		int[] numbers = new int[100];
		int expectedResults = 0;
		for (int i = 0; i < 100; i++) {
			numbers[i] = random.nextInt(200);
			if (numbers[i] > 100) {
				expectedResults++;
			}
		}
		final CountDownLatch latch = new CountDownLatch(expectedResults);
		final AtomicInteger failures = new AtomicInteger();
		for (int i = 0; i < 100; i++) {
			final int number = numbers[i];
			CompletableFuture<Integer> result = gateway.multiplyByTwo(number);

			result.whenComplete((integer, throwable) -> {
				if (throwable == null) {
					logger.info("Result of multiplication of " + number + " by 2 is " + result);
				}
				else {
					failures.incrementAndGet();
					logger.error("Unexpected exception for " + number, throwable);
				}
				latch.countDown();
			});
		}
		assertThat(latch.await(60, TimeUnit.SECONDS)).isTrue();
		assertThat(failures.get()).isEqualTo(0);
		logger.info("Finished");
	}

	@Configuration
	@ComponentScan
	@EnableIntegration
	@IntegrationComponentScan
	public static class TestConfig {

		@Bean
		public MessageChannel gatewayChannel() {
			return new DirectChannel();
		}

		@Bean
		@ServiceActivator(inputChannel = "mathServiceChannel")
		public MathService mathService() {
			return new MathService();
		}

		@Bean
		public AsyncTaskExecutor exec() {
			SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
			simpleAsyncTaskExecutor.setThreadNamePrefix("exec-");
			return simpleAsyncTaskExecutor;
		}

	}

	@MessagingGateway(defaultReplyTimeout = "0", asyncExecutor = "exec")
	public interface MathGateway {

		@Gateway(requestChannel = "gatewayChannel")
		CompletableFuture<Integer> multiplyByTwo(int number);

	}

	@MessageEndpoint
	public static class Gt100Filter {

		@Filter(inputChannel = "gatewayChannel", outputChannel = "mathServiceChannel")
		public boolean filter(int i) {
			return i > 100;
		}

	}

}
