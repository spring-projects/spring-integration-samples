/*
 * Copyright 2002-2011 the original author or authors.
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
package org.springframework.integration.model;

/**
 *
 * @author Gunnar Hillert
 * @since 2.1
 *
 */
public class CoffeeBeverage {

	private Integer id;
	private String name;
	private String description;

	/** Default Constructor */
	public CoffeeBeverage() {
		super();
	}

	/**
	 * @param id
	 * @param name
	 * @param description
	 */
	public CoffeeBeverage(Integer id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.description == null) ? 0 : this.description.hashCode());
		result = prime * result
				+ ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		CoffeeBeverage other = (CoffeeBeverage) obj;

		if (this.description == null) {

			if (other.description != null) {
				return false;
			}

		} else if (!this.description.equals(other.description)) {
			return false;
		}

		if (this.name == null) {

			if (other.name != null) {
				return false;
			}

		} else if (!this.name.equals(other.name)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CoffeeBeverage [id=").append(this.id).append(", name=")
				.append(this.name).append(", description=")
				.append(this.description).append("]");
		return builder.toString();
	}

}
