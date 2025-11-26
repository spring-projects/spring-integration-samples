/*
 * Copyright 2002-present the original author or authors.
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

import org.junit.jupiter.api.Test;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Gunnar Hillert
 */
public class GatewayDemoTest extends ActiveMQMultiContextTests {

	private static final String[] configFilesGatewayDemo = {
			"/META-INF/spring/integration/common.xml",
			"/META-INF/spring/integration/inboundGateway.xml",
			"/META-INF/spring/integration/outboundGateway.xml"
	};

	@Test
	public void testGatewayDemo() {

		System.setProperty("spring.profiles.active", "testCase");

		final GenericXmlApplicationContext applicationContext = new GenericXmlApplicationContext(configFilesGatewayDemo);

		final MessageChannel stdinToJmsOutChannel = applicationContext.getBean("stdinToJmsOutChannel", MessageChannel.class);

		stdinToJmsOutChannel.send(MessageBuilder.withPayload("jms test").build());

		final QueueChannel queueChannel = applicationContext.getBean("queueChannel", QueueChannel.class);

		@SuppressWarnings("unchecked")
		Message<String> reply = (Message<String>) queueChannel.receive(20000);
		assertThat(reply).isNotNull();
		String out = reply.getPayload();

		assertThat(out).isEqualTo("JMS response: JMS TEST");

		applicationContext.close();
	}

}
