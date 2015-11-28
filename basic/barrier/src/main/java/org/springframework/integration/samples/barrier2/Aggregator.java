/*
 * Copyright 2015 the original author or authors.
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
package org.springframework.integration.samples.barrier2;

import java.util.Collection;

import org.springframework.messaging.MessagingException;

/**
 * @author Gary Russell
 * @since 4.2
 *
 */
public class Aggregator {

	public Object aggregate(Collection<Object> results) {
		for (Object o : results) {
			if (o instanceof MessagingException) {
				return new ConsolidatedResultsException(results);
			}
		}
		return results;
	}

	@SuppressWarnings("serial")
	public static class ConsolidatedResultsException extends RuntimeException {

		private final Collection<Object> results;

		public ConsolidatedResultsException(Collection<Object> results) {
			this.results = results;
		}

		public Collection<Object> getResults() {
			return results;
		}

		@Override
		public String toString() {
			return "ConsolidatedResultsException\n[results=\n" + results.toString().replaceAll(", ", "\n") + "\n]";
		}

	}

}
