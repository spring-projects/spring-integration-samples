/*
 * Copyright 2002-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.integration.samples.jdbc;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.jdbc.domain.Gender;
import org.springframework.integration.samples.jdbc.domain.Person;
import org.springframework.integration.samples.jdbc.service.PersonService;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The test class for jdbc outbound gateway
 * @author Amol Nayak
 * @author Gary Russell
 *
 */
public class OutboundGatewayTest {

	private final Log logger = LogFactory.getLog(OutboundGatewayTest.class);

	@Test
	public void insertPersonRecord() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"/META-INF/spring/integration/spring-integration-context.xml");
		PersonService service = context.getBean(PersonService.class);
		logger.info("Creating person Instance");
		Person person = new Person();
		Calendar dateOfBirth = Calendar.getInstance();
		dateOfBirth.set(1980, Calendar.JANUARY, 1);
		person.setDateOfBirth(dateOfBirth.getTime());
		person.setName("Name Of The Person");
		person.setGender(Gender.MALE);
		person = service.createPerson(person);
		assertThat(person).isNotNull();
		logger.info("\n\tGenerated person with id: " + person.getPersonId() + ", with name: " + person.getName());
		context.close();
	}

}
