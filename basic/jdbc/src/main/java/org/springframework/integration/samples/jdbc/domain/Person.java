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

package org.springframework.integration.samples.jdbc.domain;

import java.util.Date;

/**
 * A simple POJO representing a Person
 * @author Amol Nayak
 *
 */
public class Person {

	private int personId;

	private String name;

	private Gender gender;

	private Date dateOfBirth;

	/**
	 * Gets the person id.
	 * @return the person id
	 */
	public int getPersonId() {
		return this.personId;
	}

	/**
	 * Sets the person Id.
	 * @param personId the person id
	 */
	public void setPersonId(int personId) {
		this.personId = personId;
	}

	/**
	 * Gets the name of the person.
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the gender of the person.
	 * @return the gender
	 */
	public Gender getGender() {
		return this.gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/**
	 * Gets the date of birth of the person.
	 * @return the date of birth
	 */
	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

}
