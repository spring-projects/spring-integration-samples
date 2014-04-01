/*
 * Copyright 2002-2011 the original author or authors.
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
package org.springframework.integration.samples.testing.chain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * 
 * Shows how to test a chain of endpoints that use SpEL expressions. 
 * The chain has direct input and output channels. The chain would
 * be a fragment of a larger flow. Since the output channel is direct,
 * it has no subscribers outside the context of a larger flow. So, 
 * in this test case, we bridge it to a {@link QueueChannel} to
 * facilitate easy testing.
 * 
 * @author Gary Russell
 * @since 2.0.2
 *
 */
@ContextConfiguration	// default context name is <ClassName>-context.xml
@RunWith(SpringJUnit4ClassRunner.class)
public class SpelChainTests {
	
	@Autowired
	MessageChannel inputChannel;
	
	@Autowired
	QueueChannel testChannel;
	
	@Test
	public void testTrueHeader() {
		String payload = "XXXABCXXX";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel.send(message);
		Message<?> outMessage = testChannel.receive(0);
		assertNotNull("Expected an output message", outMessage);
		Object myHeader = outMessage.getHeaders().get("myHeader");
		assertNotNull("Expecter myHeader header", myHeader);
		assertEquals("Expected myHeader==true", Boolean.TRUE, myHeader);
		assertEquals("Expected lower case message", payload.toLowerCase(), outMessage.getPayload());
	}

	@Test
	public void testFalseHeader() {
		String payload = "XXXDEFXXX";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel.send(message);
		Message<?> outMessage = testChannel.receive(0);
		assertNotNull("Expected an output message", outMessage);
		Object myHeader = outMessage.getHeaders().get("myHeader");
		assertNotNull("Expecter myHeader header", myHeader);
		assertEquals("Expected myHeader==false", Boolean.FALSE, myHeader);
		assertEquals("Expected lower case message", payload.toLowerCase(), outMessage.getPayload());
	}

}
