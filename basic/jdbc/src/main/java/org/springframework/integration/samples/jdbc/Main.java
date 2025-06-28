/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 */
package org.springframework.integration.samples.jdbc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.jdbc.domain.Gender;
import org.springframework.integration.samples.jdbc.domain.Person;
import org.springframework.integration.samples.jdbc.service.PersonService;


/**
 * Starts the Spring Context and will initialize the Spring Integration routes.
 *
 * @author Gunnar Hillert
 * @author Amol Nayak
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


		final PersonService personService = context.getBean(PersonService.class);

		LOGGER.info("""
				
				=========================================================
				                                                         
				    Please press 'q + Enter' to quit the application.    
				                                                         
				=========================================================""");

		LOGGER.info("""
				Please enter a choice and press <enter>: 
				\t1. Find person details
				\t2. Create a new person detail
				\tq. Quit the application
				Enter you choice: """);
		while (true) {
			final String input = scanner.nextLine();
			if("1".equals(input.trim())) {
				getPersonDetails(scanner, personService);
			} else if("2".equals(input.trim())) {
				createPersonDetails(scanner,personService);
			} else if("q".equals(input.trim())) {
				break;
			} else {
				LOGGER.info("Invalid choice\n\n");
			}

			LOGGER.info("""
				Please enter a choice and press <enter>: 
				\t1. Find person details
				\t2. Create a new person detail
				\tq. Quit the application
				Enter you choice: """);
		}

		LOGGER.info("Exiting application...bye.");

		context.close();

	}

	private static void createPersonDetails(final Scanner scanner,PersonService service) {
		while(true) {
			LOGGER.info("\nEnter the Person's name:");
			String name = scanner.nextLine();
			Gender gender;
			while(true) {
				LOGGER.info("Enter the Person's gender(M/F):");
				String genderStr = scanner.nextLine();
				if("m".equalsIgnoreCase(genderStr) || "f".equalsIgnoreCase(genderStr)) {
					gender = Gender.getGenderByIdentifier(genderStr.toUpperCase());
					break;
				}
			}
			Date dateOfBirth;
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			while(true) {
				LOGGER.info("Enter the Person's Date of birth in DD/MM/YYYY format:");
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
			LOGGER.info("Created person record with id: " + person.getPersonId());
			LOGGER.info("Do you want to create another person? (y/n)");
			String choice  = scanner.nextLine();
			if(!"y".equalsIgnoreCase(choice)) {
				break;
			}
		}
	}
	/**
	 * @param service
	 * @param input
	 */
	private static void getPersonDetails(final Scanner scanner,final PersonService service) {
		while(true) {
			LOGGER.info("Please enter the name of the person and press<enter>: ");
			String input = scanner.nextLine();
			final List<Person> personList = service.findPersonByName(input);
			if(personList != null && !personList.isEmpty()) {
				for(Person person:personList) {
					LOGGER.info(
							String.format("Person found - Person Id: '%d', Person Name is: '%s',  Gender: '%s'",
										  person.getPersonId(),person.getName(), person.getGender()));
					LOGGER.info(String.format(", Date of birth: '%1$td/%1$tm/%1$tC%1$ty'", person.getDateOfBirth()));
				}
			} else {
				LOGGER.info(
						String.format("No Person record found for name: '%s'.", input));
			}
			LOGGER.info("Do you want to find another person? (y/n)");
			String choice  = scanner.nextLine();
			if(!"y".equalsIgnoreCase(choice)) {
				break;
			}
		}

	}
}
