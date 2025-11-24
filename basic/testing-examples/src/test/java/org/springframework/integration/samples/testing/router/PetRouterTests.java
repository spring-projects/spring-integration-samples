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

package org.springframework.integration.samples.testing.router;

import org.assertj.core.api.HamcrestCondition;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.test.matcher.PayloadMatcher;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

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
 * @author Artem Bilan
 *
 * @since 2.0.2
 */
@SpringJUnitConfig
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
		assertThat(new PetRouter().route(payload)).isEqualTo("felineChannel");
	}

	@Test
	public void unitTestClassDog() {
		String payload = "DOG:Fido";
		assertThat(new PetRouter().route(payload)).isEqualTo("canineChannel");
	}

	@Test
	public void unitTestClassLizard() {
		String payload = "LIZARD:Scaly";
		assertThat(new PetRouter().route(payload)).isEqualTo("unknownPetTypeChannel");
	}

	@Test
	public void testCat() {
		String payload = "CAT:Fluffy";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel.send(message);
		Message<?> outMessage = testFelineChannel.receive(0);
		assertThat(outMessage).is(new HamcrestCondition<>(PayloadMatcher.hasPayload(payload)));
	}

	@Test
	public void testDog() {
		String payload = "DOG:Fido";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel.send(message);
		Message<?> outMessage = testCanineChannel.receive(0);
		assertThat(outMessage).is(new HamcrestCondition<>(PayloadMatcher.hasPayload(payload)));
	}

	@Test
	public void testLizard() {
		String payload = "LIZARD:Scaly";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel.send(message);
		Message<?> outMessage = testUnknownPetTypeChannel.receive(0);
		assertThat(outMessage).is(new HamcrestCondition<>(PayloadMatcher.hasPayload(payload)));
	}

}
