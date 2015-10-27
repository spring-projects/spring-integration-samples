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

package org.springframework.integration.samples.dsl.cafe;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.samples.cafe.Delivery;
import org.springframework.integration.samples.cafe.Drink;
import org.springframework.integration.samples.cafe.DrinkType;
import org.springframework.integration.samples.cafe.Order;
import org.springframework.integration.samples.cafe.OrderItem;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.stream.CharacterStreamWritingMessageHandler;

/**
 * @author Artem Bilan
 * @since 3.0
 */
@Configuration
@EnableAutoConfiguration
@IntegrationComponentScan
public class Application {

	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

		Cafe cafe = ctx.getBean(Cafe.class);
		for (int i = 1; i <= 100; i++) {
			Order order = new Order(i);
			order.addItem(DrinkType.LATTE, 2, false);
			order.addItem(DrinkType.MOCHA, 3, true);
			cafe.placeOrder(order);
		}

		Thread.sleep(60000);

		ctx.close();
	}

	@MessagingGateway
	public interface Cafe {

		@Gateway(requestChannel = "orders")
		void placeOrder(Order order);

	}

	private final AtomicInteger hotDrinkCounter = new AtomicInteger();

	private final AtomicInteger coldDrinkCounter = new AtomicInteger();

	@Bean
	public Executor taskExecutor() {
		return Executors.newCachedThreadPool();
	}

	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata poller() {
		return Pollers.fixedDelay(1000).get();
	}

	@Bean
	public IntegrationFlow ordersFlow() {
		return IntegrationFlows.from("orders")
				.<Order>split(Order::getItems, null)
				.channel(MessageChannels.executor(this.taskExecutor()))
				.<OrderItem, String>route(orderItem -> orderItem.isIced() ? "coldDrinks" : "hotDrinks")
				.get();
	}

	@Bean
	@DependsOn("preparedDrinksFlow")
	public IntegrationFlow coldDrinksFlow() {
		return IntegrationFlows.from(MessageChannels.queue("coldDrinks", 10))
				.<OrderItem>handle((orderItem, h) -> {
					try {
						Thread.sleep(1000);
					}
					catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						return null;
					}
					System.out.println(Thread.currentThread().getName()
							+ " prepared cold drink #" + this.coldDrinkCounter.incrementAndGet() + " for order #"
							+ orderItem.getOrderNumber() + ": " + orderItem);

					return orderItem;
				})
				.channel("preparedDrinks")
				.get();
	}

	@Bean
	@DependsOn("preparedDrinksFlow")
	public IntegrationFlow hotDrinksFlow() {
		return IntegrationFlows.from(MessageChannels.queue("hotDrinks", 10))
				.<OrderItem>handle((orderItem, h) -> {
					try {
						Thread.sleep(5000);
					}
					catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						return null;
					}
					System.out.println(Thread.currentThread().getName()
							+ " prepared hot drink #" + this.hotDrinkCounter.incrementAndGet() + " for order #"
							+ orderItem.getOrderNumber() + ": " + orderItem);

					return orderItem;
				})
				.channel("preparedDrinks")
				.get();
	}


	@Bean
	public IntegrationFlow preparedDrinksFlow() {
		return IntegrationFlows.from("preparedDrinks")
				.<OrderItem, Drink>transform(orderItem ->
								new Drink(orderItem.getOrderNumber(),
										orderItem.getDrinkType(),
										orderItem.isIced(),
										orderItem.getShots())
				)
				.aggregate(aggregator ->
						aggregator.outputProcessor(g ->
										new Delivery(g.getMessages()
												.stream()
												.map(message -> (Drink) message.getPayload())
												.collect(Collectors.toList()))
						)
								.correlationStrategy(m -> ((Drink) m.getPayload()).getOrderNumber())
						, null)
				.handle(CharacterStreamWritingMessageHandler.stdout())
				.get();
	}

}
