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
package org.springframework.integration.samples.jdbc;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.jdbc.service.UserService;


/**
 * Starts the Spring Context and will initialize the Spring Integration routes.
 *
 * @author Gunnar Hillert
 * @version 1.0
 *
 */
public final class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

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

        final Scanner scanner = new Scanner(System.in);

        final UserService service = context.getBean(UserService.class);

        LOGGER.info("\n========================================================="
                  + "\n                                                         "
                  + "\n    Please press 'q + Enter' to quit the application.    "
                  + "\n                                                         "
                  + "\n=========================================================" );

        System.out.print("Please enter a string and press <enter>: ");
        while (!scanner.hasNext("q")) {

        	final String input = scanner.nextLine();
        	final User user = service.findUser(input);

        	if (user != null) {

	        	System.out.println(
	        			String.format("User found - Username: '%s',  Email: '%s', Password: '%s'",
	        			              user.getUsername(), user.getEmail(), user.getPassword()));

        	} else {
	        	System.out.println(
	        			String.format("No User found for username: '%s'.", input));
        	}

            System.out.print("Please enter a string and press <enter>:");

        }

        LOGGER.info("Exiting application...bye.");

        System.exit(0);

    }
}
