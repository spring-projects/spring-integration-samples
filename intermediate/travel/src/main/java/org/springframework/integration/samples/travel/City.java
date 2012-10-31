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
package org.springframework.integration.samples.travel;


/**
 * Enumeration that contains relevant information for various American cities,
 * such as the postal code (ZIP Code) and the Latitude/Longitude information. Using
 * this information, traffic and weather information can be retrieved.
 *
 * @author Gunnar Hillert
 * @since 2.2
 *
 */
public enum City {

	ATLANTA(1, "Atlanta", "30334", "34.026784,-85.010794,33.471015,-83.765405"),
	BOSTON(2, "Boston", "02201", "42.636182,-71.651862,42.080413,-70.467446"),
	SAN_FRANCISCO(3, "San Francisco", "94102", "38.052886,-123.009856,37.497117,-121.82544");

	private Integer id;
	private String name;
	private String boundingBox;
	private String postalCode;

	private City(Integer id, String name, String postalCode, String boundingBox) {
		this.id = id;
		this.name = name;
		this.boundingBox = boundingBox;
		this.postalCode = postalCode;
	}

	public String getBoundingBox() {
		return boundingBox;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static City getCityForId(Integer id) {

		for (City city : City.values()) {
			if (city.id.equals(id)) {
				return city;
			}
		}

		return null;
	}

}
