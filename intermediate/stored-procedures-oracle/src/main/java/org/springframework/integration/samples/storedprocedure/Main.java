/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 */
package org.springframework.integration.samples.storedprocedure;

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

		LOGGER.info("""
				
				=========================================================
				                                                         
				          Welcome to Spring Integration's                
				     Stored Procedure/Function Sample for Oracle         
				                                                         
				    For more information please visit:                   
				    https://www.springsource.org/spring-integration       
				                                                         
				========================================================="" );

		while (true) {

			LOGGER.info("Please enter a choice and press <enter>: ");
			LOGGER.info("""
				\t1. Execute Sample 1 (Capitalize String)
				\t2. Execute Sample 2 (Coffee Service)
				\tq. Quit the application""");

			final String input = scanner.nextLine();

			if("1".equals(input.trim())) {
				executeSample1(scanner);
				continue;
			} else if("2".equals(input.trim())) {
				executeSample2(scanner);
				continue;
			} else if("q".equals(input.trim())) {
				break;
			} else {
				LOGGER.info("Invalid choice\n\n");
				LOGGER.info("Enter you choice: ");
			}
		}

		LOGGER.info("Exiting application.");
		scanner.close();

	}

	private static void executeSample1(Scanner scanner) {

		GenericXmlApplicationContext context = null;
		try {
			context = new GenericXmlApplicationContext();
			context.load("classpath:META-INF/spring/integration/spring-integration-sample1-context.xml");
			context.registerShutdownHook();
			context.refresh();

			final StringConversionService service = context.getBean(StringConversionService.class);

			LOGGER.info("""

					=========================================================
					                                                         
					    Please press 'q + Enter' to quit the application.    
					                                                         
					=========================================================
					
					 Please enter a string and press <enter>: """);



			while (!scanner.hasNext("q")) {
				String input = scanner.nextLine();

				LOGGER.info("Converting String to Uppercase using Stored Procedure...");
				String inputUpperCase = service.convertToUpperCase(input);

				LOGGER.info("Retrieving Numeric value via Sql Function...");
				Integer number = service.getNumber();

				LOGGER.info(String.format("Converted '%s' - End Result: '%s_%s'.", input, inputUpperCase, number));
				LOGGER.info("To try again, please enter a string and press <enter>:");
			}


		} finally {
			if (context != null) {
				context.close();
			}

			LOGGER.info("Back to main menu.");
		}

	}

	private static void executeSample2(Scanner scanner) {

		GenericXmlApplicationContext context = null;
		try {
			context = new GenericXmlApplicationContext();
			context.load("classpath:META-INF/spring/integration/spring-integration-sample2-context.xml");
			context.registerShutdownHook();
			context.refresh();

			final CoffeeService service = context.getBean(CoffeeService.class);

			LOGGER.info("""

					
					* Please enter 'list' and press <enter> to get a list of coffees.
					* Enter a coffee id, e.g. '1' and press <enter> to get a description.
					* Please press 'q + Enter' to quit the application.
					""");


			while (!scanner.hasNext("q")) {

				String input = scanner.nextLine();

				if ("list".equalsIgnoreCase(input)) {
					List<CoffeeBeverage> coffeeBeverages = service.findAllCoffeeBeverages();

					for (CoffeeBeverage coffeeBeverage : coffeeBeverages) {
						LOGGER.info(String.format("%s - %s", coffeeBeverage.getId(),
								coffeeBeverage.getName()));
					}

				} else {
					LOGGER.info("Retrieving coffee information...");
					String coffeeDescription = service.findCoffeeBeverage(Integer.valueOf(input));

					LOGGER.info(String.format("Searched for '%s' - Found: '%s'.", input, coffeeDescription));
					LOGGER.info("To try again, please enter another coffee beverage and press <enter>:\n\n");
				}

			}


		} finally {
			if (context != null) {
				context.close();
			}

			LOGGER.info("Back to main menu.");
		}
	}

}
