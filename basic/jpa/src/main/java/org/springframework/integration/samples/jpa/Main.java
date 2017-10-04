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

package org.springframework.integration.samples.jpa;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.samples.jpa.domain.Person;
import org.springframework.integration.samples.jpa.service.PersonService;
import org.springframework.util.StringUtils;

/**
 * Starts the Spring Context and will initialize the Spring Integration routes.
 *
 * @author Gunnar Hillert
 * @author Amol Nayak
 * @author Gary Russell
 * @author Artem Bilan
 * @version 1.0
 *
 */
@SpringBootApplication(exclude = JpaRepositoriesAutoConfiguration.class)
@ImportResource("spring-integration-context.xml")
public class Main {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");

	/**
	 * Load the Spring Integration Application Context
	 *
	 * @param args - command line arguments
	 */
	public static void main(final String... args) {

		final Scanner scanner = new Scanner(System.in);

		System.out.println("\n========================================================="
				+ "\n                                                         "
				+ "\n    Welcome to the Spring Integration JPA Sample!        "
				+ "\n                                                         "
				+ "\n    For more information please visit:                   "
				+ "\n    http://www.springintegration.org/                    "
				+ "\n                                                         "
				+ "\n=========================================================");

		ConfigurableApplicationContext context = new SpringApplicationBuilder(Main.class)
				.web(WebApplicationType.NONE)
				.run(args);

		final PersonService personService = context.getBean(PersonService.class);

		System.out.println("Please enter a choice and press <enter>: ");
		System.out.println("\t1. List all people");
		System.out.println("\t2. Create a new person");
		System.out.println("\tq. Quit the application");
		System.out.print("Enter you choice: ");

		while (true) {
			final String input = scanner.nextLine();

			if ("1".equals(input.trim())) {
				findPeople(personService);
			}
			else if ("2".equals(input.trim())) {
				createPersonDetails(scanner, personService);
			}
			else if ("q".equals(input.trim())) {
				break;
			}
			else {
				System.out.println("Invalid choice\n\n");
			}

			System.out.println("Please enter a choice and press <enter>: ");
			System.out.println("\t1. List all people");
			System.out.println("\t2. Create a new person");
			System.out.println("\tq. Quit the application");
			System.out.print("Enter you choice: ");
		}

		System.out.println("Exiting application...bye.");
		context.close();
		System.exit(0);

	}

	private static void createPersonDetails(final Scanner scanner, PersonService service) {
		while (true) {
			System.out.print("\nEnter the Person's name:");
			String name = null;

			while (true) {

				name = scanner.nextLine();

				if (StringUtils.hasText(name)) {
					break;
				}

				System.out.print("No text entered....Please enter a name:");

			}

			Person person = new Person();
			person.setName(name);
			person = service.createPerson(person);
			System.out.println("Created person record with id: " + person.getId());
			System.out.print("Do you want to create another person? (y/n)");
			String choice = scanner.nextLine();

			if (!"y".equalsIgnoreCase(choice)) {
				break;
			}
		}
	}

	private static void findPeople(final PersonService service) {

		System.out.println("ID            NAME         CREATED");
		System.out.println("==================================");

		final List<Person> people = service.findPeople();

		if (people != null && !people.isEmpty()) {
			for (Person person : people) {
				System.out.print(String.format("%d, %s, ", person.getId(), person.getName()));
				System.out.println(DATE_FORMAT.format(person.getCreatedDateTime()));//NOSONAR
			}
		}
		else {
			System.out.println(
					String.format("No Person record found."));
		}

		System.out.println("==================================\n\n");
	}

}
