/*
 * Copyright 2014 the original author or authors.
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

package org.springframework.integration.samples.dsl.cafe.nonlambda;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Aggregator;
import org.springframework.integration.annotation.CorrelationStrategy;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.AggregatorSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.RouterSpec;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.support.Consumer;
import org.springframework.integration.dsl.support.GenericHandler;
import org.springframework.integration.router.ExpressionEvaluatingRouter;
import org.springframework.integration.samples.cafe.Delivery;
import org.springframework.integration.samples.cafe.Drink;
import org.springframework.integration.samples.cafe.DrinkType;
import org.springframework.integration.samples.cafe.Order;
import org.springframework.integration.samples.cafe.OrderItem;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.stream.CharacterStreamWritingMessageHandler;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.Uninterruptibles;

/**
 * @author Artem Bilan
 * @since 3.0
 */
@SpringBootApplication
@IntegrationComponentScan
public class Application {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx =
				SpringApplication.run(Application.class, args);

		Cafe cafe = ctx.getBean(Cafe.class);
		for (int i = 1; i <= 100; i++) {
			Order order = new Order(i);
			order.addItem(DrinkType.LATTE, 2, false);
			order.addItem(DrinkType.MOCHA, 3, true);
			cafe.placeOrder(order);
		}

		System.out.println("Hit 'Enter' to terminate");
		System.in.read();
		ctx.close();
	}

	@MessagingGateway
	public interface Cafe {

		@Gateway(requestChannel = "orders.input")
		void placeOrder(Order order);

	}

	private final AtomicInteger hotDrinkCounter = new AtomicInteger();

	private final AtomicInteger coldDrinkCounter = new AtomicInteger();

	@Autowired
	private CafeAggregator cafeAggregator;

	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata poller() {
		return Pollers.fixedDelay(1000).get();
	}

	@Bean
	public IntegrationFlow orders() {
		return IntegrationFlows.from("orders.input")
				.split("payload.items")
				.channel(MessageChannels.executor(Executors.newCachedThreadPool()))
				.route("payload.iced",
						new Consumer<RouterSpec<Object, ExpressionEvaluatingRouter>>() {

							@Override
							public void accept(RouterSpec<Object, ExpressionEvaluatingRouter> spec) {
								spec.channelMapping(true, "iced")
										.channelMapping(false, "hot");
							}

						})
				.get();
	}

	@Bean
	public IntegrationFlow icedFlow() {
		return IntegrationFlows.from(MessageChannels.queue("iced", 10))
				.handle(new GenericHandler<OrderItem>() {

					@Override
					public Object handle(OrderItem payload, Map<String, Object> headers) {
						Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
						System.out.println(Thread.currentThread().getName()
								+ " prepared cold drink #" + coldDrinkCounter.incrementAndGet()
								+ " for order #" + payload.getOrderNumber() + ": " + payload);
						return payload;
					}

				})
				.channel("output")
				.get();
	}

	@Bean
	public IntegrationFlow hotFlow() {
		return IntegrationFlows.from(MessageChannels.queue("hot", 10))
				.handle(new GenericHandler<OrderItem>() {

					@Override
					public Object handle(OrderItem payload, Map<String, Object> headers) {
						Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
						System.out.println(Thread.currentThread().getName()
								+ " prepared hot drink #" + hotDrinkCounter.incrementAndGet()
								+ " for order #" + payload.getOrderNumber() + ": " + payload);
						return payload;
					}

				})
				.channel("output")
				.get();
	}

	@Bean
	public IntegrationFlow resultFlow() {
		return IntegrationFlows.from("output")
				.transform(new GenericTransformer<OrderItem, Drink>() {

					@Override
					public Drink transform(OrderItem orderItem) {
						return new Drink(orderItem.getOrderNumber(),
								orderItem.getDrinkType(),
								orderItem.isIced(),
								orderItem.getShots());
					}

				})
				.aggregate(new Consumer<AggregatorSpec>() {

					@Override
					public void accept(AggregatorSpec aggregatorSpec) {
						aggregatorSpec.processor(cafeAggregator, null);
					}

				})
				.handle(CharacterStreamWritingMessageHandler.stdout())
				.get();
	}


	@Component
	public static class CafeAggregator {

		@Aggregator
		public Delivery output(List<Drink> drinks) {
			return new Delivery(drinks);
		}

		@CorrelationStrategy
		public Integer correlation(Drink drink) {
			return drink.getOrderNumber();
		}

	}

}
