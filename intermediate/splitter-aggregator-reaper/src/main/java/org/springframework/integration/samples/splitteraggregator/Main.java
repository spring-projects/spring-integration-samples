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
package org.springframework.integration.samples.splitteraggregator;

import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.splitteraggregator.support.TestUtils;


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

		LOGGER.info("\n========================================================="
				  + "\n                                                         "
				  + "\n          Welcome to Spring Integration!                 "
				  + "\n                                                         "
				  + "\n    For more information please visit:                   "
				  + "\n    http://www.springsource.org/spring-integration       "
				  + "\n                                                         "
				  + "\n=========================================================" );

		final AbstractApplicationContext context =
				new ClassPathXmlApplicationContext("classpath:META-INF/spring/integration/*-context.xml");

		context.registerShutdownHook();

		final SearchRequestor searchRequestor = context.getBean(SearchRequestor.class);
		final SearchA searchA = context.getBean(SearchA.class);
		final SearchB searchB = context.getBean(SearchB.class);

		final Scanner scanner = new Scanner(System.in);

		System.out.println("Please enter a choice and press <enter>: ");
		System.out.println("\t1. Submit 2 search queries, 2 results returned.");
		System.out.println("\t2. Submit 2 search queries, 1 search query takes too long, 1 result returned.");
		System.out.println("\t3. Submit 2 search queries, 2 search queries take too long, 0 results returned.");

		System.out.println("\tq. Quit the application");
		System.out.print("Enter your choice: ");

		while (true) {
			final String input = scanner.nextLine();

			if("1".equals(input.trim())) {
				searchA.setExecutionTime(1000L);
				searchB.setExecutionTime(1000L);
				final CompositeResult result = searchRequestor.search(TestUtils.getCompositeCriteria());
				System.out.println("Number of Search Results: " + result.getResults().size());
			} else if("2".equals(input.trim())) {
				searchA.setExecutionTime(6000L);
				searchB.setExecutionTime(1000L);
				final CompositeResult result = searchRequestor.search(TestUtils.getCompositeCriteria());
				System.out.println("Number of Search Results: " + result.getResults().size());
			} else if("3".equals(input.trim())) {
				searchA.setExecutionTime(6000L);
				searchB.setExecutionTime(6000L);
				final CompositeResult result = searchRequestor.search(TestUtils.getCompositeCriteria());
				System.out.println("Result is null: " + (result == null));
			} else if("q".equals(input.trim())) {
				break;
			} else {
				System.out.println("Invalid choice\n\n");
			}

		}

		System.out.println("Exiting application...bye.");
		scanner.close();
		context.close();

	}
}
