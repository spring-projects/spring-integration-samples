/*
 * Copyright 2002-2018 the original author or authors.
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
package org.springintegration;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This class demonstrates the use a Spring Integration jmx adapters to receive
 * notifications, poll attributes, and invoke operations on a Remote JMX MBeanServer.
 *
 * @author Gary Russell
 * @since 2.2
 *
 */
public class NotificationListener {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx =
				new ClassPathXmlApplicationContext("classpath:/META-INF/spring/integration/remote-monitor-context.xml");
		Gateway gw = ctx.getBean(Gateway.class);
		int cmd = 0;
		while (cmd != 'q') {
			cmd = System.in.read();
			gw.send((char) cmd);
		}
		ctx.close();
	}

	public static interface Gateway {
		void send(char command);
	}
}
