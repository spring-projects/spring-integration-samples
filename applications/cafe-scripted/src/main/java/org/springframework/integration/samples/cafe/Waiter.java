/*
 * Copyright 2002-2010 the original author or authors.
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

package org.springframework.integration.samples.cafe;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * A managed resource implementation to expose the total deliveries via the Control Bus
 * 
 * @author Marius Bogoevici
 * @author David Turanski
 */
@ManagedResource
public class Waiter {

	private final AtomicInteger totalDeliveries = new AtomicInteger();

	public Delivery prepareDelivery(List<Drink> drinks) {
		totalDeliveries.getAndIncrement();
		return new Delivery(drinks);
	}

	@ManagedOperation
	public String getTotalDeliveries() {
		return this.totalDeliveries.toString();
	}

}
