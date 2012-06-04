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
package org.springframework.integration.samples.jpa;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.jpa.service.PersonService;

/**
 *
 * @author Amol Nayak
 *
 */
public class OutboundGatewayTest {

	private static final Logger LOGGER = Logger.getLogger(OutboundGatewayTest.class);

	@Test
	public void insertPersonRecord() {

		final ApplicationContext context = new ClassPathXmlApplicationContext(
			"classpath:/META-INF/spring/integration/spring-integration-context.xml");

		final PersonService service = context.getBean(PersonService.class);

		LOGGER.info("Creating person Instance");

		final Person person = new Person();
		Calendar createdDateTime = Calendar.getInstance();
		createdDateTime.set(1980, 0, 1);
		person.setCreatedDateTime(createdDateTime.getTime());
		person.setName("Name Of The Person");

		final Person persistedPerson = service.createPerson(person);
		Assert.assertNotNull("Expected a non null instance of Person, got null", persistedPerson);
		LOGGER.info("\n\tGenerated person with id: " + persistedPerson.getId() + ", with name: " + persistedPerson.getName());
	}

}
