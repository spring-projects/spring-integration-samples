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

import java.io.IOException;

import org.springframework.context.support.AbstractApplicationContext;

/**
 * Provides the 'main' method for running the Cafe Demo Operations
 * application using AMQP. Before running, be sure to have a RabbitMQ
 * broker started on localhost:5672 configured with the default
 * guest | guest client credentials on the / vHost. When an order is
 * placed on the new-orders queue, the Cafe Operations app will split
 * the order into order line items, route them to either the coldDrink
 * or hotDrink Barista, aggregate the prepared drinks returned from the
 * Barista using a Waiter, and publish the delivered order on the
 * cafe-deliveries exchange.
 * <p/>
 * The relevant components are defined within the configuration files:
 * ("cafeDemo-amqp-operations-xml.xml", "cafeDemo-amqp-config-xml.xml").
 * <p/>
 * Before starting this app, be sure to start the CafeDemoAppBaristaCold
 * and CafeDemoAppBaristaHot apps first.
 *
 * If deploying in SpringSource dmServer, the relevant ApplicationContext
 * configuration is in the META-INF/spring directory instead.
 *
 * @author Tom McCuch
 */
public class CafeDemoAppOperationsAmqp {

	public static void main(String[] args) {
		AbstractApplicationContext context =
				CafeDemoAppUtilities.loadProfileContext(
					"/META-INF/spring/integration/amqp/cafeDemo-amqp-operations-xml.xml",
					CafeDemoAppOperationsAmqp.class,CafeDemoAppUtilities.DEV);
		System.out.println("Press Enter/Return in the console to exit the Cafe Operations App");

		try {
			System.in.read();
		}
		catch (IOException e) {
			context.close();
		}
		context.close();
	}
}
