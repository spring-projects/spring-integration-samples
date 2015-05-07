/*
 * Copyright 2002-2015 the original author or authors.
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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

/**
 * @author Gunnar Hillert
 * @author Gary Russell
 */
public class AggregatorDemoTest {

	private final static String[] configFilesGatewayDemo = {
		"/META-INF/spring/integration/common.xml",
		"/META-INF/spring/integration/aggregation.xml"
	};

	@Test
	public void testGatewayDemo() throws InterruptedException {

		System.setProperty("spring.profiles.active", "testCase");

		final GenericXmlApplicationContext applicationContext = new GenericXmlApplicationContext(configFilesGatewayDemo);

		final MessageChannel stdinToJmsoutChannel = applicationContext.getBean("stdinToJmsoutChannel", MessageChannel.class);

		stdinToJmsoutChannel.send(MessageBuilder.withPayload("jms test").build());

		final QueueChannel queueChannel = applicationContext.getBean("queueChannel", QueueChannel.class);

		@SuppressWarnings("unchecked")
		Message<List<String>> reply = (Message<List<String>>) queueChannel.receive(20000);
		Assert.assertNotNull(reply);
		List<String> out = reply.getPayload();

		Assert.assertEquals("[JMS TEST, JMS TEST]", out.toString());

		applicationContext.close();
	}

}
