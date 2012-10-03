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

package org.springframework.integration.samples.cafe.xml;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.integration.samples.cafe.Cafe;
import org.springframework.integration.samples.cafe.DrinkType;
import org.springframework.integration.samples.cafe.Order;

/**
 * Provides the 'main' method for running the Cafe Demo store front
 * application using AMQP. Before running, be sure to have a
 * RabbitMQ broker started on localhost:5672 configured with the default
 * guest | guest client credentials on the / vHost. When an order is
 * placed, the Cafe store front will publish that order on the cafe-orders
 * exchange to be processed.
 * <p/>
 * The relevant components are defined within the configuration files:
 * ("cafeDemo-amqp-xml.xml", "cafeDemo-amqp-config-xml.xml").
 * <p/>
 * If deploying in SpringSource dmServer, the relevant ApplicationContext
 * configuration is in the META-INF/spring directory instead.
 *
 * @author Tom McCuch
 */
public class CafeDemoAppAmqp {

	/**
	 * place some orders
	 * @param context spring context
	 * @param count the number of standard orders
	 */
	public static void order(AbstractApplicationContext context, int count){
		Cafe cafe = (Cafe) context.getBean("cafe");
		for (int i = 1; i <= 100; i++) {
			Order order = new Order(i);
			order.addItem(DrinkType.LATTE, 2, false);
			order.addItem(DrinkType.MOCHA, 3, true);
			cafe.placeOrder(order);
		}
	}

	public static void main(String[] args) {
		AbstractApplicationContext context =
			CafeDemoAppUtilities.loadProfileContext(
					"/META-INF/spring/integration/amqp/cafeDemo-amqp-xml.xml",
					CafeDemoAppAmqp.class,CafeDemoAppUtilities.DEV);
		order(context, 100);
		context.close();
	}
}
