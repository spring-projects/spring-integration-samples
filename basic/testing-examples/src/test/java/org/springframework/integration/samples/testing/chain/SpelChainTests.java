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
package org.springframework.integration.samples.testing.chain;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

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
 * @author Artem Bilan
 *
 * @since 2.0.2
 *
 */
@SpringJUnitConfig
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
		assertThat(outMessage).isNotNull();
		Object myHeader = outMessage.getHeaders().get("myHeader");
		assertThat(myHeader).isEqualTo(Boolean.TRUE);
		assertThat(outMessage.getPayload()).isEqualTo(payload.toLowerCase());
	}

	@Test
	public void testFalseHeader() {
		String payload = "XXXDEFXXX";
		Message<String> message = MessageBuilder.withPayload(payload).build();
		inputChannel.send(message);
		Message<?> outMessage = testChannel.receive(0);
		assertThat(outMessage).isNotNull();
		Object myHeader = outMessage.getHeaders().get("myHeader");
		assertThat(myHeader).isEqualTo(Boolean.FALSE);
		assertThat(outMessage.getPayload()).isEqualTo(payload.toLowerCase());
	}

}
