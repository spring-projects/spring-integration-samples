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
 * A simple bootstrap main() method for starting a pair of JMS Gateways.
 * Text entered in the console will go through an outbound JMS Gateway from
 * which it is sent to a JMS Destination. An inbound JMS Gateway is listening
 * to that same JMS Destination and will send the text to another Spring
 * Integration channel where it is handled by a service that simply converts
 * the text to upper case. The upper-case result is then sent in a response
 * message over JMS so that the outbound Gateway receives it as the reply to
 * its original request. At that point, the text is logged to the console.
 * <p>
 * See the configuration in the three XML files that are referenced below.
 * 
 * @author Mark Fisher
 */
public class GatewayDemo {

	private final static String[] configFiles = {
		"common.xml", "inboundGateway.xml", "outboundGateway.xml"
	};


	public static void main(String[] args) {
		ActiveMqTestUtils.prepare();
		new ClassPathXmlApplicationContext(configFiles, GatewayDemo.class);
	}

}
