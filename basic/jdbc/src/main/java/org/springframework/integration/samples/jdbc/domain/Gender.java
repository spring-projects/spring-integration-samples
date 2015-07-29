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
package org.springframework.integration.samples.jdbc.domain;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the gender of the person
 * @author Amol Nayak
 *
 */
public enum Gender {

	MALE("M"),FEMALE("F");
	private static Map<String, Gender> map;
	
	private String identifier;

	private Gender(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}	
	
	public static Gender getGenderByIdentifier(String identifier) {
		return map.get(identifier);
	}
	
	static {
		map = new HashMap<String, Gender>();
		for(Gender gender:EnumSet.allOf(Gender.class)) {
			map.put(gender.getIdentifier(), gender);
		}
	}
}
