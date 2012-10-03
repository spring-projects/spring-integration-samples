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
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.cafe.Cafe;
import org.springframework.integration.samples.cafe.DrinkType;
import org.springframework.integration.samples.cafe.Order;

import java.io.IOException;

/**
 * Main class for running the Cafe sample with JMS-backed (ActiveMQ) channels. Once the application
 * is running, simply press <return> or any other key to end the application. To fully experience the
 * benefits of this solution, try halting/exiting the program in the middle of running it, comment out the
 * call to place new orders (the order() function call) and watch that the processing still continues
 * where it left off when you halted it. This is because the messages are persisted in the ActiveMQ queues.
 *
 * @author Christian Posta
 */
public class CafeDemoActiveMQBackedChannels {

	/**
	 * Place some orders.
	 *
	 * @param context spring context
	 * @param count the number of standard orders
	 */
	public static void order(AbstractApplicationContext context, int count){
		Cafe cafe = (Cafe) context.getBean("cafe");
		for (int i = 1; i <= count; i++) {
			Order order = new Order(i);
			order.addItem(DrinkType.LATTE, 2, false);
			order.addItem(DrinkType.MOCHA, 3, true);
			cafe.placeOrder(order);
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"/META-INF/spring/integration/activemq/cafeDemo-amq-config.xml",
				"/META-INF/spring/integration/activemq/cafeDemo-amq-jms-backed.xml");

		// comment this out to run the sample without placing any new orders on the queue
		order(context, 25);

		System.in.read();
		context.close();
	}
}
