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

package org.springframework.integration.samples.cafe;

import java.io.Serializable;

/**
 * @author Marius Bogoevici
 * @author Tom McCuch
 * @author Gunnar Hillert
 */
public class Drink implements Serializable{

	private static final long serialVersionUID = 1L;

	private boolean iced;

	private int shots;

	private DrinkType drinkType;

	private int orderNumber;

	// Default constructor required by Jackson Java JSON-processor
	public Drink() {}

	public Drink(int orderNumber, DrinkType drinkType, boolean iced, int shots) {
		this.orderNumber = orderNumber;
		this.drinkType = drinkType;
		this.iced = iced;
		this.shots = shots;
	}


	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public boolean isIced() {
		return this.iced;
	}

	public void setIced(boolean iced) {
		this.iced = iced;
	}

	public DrinkType getDrinkType() {
		return this.drinkType;
	}

	public void setDrinkType(DrinkType drinkType) {
		this.drinkType = drinkType;
	}

	public int getShots() {
		return this.shots;
	}

	public void setShots(int shots) {
		this.shots = shots;
	}

	@Override
	public String toString() {
		return (iced?"Iced":"Hot")  + " " + drinkType.toString() + ", " + shots + " shots.";
	}

}
