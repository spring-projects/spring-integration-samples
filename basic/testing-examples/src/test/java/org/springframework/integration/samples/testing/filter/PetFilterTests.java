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
package org.springframework.integration.samples.testing.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
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
 * Shows how to test a filter. 
 * The filter has direct input and output channels. The filter configuration would
 * be a fragment of a larger flow. Since the output channel is direct,
 * it has no subscribers outside the context of a larger flow. So, 
 * in this test case, we bridge it to a {@link QueueChannel} to
 * facilitate easy testing.
 * 
 * Similarly, we bridge the discard channel which is configured on the second
 * filter instance.
 * 
 * @author Gary Russell
 * @since 2.0.2
 */
@ContextConfiguration	// default context name is <ClassName>-context.xml
@RunWith(SpringJUnit4ClassRunner.class)
public class PetFilterTests {
	
	@Autowired
	MessageChannel inputChannel;
	
	@Autowired
	MessageChannel inputChannel2;
	
	@Autowired
	QueueChannel testChannel;

	@Autowired
	QueueChannel testChannel2;

	@Autowired
	QueueChannel testDiscardChannel2;

	@Test
	public void unitTestClassCat() {
		String payload = "CAT:Fluffy";
		assertFalse(new PetFilter().dogsOnly(payload));
	}
	
	@Test
	public void unitTestClassDog() {
		String payload = "DOG:Fido";
		assertTrue(new PetFilter().dogsOnly(payload));
	}
	
	@Test
	public void unitTestClassLizard() {
		String payload = "LIZARD:Scaly";
		assertFalse(new PetFilter().dogsOnly(payload));
	}
	
	@Test
	public void testCat() {
		String payload = "CAT:Fluffy";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel.send(message);
		Message<?> outMessage = testChannel.receive(0);
		assertNull("Expected no output message", outMessage);
	}

	@Test
	public void testDog() {
		String payload = "DOG:Fido";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel.send(message);
		Message<?> outMessage = testChannel.receive(0);
		assertNotNull("Expected an output message", outMessage);
		assertThat(outMessage, hasPayload(payload));
	}

	@Test
	public void testLizard() {
		String payload = "LIZARD:Scaly";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel.send(message);
		Message<?> outMessage = testChannel.receive(0);
		assertNull("Expected no output message", outMessage);
	}
	
	@Test
	public void testCatDiscard() {
		String payload = "CAT:Fluffy";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel2.send(message);
		Message<?> outMessage = testChannel2.receive(0);
		assertNull("Expected no output message", outMessage);
		outMessage = testDiscardChannel2.receive(0);
		assertNotNull("Expected discard message", outMessage);
		assertThat(outMessage, hasPayload(payload));
	}

	@Test
	public void testDogDiscard() {
		String payload = "DOG:Fido";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel2.send(message);
		Message<?> outMessage = testChannel.receive(0);
		assertNotNull("Expected an output message", outMessage);
		assertThat(outMessage, hasPayload(payload));
		outMessage = testDiscardChannel2.receive(0);
		assertNull("Expected no discard message", outMessage);
	}

	@Test
	public void testLizardDiscard() {
		String payload = "LIZARD:Scaly";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel2.send(message);
		Message<?> outMessage = testChannel.receive(0);
		assertNull("Expected no output message", outMessage);
		outMessage = testDiscardChannel2.receive(0);
		assertNotNull("Expected discard message", outMessage);
		assertThat(outMessage, hasPayload(payload));
	}
}
