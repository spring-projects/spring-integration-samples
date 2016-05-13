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

package org.springframework.integration;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.model.CoffeeBeverage;
import org.springframework.integration.service.CoffeeService;

/**
 * @author Gunnar Hillert
 * @since 2.1
 */
public class CoffeeServiceFindAllTest {

    @Test
    public void testFindCoffee() {
        final ApplicationContext context
            = new ClassPathXmlApplicationContext("/META-INF/spring/integration/spring-integration-context.xml",
                                                  CoffeeServiceFindAllTest.class);

        final CoffeeService service = context.getBean(CoffeeService.class);

        List<CoffeeBeverage> coffeeBeverages = service.findAllCoffeeBeverages();

        assertTrue(coffeeBeverages.size() == 4);

    }

}
