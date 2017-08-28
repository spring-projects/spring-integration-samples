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
package org.springframework.integration.samples.mailattachments;

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
 * @since 2.2
 *
 */
public final class Main {

	private static final Log LOGGER = LogFactory.getLog(Main.class);

	private static final String HORIZONTAL_LINE =
		"\n=========================================================";

	private Main() { }

	/**
	 * Load the Spring Integration Application Context
	 *
	 * @param args - command line arguments
	 */
	public static void main(final String... args) {

		LOGGER.info(HORIZONTAL_LINE
				  + "\n"
				  + "\n          Welcome to Spring Integration!                 "
				  + "\n"
				  + "\n    For more information please visit:                   "
				  + "\n    http://www.springsource.org/spring-integration       "
				  + "\n"
				  + HORIZONTAL_LINE );

		final AbstractApplicationContext context =
				new ClassPathXmlApplicationContext("classpath:META-INF/spring/integration/*-context.xml");

		context.registerShutdownHook();

		final Scanner scanner = new Scanner(System.in);

		LOGGER.info(HORIZONTAL_LINE
				  + "\n"
				  + "\n    Please press 'q + Enter' to quit the application.    "
				  + "\n"
				  + HORIZONTAL_LINE );

		while (true) {

			final String input = scanner.nextLine();

			if("q".equals(input.trim())) {
				break;
			}

		}

		LOGGER.info("Exiting application...bye.");

		scanner.close();
		context.close();

	}
}
