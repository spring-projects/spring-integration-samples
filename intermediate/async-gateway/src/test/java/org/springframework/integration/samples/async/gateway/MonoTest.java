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

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;
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
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author Oleg Zhurakousky
 * @author Gary Russell
 *
 */
@ContextConfiguration(classes = MonoTest.TestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class MonoTest {

	private static Log logger = LogFactory.getLog(MonoTest.class);

	@Autowired
	private MathGateway gateway;

	@Test
	public void testMonoGateway() throws Exception {
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
			gateway.multiplyByTwo(number)
					.subscribeOn(Schedulers.elastic())
					.filter(p -> p != null)
					.doOnNext(result1 -> {
						logger.info("Result of multiplication of " + number + " by 2 is " + result1);
						latch.countDown();
					})
					.doOnError(t -> {
						failures.incrementAndGet();
						logger.error("Unexpected exception for " + number, t);
					}).subscribe();
		}
		assertTrue(latch.await(60, TimeUnit.SECONDS));
		assertThat(failures.get(), greaterThanOrEqualTo(0));
		logger.info("Finished");
	}

	@Configuration
	@EnableIntegration
	@ComponentScan
	@IntegrationComponentScan
	public static class TestConfig {

		@Bean
		@ServiceActivator(inputChannel = "mathServiceChannel")
		public MathService mathService() {
			return new MathService();
		}

	}

	@MessagingGateway(defaultReplyTimeout = "0")
	public interface MathGateway {

		@Gateway(requestChannel = "gatewayChannel")
		Mono<Integer> multiplyByTwo(int number);

	}

	@MessageEndpoint
	public static class Gt100Filter {

		@Filter(inputChannel = "gatewayChannel", outputChannel = "mathServiceChannel")
		public boolean filter(int i) {
			return i > 100;
		}

	}

}
