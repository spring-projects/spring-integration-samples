/*
 * Copyright 2002-2011 the original author or authors.
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
package org.springframework.integration.samples.testing.errorhandling;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.samples.testing.gateway.VoidGateway;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * Shows how to test a gateway to ensure the message injected
 * into the Spring Integration flow is what we expected.
 * The gateway uses a direct input channel. The configuration would
 * be a fragment of a larger flow. Since the input channel is direct,
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
public class GatewayTests {

	@Autowired
	QueueChannel testChannel;

	@Autowired
	VoidGateway gateway;

	@Test
	public void testTrueHeader() {
		String payload = "XXXABCXXX";
		String fileName = "abc.txt";
		gateway.process(payload, fileName);
		Message<?> errorMessage = testChannel.receive(0);
		assertThat(errorMessage)
				.extracting("payload.failedMessage.payload")
				.isEqualTo(payload);
		assertThat(errorMessage)
				.extracting("payload.cause")
				.isInstanceOf(MessageHandlingException.class);
	}

}
