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
package org.springframework.integration.samples.testing.router;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.integration.test.matcher.PayloadMatcher.hasPayload;

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
 * Shows how to test a router; the router is configured to route to direct
 * channel names. The configuration would
 * be a fragment of a larger flow. Since the output channels are direct,
 * they have no subscribers outside the context of a larger flow. So, 
 * in this test case, we bridge them to {@link QueueChannel}s to
 * facilitate easy testing.
 * 
 * @author Gary Russell
 * @since 2.0.2
 */
@ContextConfiguration	// default context name is <ClassName>-context.xml
@RunWith(SpringJUnit4ClassRunner.class)
public class PetRouterTests {
	
	@Autowired
	MessageChannel inputChannel;
	
	@Autowired
	QueueChannel testFelineChannel;
	
	@Autowired
	QueueChannel testCanineChannel;
	
	@Autowired
	QueueChannel testUnknownPetTypeChannel;
	
	@Test
	public void unitTestClassCat() {
		String payload = "CAT:Fluffy";
		assertEquals("felineChannel", new PetRouter().route(payload));
	}
	
	@Test
	public void unitTestClassDog() {
		String payload = "DOG:Fido";
		assertEquals("canineChannel", new PetRouter().route(payload));
	}
	
	@Test
	public void unitTestClassLizard() {
		String payload = "LIZARD:Scaly";
		assertEquals("unknownPetTypeChannel", new PetRouter().route(payload));
	}
	
	@Test
	public void testCat() {
		String payload = "CAT:Fluffy";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel.send(message);
		Message<?> outMessage = testFelineChannel.receive(0);
		assertNotNull("Expected an output message", outMessage);
		assertThat(outMessage, hasPayload(payload));
	}

	@Test
	public void testDog() {
		String payload = "DOG:Fido";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel.send(message);
		Message<?> outMessage = testCanineChannel.receive(0);
		assertNotNull("Expected an output message", outMessage);
		assertThat(outMessage, hasPayload(payload));
	}

	@Test
	public void testLizard() {
		String payload = "LIZARD:Scaly";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel.send(message);
		Message<?> outMessage = testUnknownPetTypeChannel.receive(0);
		assertNotNull("Expected an output message", outMessage);
		assertThat(outMessage, hasPayload(payload));
	}
}
