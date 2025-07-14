/*
 * Copyright 2019 the original author or authors.
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

package org.springframework.integration.samples.mqtt;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.integration.test.mock.MockIntegration.messageArgumentCaptor;
import static org.springframework.integration.test.mock.MockIntegration.mockMessageHandler;

/**
 * @author Gary Russell
 *
 * @since 5.2
 *
 */
@SpringBootTest
@SpringIntegrationTest
public class ApplicationTest implements MosquittoContainerTest {

	@Autowired
	private MockIntegrationContext mockIntegrationContext;

	@Autowired
	private IntegrationFlow mqttOutFlow;

	@DynamicPropertySource
	static void mqttDbProperties(DynamicPropertyRegistry registry) {
		registry.add("mqtt.url", MosquittoContainerTest::mqttUrl);
	}


	@Test
	public void test() throws InterruptedException {
		ArgumentCaptor<Message<?>> captor = messageArgumentCaptor();
		CountDownLatch receiveLatch = new CountDownLatch(1);
		MessageHandler mockMessageHandler = mockMessageHandler(captor).handleNext(m -> receiveLatch.countDown());
		this.mockIntegrationContext
				.substituteMessageHandlerFor(
						"mqttInFlow.org.springframework.integration.config.ConsumerEndpointFactoryBean#1",
						mockMessageHandler);
		this.mqttOutFlow.getInputChannel().send(new GenericMessage<>("foo"));
		assertThat(receiveLatch.await(10, TimeUnit.SECONDS)).isTrue();
		verify(mockMessageHandler).handleMessage(any());
		assertThat(captor.getValue().getPayload())
				.isEqualTo("foo sent to MQTT, received from MQTT");
	}

}
