/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.integration.samples.jdbc.service;

import java.util.List;

import org.springframework.integration.samples.jdbc.domain.Person;

/**
 * The Service used to create Person instance in database
 * @author Amol Nayak
 *
 */
public interface PersonService {

	/**
	 * Creates a {@link Person} instance from the {@link Person} instance passed
	 *
	 * @param person created person instance, it will contain the generated primary key and the formatted name
	 * @return the retrieved {@link Person}
	 */
	Person createPerson(Person person);

	/**
	 * Find the person by the person name, the name search is case insensitive, however the
	 * spaces are not ignored
	 *
	 * @param name
	 * @return the matching {@link Person} record
	 */
	List<Person> findPersonByName(String name);

}
