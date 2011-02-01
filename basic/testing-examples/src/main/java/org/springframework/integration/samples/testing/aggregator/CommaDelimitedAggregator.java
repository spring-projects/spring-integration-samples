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
package org.springframework.integration.samples.testing.aggregator;

import java.util.List;

import org.springframework.integration.annotation.Aggregator;

/**
 * 
 * Aggregates messages into a comma-delimited list.
 * 
 * @author Gary Russell
 * @since 2.0.2
 *
 */
public class CommaDelimitedAggregator {

	@Aggregator
	public String aggregate(List<String> bits) {
		StringBuilder sb = new StringBuilder();
		for (String bit : bits) {
			sb.append(bit).append(",");
		}
		// remove final comma, if any
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
		}
		if (sb.length() < 1) {
			return null;
		}
		return sb.toString();
	}
}
