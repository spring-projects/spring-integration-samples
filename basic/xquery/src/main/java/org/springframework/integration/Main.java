/*
 * Copyright 2002-2012 the original author or authors.
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
package org.springframework.integration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.integration.service.CustomerService;
import org.springframework.util.ClassUtils;
import org.springframework.util.FileCopyUtils;


/**
 * Starts the Spring Context and will initialize the Spring Integration routes.
 *
 * @author Gunnar Hillert
 * @version 1.0
 *
 */
public final class Main {

	private static final Logger LOGGER = Logger.getLogger(Main.class);

	private static final String HORIZONTAL_LINE = "=========================================================\n";

	private Main() { }

	/**
	 * Load the Spring Integration Application Context
	 *
	 * @param args - command line arguments
	 * @throws IOException
	 */
	public static void main(final String... args) throws IOException {

		final Scanner scanner = new Scanner(System.in);

		System.out.println("\n"
						+ HORIZONTAL_LINE
						+ "\n                                                         "
						+ "\n    Welcome to the Spring Integration XQuery Sample!     "
						+ "\n                                                         "
						+ "\n    For more information please visit:                   "
						+ "\n    http://www.springintegration.org/                    "
						+ "\n                                                         "
						+ "\n"
						+ HORIZONTAL_LINE);

		System.out.println("Which XQuery Processor would you like to use? <enter>: ");
		System.out.println("\t1. Use Saxon");
		System.out.println("\t2. Use Sedna");
		System.out.println("\t3. Use BaseX");

		System.out.println("\tq. Quit the application");
		System.out.print("Enter your choice: ");

		final GenericXmlApplicationContext context = new GenericXmlApplicationContext();

		while (true) {
			final String input = scanner.nextLine();

			if("1".equals(input.trim())) {
				context.getEnvironment().setActiveProfiles("saxon");
				System.out.println("\nUsing Saxon...");
				break;
			} else if("2".equals(input.trim())) {
				context.getEnvironment().setActiveProfiles("sedna");
				System.out.println("\nUsing Sedna...");
				break;
			} else if("3".equals(input.trim())) {
				context.getEnvironment().setActiveProfiles("basex");
				System.out.println("\nUsing BaseX...");

				if(ClassUtils.isPresent("net.xqj.sedna.SednaXQDataSource", ClassUtils.getDefaultClassLoader())) {
					LOGGER.error("Detected the Sedna library to be present. This "
						+ "conflicts with BaseX. Please start the application "
						+ "from the command line using:\n\n"
						+ "mvn exec:java -Dbasex\n\nExiting...");
					System.exit(1);
				}

				break;
			} else if("q".equals(input.trim())) {
				System.out.println("Exiting application...bye.");
				System.exit(0);
			} else {
				System.out.println("Invalid choice\n\n");
				System.out.print("Enter you choice: ");
			}
		}

		context.load("classpath:META-INF/spring/integration/*-context.xml");
		context.registerShutdownHook();
		context.refresh();

		final CustomerService service = context.getBean(CustomerService.class);

		final Resource resource = context.getResource("classpath:data/customers.xml");
		final InputStream is = resource.getInputStream();
		final String customers = new String(FileCopyUtils.copyToByteArray(is));

		System.out.println("\n\nExtracting Customer Names from:\n"
		                 + HORIZONTAL_LINE
		                 + customers + "\n"
		                 + HORIZONTAL_LINE
		                 + "\n");
		final String customernames = service.getCustomerNames(customers);
		System.out.println("Extracted Customer Names: \n"
		                 + HORIZONTAL_LINE
		                 + customernames + "\n"
		                 + HORIZONTAL_LINE
		                 + "\n");

	}

}