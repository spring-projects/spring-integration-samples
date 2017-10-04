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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.samples.jpa.domain.Person;
import org.springframework.integration.samples.jpa.service.PersonService;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Amol Nayak
 * @author Artem Bilan
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class JpaTests {

	@Autowired
	private PersonService personService;

	@Test
	public void insertPersonRecord() {
		Person person = new Person();
		Calendar createdDateTime = Calendar.getInstance();
		createdDateTime.set(1980, Calendar.JANUARY, 1);
		person.setCreatedDateTime(createdDateTime.getTime());
		person.setName("Name Of The Person");

		this.personService.createPerson(person);
		List<Person> people = this.personService.findPeople();
		assertNotNull(people);
		assertEquals(2, people.size());
		assertEquals(person.getName(), people.get(1).getName());
	}

}
