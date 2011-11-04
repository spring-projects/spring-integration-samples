/*
 * Copyright 2002-2010 the original author or authors.
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

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.enricher.service.UserService;


/**
 * Starts the Spring Context and will initialize the Spring Integration routes.
 *
 * @author Gunnar Hillert
 * @version 1.0
 *
 */
public final class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

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
                  + "\n    Usage: Please enter 1 + Enter or 2 + Enter                            "
                  + EMPTY_LINE
                  + "\n    2 different message flows are triggered. For both flows a             "
                  + "\n    user object containing only the username is passed in.                "
                  + EMPTY_LINE
                  + "\n    However the Enrichment is executed differently:                       "
                  + EMPTY_LINE
                  + "\n    1: In the Enricher, pass the full User object to the request channel. "
                  + "\n    2: In the Enricher, pass only the username to the request channel.    "
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

            } else {
            	LOGGER.info("\n\n    Please enter '1' or '2' <enter>:\n\n");
            }

        }

        LOGGER.info("\n\nExiting application...bye.");

        System.exit(0);

    }

    private static void printUserInformation(User user) {

    	if (user != null) {
    		LOGGER.info("\n\n    User found - Username: '{}',  Email: '{}', Password: '{}'.\n\n",
    				new Object[] {user.getUsername(), user.getEmail(), user.getPassword()});

    	} else {
    		LOGGER.info("\n\n    No User found for username: 'foo'.\n\n");
    	}

    }

}
