/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 */

package org.springframework.integration.samples.poller;

import java.time.Duration;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.util.DynamicPeriodicTrigger;

/**
 * Starts the Spring Context and will initialize the Spring Integration routes.
 *
 * @author Gunnar Hillert
 * @author Gary Russell
 * @author Artem Bilan
 *
 * @version 1.0
 *
 */
public final class Main {

	private static final Log LOGGER = LogFactory.getLog(Main.class);

	private Main() {
	}

	/**
	 * Load the Spring Integration Application Context
	 *
	 * @param args - command line arguments
	 */
	public static void main(final String... args) {

		LOGGER.info("""

				=========================================================
				                                                          
				 Welcome to the Spring Integration Dynamic Poller Sample! 
				                                                          
				    For more information please visit:                    
				    https://www.springsource.org/spring-integration        
				                                                          
				==========================================================");

		final AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:META-INF/spring/integration/*-context.xml");

		context.registerShutdownHook();

		final Scanner scanner = new Scanner(System.in);

		final DynamicPeriodicTrigger trigger = context.getBean(DynamicPeriodicTrigger.class);

		LOGGER.info("""

				=========================================================
				                                                         
				    Please press 'q + Enter' to quit the application.    
				                                                         
				=========================================================""");

		System.out.print("Please enter a non-negative numeric value and press <enter>: ");

		while (true) {

			final String input = scanner.nextLine();

			if ("q".equals(input.trim())) {
				break;
			}

			try {

				int triggerPeriod = Integer.valueOf(input);

				System.out.println(String.format("Setting trigger period to '%s' ms", triggerPeriod));

				trigger.setDuration(Duration.ofMillis(triggerPeriod));

			}
			catch (Exception e) {
				LOGGER.error("An exception was caught: " + e);
			}

			System.out.print("Please enter a non-negative numeric value and press <enter>: ");

		}

		LOGGER.info("Exiting application...bye.");

		scanner.close();
		context.close();

	}
}
