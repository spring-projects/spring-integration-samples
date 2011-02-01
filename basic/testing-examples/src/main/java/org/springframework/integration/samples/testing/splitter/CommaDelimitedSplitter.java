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
package org.springframework.integration.samples.testing.splitter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.integration.annotation.Splitter;

/**
 * Splits a message containing a comma-delimited list into 
 * a list of strings. Empty elements are dropped.
 * 
 * @author Gary Russell
 * @since 2.0.2
 *
 */
public class CommaDelimitedSplitter {
	
	@Splitter
	public List<String> split(String input) {
		String[] splits = input.split(",");
		List<String> list = new ArrayList<String>();
		for (String split : splits) {
			String trimmed = split.trim();
			if (trimmed.length() > 0) {
				list.add(trimmed);
			}
		}
		return list;
	}

}
