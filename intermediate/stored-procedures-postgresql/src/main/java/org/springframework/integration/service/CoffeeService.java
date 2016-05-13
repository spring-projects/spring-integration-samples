/*
 * Copyright 2002-2014 the original author or authors
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package org.springframework.integration.service;

import java.util.List;

import org.springframework.integration.model.CoffeeBeverage;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Transactional;


/**
 * Provides access to the Coffee Database Services.
 *
 * @author Gunnar Hillert
 * @since 2.2
 */
@Transactional
public interface CoffeeService {

	/**
	 * Find the description for a provided coffee beverage.
	 *
	 * @param input of the coffee beverage
	 * @return The the description of the coffee beverage
	 */
	String findCoffeeBeverage(Integer input);

	/**
	 * Find the description for a provided coffee beverage.
	 *
	 * @return Collection of coffee beverages
	 */
	@Payload("new java.util.Date()")
	List<CoffeeBeverage> findAllCoffeeBeverages();

}
