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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.jdbc.service.PersonService;
import org.springframework.integration.samples.jdbc.service.UserService;


/**
 * Starts the Spring Context and will initialize the Spring Integration routes.
 *
 * @author Gunnar Hillert
 * @author Amol Nayak
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

        final UserService userService = context.getBean(UserService.class);
        final PersonService personService = context.getBean(PersonService.class);

        LOGGER.info("\n========================================================="
                  + "\n                                                         "
                  + "\n    Please press 'q + Enter' to quit the application.    "
                  + "\n                                                         "
                  + "\n=========================================================" );

        System.out.println("Please enter a choice and press <enter>: ");
        System.out.println("\t1. Find user details");
        System.out.println("\t2. Create a new person detail");
        System.out.println("\tq. Quit the application");
        System.out.print("Enter you choice: ");
        while (true) {
        	final String input = scanner.nextLine();
        	if("1".equals(input.trim()))
        		getUserDetails(scanner,userService);
        	else if("2".equals(input.trim()))
        		createPersonDetails(scanner,personService);
        	else if("q".equals(input.trim()))
        		break;
        	else 
        		System.out.println("Invalid choice\n\n");

        	System.out.println("Please enter your choice and press <enter>: ");
            System.out.println("\t1. Find user details");
            System.out.println("\t2. Create a new person detail");
            System.out.println("\tq. Quit the application");
            System.out.print("Enter you choice: ");
        }

        LOGGER.info("Exiting application...bye.");

        System.exit(0);

    }

    private static void createPersonDetails(final Scanner scanner,PersonService service) {
    	while(true) {
    		System.out.print("\nEnter the Person's name:");
    		String name = scanner.nextLine();
    		Gender gender;
    		while(true) {
    			System.out.print("Enter the Person's gender(M/F):");
        		String genderStr = scanner.nextLine();
        		if("m".equalsIgnoreCase(genderStr) || "f".equalsIgnoreCase(genderStr)) {
        			gender = Gender.getGenderByIdentifier(genderStr.toUpperCase());
        			break;
        		}        			
    		}
    		Date dateOfBirth;
    		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    		while(true) {
    			System.out.print("Enter the Person's Date of birth in DD/MM/YYYY format:");
        		String dobStr = scanner.nextLine();
        		try {
					dateOfBirth = format.parse(dobStr);
					break;
				} catch (ParseException e) {
					//Silently suppress and ask to enter details again
				}        		        			
    		}
    		
    		Person person = new Person();
    		person.setDateOfBirth(dateOfBirth);
    		person.setGender(gender);
    		person.setName(name);
    		person = service.createPerson(person);
    		System.out.println("Created person record with id: " + person.getPersonId());
    		System.out.print("Do you want to create another person? (y/n)");
			String choice  = scanner.nextLine();
			if(!"y".equalsIgnoreCase(choice))
				break;
    	}
    }
	/**
	 * @param service
	 * @param input
	 */
	private static void getUserDetails(final Scanner scanner,final UserService service) {
		while(true) {
			System.out.print("Please enter a string and press <enter>: ");
			String input = scanner.nextLine();
			final User user = service.findUser(input);
			if (user != null) {

				System.out.println(
						String.format("User found - Username: '%s',  Email: '%s', Password: '%s'",
						              user.getUsername(), user.getEmail(), user.getPassword()));

			} else {
				System.out.println(
						String.format("No User found for username: '%s'.", input));
			}
			System.out.print("Do you want to find another user? (y/n)");
			String choice  = scanner.nextLine();
			if(!"y".equalsIgnoreCase(choice))
				break;			
		}
		
	}
}
