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

/**
 * @author Marius Bogoevici
 */
public class Delivery {

	private static final String SEPARATOR = "-----------------------";

	private final List<Drink> deliveredDrinks;

	private final int orderNumber;

	public Delivery(List<Drink> deliveredDrinks) {
		assert(deliveredDrinks.size() > 0);
        this.deliveredDrinks = deliveredDrinks;
		this.orderNumber = deliveredDrinks.get(0).getOrderNumber();
    }

    public int getOrderNumber() {
		return orderNumber;
	}

	public List<Drink> getDeliveredDrinks() {
        return deliveredDrinks;
    }

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer(SEPARATOR + "\n");
		buffer.append("Order #" + getOrderNumber() + "\n");
		for (Drink drink : getDeliveredDrinks()) {
			buffer.append(drink);
			buffer.append("\n");
		}
		buffer.append(SEPARATOR + "\n");
		return buffer.toString();
	}

}
