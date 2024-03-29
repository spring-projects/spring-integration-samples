/*
 * Copyright 2002-2017 the original author or authors.
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

package org.springframework.integration.samples.jms;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;

/**
 * @author Gunnar Hillert
 */
public class ChannelAdapterDemoTest extends ActiveMQMultiContextTests {

	private final static String[] configFilesChannelAdapterDemo = {
		"/META-INF/spring/integration/common.xml",
		"/META-INF/spring/integration/inboundChannelAdapter.xml",
		"/META-INF/spring/integration/outboundChannelAdapter.xml"
	};

	@Test
	public void testChannelAdapterDemo() throws InterruptedException {

		System.setProperty("spring.profiles.active", "testCase");

		final GenericXmlApplicationContext applicationContext = new GenericXmlApplicationContext(configFilesChannelAdapterDemo);

		final MessageChannel stdinToJmsOutChannel = applicationContext.getBean("stdinToJmsOutChannel", MessageChannel.class);

		stdinToJmsOutChannel.send(MessageBuilder.withPayload("jms test").build());

		final QueueChannel queueChannel = applicationContext.getBean("queueChannel", QueueChannel.class);

		@SuppressWarnings("unchecked")
		Message<String> reply = (Message<String>) queueChannel.receive(20000);
		Assertions.assertNotNull(reply);
		String out = reply.getPayload();
		Assertions.assertEquals("jms test", out);

		applicationContext.close();
	}

}
