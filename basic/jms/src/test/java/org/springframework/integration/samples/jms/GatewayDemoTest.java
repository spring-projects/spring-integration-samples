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
package org.springframework.integration.samples.jms;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

/**
 * @author Gunnar Hillert
 */
public class GatewayDemoTest {

	private final static String[] configFilesGatewayDemo = {
		"/META-INF/spring/integration/common.xml",
		"/META-INF/spring/integration/inboundGateway.xml",
		"/META-INF/spring/integration/outboundGateway.xml"
	};

	private final ByteArrayOutputStream out = new ByteArrayOutputStream();

	@Before
	public void setupStreams() {
		System.setOut(new PrintStream(this.out));
	}

	private void resetStreams() {
		this.out.reset();
	}

	@Test
	public void testGatewayDemo() throws InterruptedException {

		final GenericXmlApplicationContext applicationContext = new GenericXmlApplicationContext(configFilesGatewayDemo);

		final MessageChannel stdinToJmsoutChannel = applicationContext.getBean("stdinToJmsoutChannel", MessageChannel.class);

		resetStreams();
		stdinToJmsoutChannel.send(MessageBuilder.withPayload("jms test").build());

		Thread.sleep(1000);
		Assert.assertEquals("JMS response: JMS TEST\n", out.toString());

		applicationContext.close();
	}

}
