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

package org.springframework.integration.samples.cafe;

/**
 * @author Mark Fisher
 * @author Marius Bogoevici
 */
public class OrderItem {

    private DrinkType type;

    private int shots = 1;

    private boolean iced = false;

	private final Order order;


	public OrderItem(Order order, DrinkType type, int shots, boolean iced) {
        this.order = order;
		this.type = type;
        this.shots = shots;
        this.iced = iced;
    }


	public Order getOrder() {
		return this.order;
	}

	public boolean isIced() {
        return this.iced;
    }

    public int getShots() {
        return shots;
    }

    public DrinkType getDrinkType() {
        return this.type;
    }

    public String toString() {
        return ((this.iced) ? "iced " : "hot ") + " order:"  + this.order.getNumber() + " " +this.shots + " shot " + this.type;
    }

}
