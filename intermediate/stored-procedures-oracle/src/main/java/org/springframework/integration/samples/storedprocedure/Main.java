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

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.integration.model.CoffeeBeverage;
import org.springframework.integration.service.CoffeeService;
import org.springframework.integration.service.StringConversionService;


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

	private Main() { }

	/**
	 * Load the Spring Integration Application Context
	 *
	 * @param args - command line arguments
	 */
	public static void main(final String... args) {

		final Scanner scanner = new Scanner(System.in);

		LOGGER.info("\n========================================================="
				  + "\n                                                         "
				  + "\n          Welcome to Spring Integration's                "
				  + "\n     Stored Procedure/Function Sample for Oracle         "
				  + "\n                                                         "
				  + "\n    For more information please visit:                   "
				  + "\n    http://www.springsource.org/spring-integration       "
				  + "\n                                                         "
				  + "\n=========================================================" );

		while (true) {

			System.out.println("Please enter a choice and press <enter>: ");
			System.out.println("\t1. Execute Sample 1 (Capitalize String)");
			System.out.println("\t2. Execute Sample 2 (Coffee Service)");
			System.out.println("\tq. Quit the application");

			final String input = scanner.nextLine();

			if("1".equals(input.trim())) {
				executeSample1();
				continue;
			} else if("2".equals(input.trim())) {
				executeSample2();
				continue;
			} else if("q".equals(input.trim())) {
				break;
			} else {
				System.out.println("Invalid choice\n\n");
				System.out.print("Enter you choice: ");
			}
		}

		System.out.println("Exiting application.");
		scanner.close();

	}

	private static void executeSample1() {

		final Scanner scanner = new Scanner(System.in);

		final GenericXmlApplicationContext context = new GenericXmlApplicationContext();
		context.load("classpath:META-INF/spring/integration/spring-integration-sample1-context.xml");
		context.registerShutdownHook();
		context.refresh();

		final StringConversionService service = context.getBean(StringConversionService.class);

		final String message =
				  "\n========================================================="
				+ "\n                                                         "
				+ "\n    Please press 'q + Enter' to quit the application.    "
				+ "\n                                                         "
				+ "\n========================================================="
				+ "\n\n Please enter a string and press <enter>: ";

		System.out.print(message);

		while (!scanner.hasNext("q")) {
			String input = scanner.nextLine();

			System.out.println("Converting String to Uppcase using Stored Procedure...");
			String inputUpperCase = service.convertToUpperCase(input);

			System.out.println("Retrieving Numeric value via Sql Function...");
			Integer number = service.getNumber();

			System.out.println(String.format("Converted '%s' - End Result: '%s_%s'.", input, inputUpperCase, number));
			System.out.print("To try again, please enter a string and press <enter>:");
		}

		scanner.close();
		context.close();
		System.out.println("Back to main menu.");

	}

	private static void executeSample2() {

		final Scanner scanner = new Scanner(System.in);

		final GenericXmlApplicationContext context = new GenericXmlApplicationContext();
		context.load("classpath:META-INF/spring/integration/spring-integration-sample2-context.xml");
		context.registerShutdownHook();
		context.refresh();

		final CoffeeService service = context.getBean(CoffeeService.class);

		final String message = "\n\n" +
			"* Please enter 'list' and press <enter> to get a list of coffees.\n" +
			"* Enter a coffee id, e.g. '1' and press <enter> to get a description.\n" +
			"* Please press 'q + Enter' to quit the application.\n";

		System.out.println(message);

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

		scanner.close();
		context.close();

		System.out.println("Back to main menu.");
	}

}
