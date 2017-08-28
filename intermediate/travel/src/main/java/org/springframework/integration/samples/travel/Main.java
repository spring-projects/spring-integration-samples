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
package org.springframework.integration.samples.travel;

import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public final class Main {

	private static final Log LOGGER = LogFactory.getLog(Main.class);

	/**
	 * Prevent instantiation.
	 */
	private Main() {}

	/**
	 * @param args Not used.
	 */
	public static void main(String... args) throws Exception{

		final GenericXmlApplicationContext context = new GenericXmlApplicationContext();

		final ConfigurableEnvironment env = context.getEnvironment();
		boolean mapQuestApiKeyDefined = env.containsProperty("mapquest.apikey");

		if (mapQuestApiKeyDefined) {
			env.setActiveProfiles("mapquest");
		}

		context.load("classpath:META-INF/spring/*.xml");
		context.refresh();

		final TravelGateway travelGateway = context.getBean("travelGateway", TravelGateway.class);

		final Scanner scanner = new Scanner(System.in);

		System.out.println("\n========================================================="
						+ "\n                                                         "
						+ "\n    Welcome to the Spring Integration Travel App!        "
						+ "\n                                                         "
						+ "\n    For more information please visit:                   "
						+ "\n    http://www.springintegration.org/                    "
						+ "\n                                                         "
						+ "\n=========================================================" );

		System.out.println("Please select the city, for which you would like to get traffic and weather information:");

		for (City city : City.values()) {
			System.out.println(String.format("\t%s. %s", city.getId(), city.getName()));
		}
		System.out.println("\tq. Quit the application");
		System.out.print("Enter your choice: ");

		while (true) {
			final String input = scanner.nextLine();

			if("q".equals(input.trim())) {
				System.out.println("Exiting application...bye.");
				break;
			}
			else {

				final Integer cityId = Integer.valueOf(input);
				final City city = City.getCityForId(cityId);

				final String weatherReply = travelGateway.getWeatherByCity(city);

				System.out.println("\n========================================================="
								+ "\n    Weather:"
								+ "\n=========================================================" );
				System.out.println(weatherReply);

				if (mapQuestApiKeyDefined) {
					final String trafficReply = travelGateway.getTrafficByCity(city);

					System.out.println("\n========================================================="
									+ "\n    Traffic:"
									+ "\n=========================================================" );
					System.out.println(trafficReply);
				}
				else {
					LOGGER.warn("Skipping Traffic Information call. Did you setup your MapQuest API Key? " +
							"e.g. by calling:\n\n    $ mvn exec:java -Dmapquest.apikey=\"your_mapquest_api_key_url_decoded\"");
				}

				System.out.println("\n========================================================="
								+ "\n    Done."
								+ "\n=========================================================" );
				System.out.print("Enter your choice: ");
			}
		}

		scanner.close();
		context.close();
	}

}
