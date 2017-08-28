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
package org.springframework.integration.samples.storedprocedure;

import java.util.List;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.model.CoffeeBeverage;
import org.springframework.integration.service.CoffeeService;


/**
 * Starts the Spring Context and will initialize the Spring Integration routes.
 *
 * @author Gunnar Hillert
 * @author Gary Russell
 * @since 2.2
 *
 */
public class Main {

	private static final Log LOGGER = LogFactory.getLog(Main.class);

	private static final String LINE    = "\n=========================================================";
	private static final String NEWLINE = "\n";

	private Main() { }

	/**
	 * Load the Spring Integration Application Context
	 *
	 * @param args - command line arguments
	 */
	public static void main(final String... args) {

		LOGGER.info(LINE
				+ LINE
				+ "\n    Welcome to Spring Integration Coffee Database!       "
				+ NEWLINE
				+ "\n    For more information please visit:                   "
				+ "\n    http://www.springsource.org/spring-integration       "
				+ NEWLINE
				+ LINE );

		final AbstractApplicationContext context =
			new ClassPathXmlApplicationContext("classpath:META-INF/spring/integration/*-context.xml");

		context.registerShutdownHook();

		final Scanner scanner = new Scanner(System.in);

		final CoffeeService service = context.getBean(CoffeeService.class);

		LOGGER.info(LINE
				+ NEWLINE
				+ "\n    Please press 'q + Enter' to quit the application."
				+ NEWLINE
				+ LINE);

		System.out.print("Please enter 'list' and press <enter> to get a list of coffees.");
		System.out.print("Enter a coffee id, e.g. '1' and press <enter> to get a description.\n\n");

		while (!scanner.hasNext("q")) {

			String input = scanner.nextLine();

			if ("list".equalsIgnoreCase(input)) {
				List<CoffeeBeverage> coffeeBeverages = service.findAllCoffeeBeverages();

				for (CoffeeBeverage coffeeBeverage : coffeeBeverages) {
					System.out.println(String.format("%s - %s", coffeeBeverage.getId(),
																coffeeBeverage.getName()));
				}

			} else {
				System.out.println("Retrieving coffee information...");
				String coffeeDescription = service.findCoffeeBeverage(Integer.valueOf(input));

				System.out.println(String.format("Searched for '%s' - Found: '%s'.", input, coffeeDescription));
				System.out.print("To try again, please enter another coffee beverage and press <enter>:\n\n");
			}

		}

		LOGGER.info("Exiting application...bye.");

		scanner.close();
		context.close();

	}
}
