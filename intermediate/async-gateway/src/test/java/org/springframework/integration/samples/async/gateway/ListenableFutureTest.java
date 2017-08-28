/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.integration.samples.async.gateway;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @author Oleg Zhurakousky
 * @author Gary Russell
 *
 */
@ContextConfiguration(classes = ListenableFutureTest.TestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class ListenableFutureTest {

	private static Log logger = LogFactory.getLog(ListenableFutureTest.class);

	@Autowired
	private MathGateway gateway;

	@Test
	public void testAsyncGateway() throws Exception{
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
			ListenableFuture<Integer> result = gateway.multiplyByTwo(number);
			ListenableFutureCallback<Integer> callback = new ListenableFutureCallback<Integer>() {

				@Override
				public void onSuccess(Integer result) {
					logger.info("Result of multiplication of " + number + " by 2 is " + result);
					latch.countDown();
				}

				@Override
				public void onFailure(Throwable t) {
					failures.incrementAndGet();
					logger.error("Unexpected exception for " + number, t);
					latch.countDown();
				}
			};
			result.addCallback(callback);
		}
		assertTrue(latch.await(60, TimeUnit.SECONDS));
		assertEquals(0, failures.get());
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
		@ServiceActivator(inputChannel="mathServiceChannel")
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
		ListenableFuture<Integer> multiplyByTwo(int number);

	}

	@MessageEndpoint
	public static class Gt100Filter {

		@Filter(inputChannel="gatewayChannel", outputChannel="mathServiceChannel")
		public boolean filter(int i) {
			return i > 100;
		}

	}

}
