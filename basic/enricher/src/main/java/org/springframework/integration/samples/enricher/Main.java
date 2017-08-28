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
package org.springframework.integration.samples.enricher;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.enricher.domain.User;
import org.springframework.integration.samples.enricher.service.UserService;


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

	private static final String LINE_SEPARATOR = "\n==========================================================================";

	private static final String EMPTY_LINE     = "\n                                                                          ";

	private Main() { }

	/**
	 * Load the Spring Integration Application Context
	 *
	 * @param args - command line arguments
	 */
	public static void main(final String... args) {

		LOGGER.info(LINE_SEPARATOR
				  + EMPTY_LINE
				  + "\n          Welcome to Spring Integration!                 "
				  + EMPTY_LINE
				  + "\n    For more information please visit:                   "
				  + "\n    http://www.springsource.org/spring-integration       "
				  + EMPTY_LINE
				  + LINE_SEPARATOR );

		final AbstractApplicationContext context =
				new ClassPathXmlApplicationContext("classpath:META-INF/spring/integration/*-context.xml");

		context.registerShutdownHook();

		final Scanner scanner = new Scanner(System.in);

		final UserService service = context.getBean(UserService.class);

		LOGGER.info(LINE_SEPARATOR
				  + EMPTY_LINE
				  + "\n    Please press 'q + Enter' to quit the application.                     "
				  + EMPTY_LINE
				  + LINE_SEPARATOR
				  + EMPTY_LINE
				  + "\n    This example illustrates the usage of the Content Enricher.           "
				  + EMPTY_LINE
				  + "\n    Usage: Please enter 1 or 2 or 3 + Enter                               "
				  + EMPTY_LINE
				  + "\n    3 different message flows are triggered. For sample 1+2 a             "
				  + "\n    user object containing only the username is passed in.                "
				  + "\n    For sample 3 a Map with the 'username' key is passed in and enriched  "
				  + "\n    with the user object using the 'user' key.                            "
				  + EMPTY_LINE
				  + "\n    1: In the Enricher, pass the full User object to the request channel. "
				  + "\n    2: In the Enricher, pass only the username to the request channel.    "
				  + "\n    3: In the Enricher, pass only the username to the request channel.    "
				  + EMPTY_LINE
				  + LINE_SEPARATOR);

		while (!scanner.hasNext("q")) {

			final String input = scanner.nextLine();

			User user = new User("foo", null, null);

			if ("1".equals(input)) {

				final User fullUser = service.findUser(user);
				printUserInformation(fullUser);

			} else if ("2".equals(input)) {

				final User fullUser = service.findUserByUsername(user);
				printUserInformation(fullUser);

			} else if ("3".equals(input)) {

				final Map<String, Object> userData = new HashMap<String, Object>();
				userData.put("username", "foo_map");

				final Map<String, Object> enrichedUserData = service.findUserWithUsernameInMap(userData);

				final User fullUser = (User) enrichedUserData.get("user");

				printUserInformation(fullUser);

			} else {
				LOGGER.info("\n\n    Please enter '1', '2', or '3' <enter>:\n\n");
			}

		}

		LOGGER.info("\n\nExiting application...bye.");

		scanner.close();
		context.close();

	}

	private static void printUserInformation(User user) {

		if (user != null) {
			LOGGER.info(String.format("\n\n    User found - Username: '%s',  Email: '%s', Password: '%s'.\n\n",
					user.getUsername(), user.getEmail(), user.getPassword()));

		} else {
			LOGGER.info("\n\n    No User found for username: 'foo'.\n\n");
		}

	}

}
