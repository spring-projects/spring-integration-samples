/*
 * Copyright 2014-2015 the original author or authors.
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

package org.springframework.integration.samples.dsl.cafe.lambda;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.samples.cafe.Delivery;
import org.springframework.integration.samples.cafe.Drink;
import org.springframework.integration.samples.cafe.DrinkType;
import org.springframework.integration.samples.cafe.Order;
import org.springframework.integration.samples.cafe.OrderItem;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.stream.CharacterStreamWritingMessageHandler;

import com.google.common.util.concurrent.Uninterruptibles;

/**
 * @author Artem Bilan
 * @since 3.0
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

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

	private AtomicInteger hotDrinkCounter = new AtomicInteger();

	private AtomicInteger coldDrinkCounter = new AtomicInteger();

	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata poller() {
		return Pollers.fixedDelay(1000).get();
	}

	@Bean
	public IntegrationFlow orders() {
		return f -> f
				.split(Order.class, Order::getItems)
				.channel(c -> c.executor(Executors.newCachedThreadPool()))
				.<OrderItem, Boolean>route(OrderItem::isIced, mapping -> mapping
						.subFlowMapping(true, sf -> sf
								.channel(c -> c.queue(10))
								.publishSubscribeChannel(c -> c
										.subscribe(s -> s.handle(m -> Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS)))
										.subscribe(sub -> sub
												.<OrderItem, String>transform(p ->
														Thread.currentThread().getName()
																+ " prepared cold drink #" + this.coldDrinkCounter.incrementAndGet()
																+ " for order #" + p.getOrderNumber() + ": " + p)
												.handle(m -> System.out.println(m.getPayload())))))
						.subFlowMapping(false, sf -> sf
								.channel(c -> c.queue(10))
								.publishSubscribeChannel(c -> c
										.subscribe(s -> s.handle(m -> Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS)))
										.subscribe(sub -> sub
												.<OrderItem, String>transform(p ->
														Thread.currentThread().getName()
																+ " prepared hot drink #" + this.hotDrinkCounter.incrementAndGet()
																+ " for order #" + p.getOrderNumber() + ": " + p)
												.handle(m -> System.out.println(m.getPayload())))))
						.defaultOutputToParentFlow())
				.<OrderItem, Drink>transform(orderItem ->
						new Drink(orderItem.getOrderNumber(),
								orderItem.getDrinkType(),
								orderItem.isIced(),
								orderItem.getShots()))
				.aggregate(aggregator -> aggregator
						.outputProcessor(g ->
								new Delivery(g.getMessages()
										.stream()
										.map(message -> (Drink) message.getPayload())
										.collect(Collectors.toList())))
						.correlationStrategy(m -> ((Drink) m.getPayload()).getOrderNumber()))
				.handle(CharacterStreamWritingMessageHandler.stdout());
	}

}
