/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 */

package org.springframework.integration.samples.dsl.cafe.lambda;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
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
@SpringBootApplication
public class Application {

	private static final Log LOGGER = LogFactory.getLog(Application.class);

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

		Cafe cafe = ctx.getBean(Cafe.class);
		for (int i = 1; i <= 100; i++) {
			Order order = new Order(i);
			order.addItem(DrinkType.LATTE, 2, false);
			order.addItem(DrinkType.MOCHA, 3, true);
			cafe.placeOrder(order);
		}

		LOGGER.info("Hit 'Enter' to terminate");
		waitForEnter(ctx);
	}

	private static void waitForEnter(ConfigurableApplicationContext ctx) {
		try {
			//System.in.read();
			LOGGER.info("Press enter to terminate");
			System.in.read();
		}
		catch (IOException e) {
			LOGGER.error("Error reading from System.in", e);
		}
		finally {
			ctx.close();
		}
	}

	@MessagingGateway
	interface Cafe {

		@Gateway(requestChannel = "orders.input")
		void placeOrder(Order order);

	}

	private final AtomicInteger hotDrinkCounter = new AtomicInteger();

	private final AtomicInteger coldDrinkCounter = new AtomicInteger();

	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	PollerSpec poller() {
		return Pollers.fixedDelay(1000);
	}

	@Bean
	IntegrationFlow orders() {
		return f -> f
				.split(Order.class, Order::getItems)
				.channel(c -> c.executor(Executors.newCachedThreadPool()))
				.route(OrderItem.class, OrderItem::isIced, mapping -> mapping
						.subFlowMapping(true, this::buildColdDrinkSubFlow)
						.subFlowMapping(false, this::buildHotDrinkSubFlow))
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

	private void buildColdDrinkSubFlow(IntegrationFlow.Builder sf) {
		buildDrinkSubFlow(sf, true, coldDrinkCounter, 1, "cold");
	}

	private void buildHotDrinkSubFlow(IntegrationFlow.Builder sf) {
		buildDrinkSubFlow(sf, false, hotDrinkCounter, 5, "hot");
	}

	private static void buildDrinkSubFlow(IntegrationFlow.Builder sf, boolean isIced,
															 AtomicInteger drinkCounter, long sleepTime, String drinkType) {
		sf
				.channel(c -> c.queue(10))
				.publishSubscribeChannel(c -> c
						.subscribe(s -> s.handle(m -> sleepUninterruptibly(sleepTime, TimeUnit.SECONDS)))
						.subscribe(sub -> sub
								.<OrderItem, String>transform(p ->
										Thread.currentThread().getName() +
												" prepared " + drinkType + " drink #" +
												(isIced ? "" : "") +
												" for order #" + p.getOrderNumber() + ": " + p)
								.handle(m -> LOGGER.info(m.getPayload()))))
				.bridge();
	}

	private static void sleepUninterruptibly(long sleepFor, TimeUnit unit) {
		boolean interrupted = false;
		try {
			unit.sleep(sleepFor);
		}
		catch (InterruptedException e) {
			interrupted = true;
		}
		finally {
			if (interrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}

}
