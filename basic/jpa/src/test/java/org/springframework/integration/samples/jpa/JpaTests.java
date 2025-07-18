/*
 * Copyright 2002-2012 the original author or authors.
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

package org.springframework.integration.samples.jpa;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.samples.jpa.domain.Person;
import org.springframework.integration.samples.jpa.service.PersonService;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Amol Nayak
 * @author Artem Bilan
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class JpaTests {

	@Autowired
	private PersonService personService;

	@Test
	public void insertPersonRecord() {
		Person person = new Person();
		person.setCreatedDateTime(LocalDate.of(1980, 1, 1));
		person.setName("Name Of The Person");

		this.personService.createPerson(person);
		List<Person> people = this.personService.findPeople();
		assertThat(people).hasSize(2);
		assertThat(people.get(1).getName()).isEqualTo(person.getName());
	}

}
