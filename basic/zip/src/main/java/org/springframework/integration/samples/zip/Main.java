/*
 * Copyright 2015-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.samples.zip;

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
 * @author Artem Bilan
 *
 * @since 6.4
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

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("""
					
					=========================================================\
					
					                                                         \
					
					    Welcome to the Spring Integration Zip Sample         \
					
					                                                         \
					
					    For more information please visit:                   \
					
					    https://www.springsource.org/spring-integration       \
					
					                                                         \
					
					=========================================================""");
		}

		final AbstractApplicationContext context =
				new ClassPathXmlApplicationContext("classpath:META-INF/spring/integration/*-context.xml");

		context.registerShutdownHook();

		SpringIntegrationUtils.displayDirectories(context);

		final Scanner scanner = new Scanner(System.in);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("""
					
					=========================================================\
					
					                                                         \
					
					    Please press 'q + Enter' to quit the application.    \
					
					                                                         \
					
					=========================================================""");
		}

		while (true) {
			if (scanner.hasNext("q")) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("Exiting application...bye.");
				}
				scanner.close();
				System.exit(0);
			}
		}
	}

}
