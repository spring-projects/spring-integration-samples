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

package org.springframework.integration.samples.oddeven;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple POJO providing a method that returns incrementing integer values.
 * This is referenced in the sample's configuration by an inbound Channel
 * Adapter acting as a Polling Consumer.
 * <p/>
 * Every 5th number will be returned as a negative value.
 *
 * @author Mark Fisher
 */
public class Counter {

	private final AtomicInteger count = new AtomicInteger();

	public int next() {
		int nextNumber = count.incrementAndGet();
		return (nextNumber % 5 == 0) ? -nextNumber : nextNumber;
	}

}
