/*
 * Copyright 2002-2017 the original author or authors.
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
package org.springframework.integration.samples.helloworld;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PollerApp {

	/**
	 * Simple application that polls the current system time 2 times every
	 * 20 seconds (20000 milliseconds).
	 *
	 * The resulting message contains the time in milliseconds and the message
	 * is routed to a Logging Channel Adapter which will print the time to the
	 * command prompt.
	 *
	 * @param args Not used.
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception{
		new ClassPathXmlApplicationContext("META-INF/spring/integration/delay.xml");
	}

}
