/*
 * Copyright 2014-2017 the original author or authors.
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
package org.springframework.integration.samples.amqp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Starts the Spring Context and will initialize the Spring Integration message flow.
 *
 * @author Gary Russell.
 * @since 4.0
 *
 */
public final class SamplePubConfirmsReturns {

	private static final Log LOGGER = LogFactory.getLog(SamplePubConfirmsReturns.class);

	private SamplePubConfirmsReturns() { }

	/**
	 * Load the Spring Integration Application Context
	 *
	 * @param args - command line arguments
	 */
	public static void main(final String... args) {

		LOGGER.info("\n========================================================="
				  + "\n                                                         "
				  + "\n          Welcome to Spring Integration!                 "
				  + "\n                                                         "
				  + "\n    For more information please visit:                   "
				  + "\n    http://www.springsource.org/spring-integration       "
				  + "\n                                                         "
				  + "\n=========================================================" );

		@SuppressWarnings("resource")
		final AbstractApplicationContext context =
				new ClassPathXmlApplicationContext("classpath:META-INF/spring/integration/spring-integration-confirms-context.xml");

		context.registerShutdownHook();

		LOGGER.info("\n========================================================="
				  + "\n                                                          "
				  + "\n    This is the AMQP Sample with confirms/returns -       "
				  + "\n                                                          "
				  + "\n    Please enter some text and press return. The entered  "
				  + "\n    Message will be sent to the configured RabbitMQ Queue,"
				  + "\n    then again immediately retrieved from the Message     "
				  + "\n    Broker and ultimately printed to the command line.    "
				  + "\n    Send 'fail' to demonstrate a return because the       "
				  + "\n    message couldn't be routed to a queue.                "
				  + "\n    Send 'nack' to demonstrate a NACK because the         "
				  + "\n    exchange doesn't exist, causing the channel to be     "
				  + "\n    closed in error by the broker.                        "
				  + "\n                                                          "
				  + "\n=========================================================" );

	}
}
