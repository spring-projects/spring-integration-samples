/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.springframework.integration.samples.cafe.demo;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.cafe.WaiterMonitor;

/**
 * Sets up a remote connection to the CafeDemoApp to manage it using the Groovy Control Bus.
 * The WaiterMonitor queries the total delivered orders every second.
 * If totalDeliveries >= 3, stop the cafe inbound adapter.
 *
 * @author David Turanski
 * @author Gary Russell
 *
 */
public class ControlBusMain {
	private static Log logger = LogFactory.getLog(ControlBusMain.class);

	public static void main(String[] args)  {

		AbstractApplicationContext context =
			new ClassPathXmlApplicationContext("/META-INF/spring/integration/cafeDemo-control-bus.xml");

			WaiterMonitor waiterMonitor = (WaiterMonitor) context.getBean("waiterMonitor");

			int totalDeliveries = 0;
			while (totalDeliveries <= 3) {
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {
					logger.error("Interrupted", e);
				}

				totalDeliveries = (Integer)waiterMonitor.sendControlScript("waiter.totalDeliveries");

				logger.info("Total cafe deliveries: " + totalDeliveries);

				if (totalDeliveries > 3) {
					logger.info("stopping orders...");
					waiterMonitor.sendControlScript("cafe.stop()");
					logger.info("orders stopped");
				}
			}
			context.close();
			System.exit(0);
	}

}
