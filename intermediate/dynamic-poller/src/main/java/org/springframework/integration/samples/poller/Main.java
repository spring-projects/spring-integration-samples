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
package org.springframework.integration.samples.poller;

import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Starts the Spring Context and will initialize the Spring Integration routes.
 *
 * @author Gunnar Hillert
 * @author Gary Russell
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

		LOGGER.info("\n=========================================================="
				+ "\n                                                          "
				+ "\n Welcome to the Spring Integration Dynamic Poller Sample! "
				+ "\n                                                          "
				+ "\n    For more information please visit:                    "
				+ "\n    http://www.springsource.org/spring-integration        "
				+ "\n                                                          "
				+ "\n==========================================================");

		final AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:META-INF/spring/integration/*-context.xml");

		context.registerShutdownHook();

		final Scanner scanner = new Scanner(System.in);

		final DynamicPeriodicTrigger trigger = context.getBean(DynamicPeriodicTrigger.class);

		LOGGER.info("\n========================================================="
				+ "\n                                                         "
				+ "\n    Please press 'q + Enter' to quit the application.    "
				+ "\n                                                         "
				+ "\n=========================================================");

		System.out.print("Please enter a non-negative numeric value and press <enter>: ");

		while (true) {

			final String input = scanner.nextLine();

			if ("q".equals(input.trim())) {
				break;
			}

			try {

				int triggerPeriod = Integer.valueOf(input);

				System.out.println(String.format("Setting trigger period to '%s' ms", triggerPeriod));

				trigger.setPeriod(triggerPeriod);

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
