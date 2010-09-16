/*
 * Copyright 2002-2010 the original author or authors.
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

package org.springframework.integration.samples.jms;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A simple bootstrap main() method for starting a pair of JMS Channel
 * Adapters. Text entered in the console will go through an outbound
 * JMS Channel Adapter from which it is sent to a JMS Destination.
 * An inbound JMS Channel Adapter is listening to that same JMS
 * Destination and will echo the result in the console.
 * <p>
 * See the configuration in the three XML files that are referenced below.
 * 
 * @author Mark Fisher
 */
public class ChannelAdapterDemo {

	private final static String[] configFiles = {
		"/META-INF/spring/integration/common.xml", 
		"/META-INF/spring/integration/inboundChannelAdapter.xml", 
		"/META-INF/spring/integration/outboundChannelAdapter.xml"
	};


	public static void main(String[] args) {
		ActiveMqTestUtils.prepare();
		new ClassPathXmlApplicationContext(configFiles, ChannelAdapterDemo.class);
		System.out.println("Please type something and hit return");
	}

}
