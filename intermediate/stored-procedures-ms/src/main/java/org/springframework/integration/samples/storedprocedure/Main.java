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

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
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

		LOGGER.info("""
				
				=========================================================
				                                                         
				          Welcome to Spring Integration!                 
				                                                         
				    For more information please visit:                   
				    https://www.springsource.org/spring-integration       
				                                                         
				========================================================="" );

		final AbstractApplicationContext context =
				new ClassPathXmlApplicationContext("classpath:META-INF/spring/integration/*-context.xml");

		context.registerShutdownHook();

		final Scanner scanner = new Scanner(System.in);

		final StringConversionService service = context.getBean(StringConversionService.class);

		LOGGER.info("""
				
				=========================================================
				                                                         
				    Please press 'q + Enter' to quit the application.    
				                                                         
				=========================================================""");

		LOGGER.info("Please enter a string and press <enter>: ");

		while (!scanner.hasNext("q")) {
			String input = scanner.nextLine();

			LOGGER.info("Converting String to Uppercase using Stored Procedure...");
			String inputUpperCase = service.convertToUpperCase(input);

			LOGGER.info("Retrieving Numeric value via Sql Function...");
			Integer number = service.getNumber();

			LOGGER.info(String.format("Converted '%s' - End Result: '%s_%s'.", input, inputUpperCase, number));
			LOGGER.info("To try again, please enter a string and press <enter>:");
		}

		LOGGER.info("Exiting application...bye.");

		scanner.close();
		context.close();
	}

}
