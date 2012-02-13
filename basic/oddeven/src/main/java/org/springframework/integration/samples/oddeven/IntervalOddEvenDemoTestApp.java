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
package org.springframework.integration.samples.oddeven;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates a method-invoking Inbound Channel Adapter acting as a Polling
 * Consumer with an interval-based trigger. That adapter is followed,
 * downstream, by a Content Based Router. The router sends to one of two channels,
 * based on whether the payload number is odd or even. Each of those two channels
 * has an Event Driven Consumer ready to log the number and the current time.
 * <p>
 * See the 'intervalOddEvenDemo.xml' configuration file for more detail.
 *
 * @author Mark Fisher
 */
public class IntervalOddEvenDemoTestApp {

	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("/META-INF/spring/integration/intervalOddEvenDemo.xml", IntervalOddEvenDemoTestApp.class);
	}

}
