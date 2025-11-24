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
package org.springframework.integration.samples.testing.filter;

import org.assertj.core.api.HamcrestCondition;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.integration.test.matcher.PayloadMatcher.hasPayload;

/**
 * Shows how to test a filter.
 * The filter has direct input and output channels. The filter configuration would
 * be a fragment of a larger flow. Since the output channel is direct,
 * it has no subscribers outside the context of a larger flow. So,
 * in this test case, we bridge it to a {@link QueueChannel} to
 * facilitate easy testing.
 * <p>
 * Similarly, we bridge the discard channel which is configured on the second
 * filter instance.
 *
 * @author Gary Russell
 * @author Artem Bilan
 *
 * @since 2.0.2
 */
@SpringJUnitConfig
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
		assertThat(new PetFilter().dogsOnly(payload)).isFalse();
	}

	@Test
	public void unitTestClassDog() {
		String payload = "DOG:Fido";
		assertThat(new PetFilter().dogsOnly(payload)).isTrue();
	}

	@Test
	public void unitTestClassLizard() {
		String payload = "LIZARD:Scaly";
		assertThat(new PetFilter().dogsOnly(payload)).isFalse();
	}

	@Test
	public void testCat() {
		String payload = "CAT:Fluffy";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel.send(message);
		Message<?> outMessage = testChannel.receive(0);
		assertThat(outMessage).isNull();
	}

	@Test
	public void testDog() {
		String payload = "DOG:Fido";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel.send(message);
		Message<?> outMessage = testChannel.receive(0);
		assertThat(outMessage).is(new HamcrestCondition<>(hasPayload(payload)));
	}

	@Test
	public void testLizard() {
		String payload = "LIZARD:Scaly";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel.send(message);
		Message<?> outMessage = testChannel.receive(0);
		assertThat(outMessage).isNull();
	}

	@Test
	public void testCatDiscard() {
		String payload = "CAT:Fluffy";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel2.send(message);
		Message<?> outMessage = testChannel2.receive(0);
		assertThat(outMessage).isNull();
		outMessage = testDiscardChannel2.receive(0);
		assertThat(outMessage).is(new HamcrestCondition<>(hasPayload(payload)));
	}

	@Test
	public void testDogDiscard() {
		String payload = "DOG:Fido";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel2.send(message);
		Message<?> outMessage = testChannel.receive(0);
		assertThat(outMessage).is(new HamcrestCondition<>(hasPayload(payload)));
		outMessage = testDiscardChannel2.receive(0);
		assertThat(outMessage).isNull();
	}

	@Test
	public void testLizardDiscard() {
		String payload = "LIZARD:Scaly";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel2.send(message);
		Message<?> outMessage = testChannel.receive(0);
		assertThat(outMessage).isNull();
		outMessage = testDiscardChannel2.receive(0);
		assertThat(outMessage).is(new HamcrestCondition<>(hasPayload(payload)));
	}

}
