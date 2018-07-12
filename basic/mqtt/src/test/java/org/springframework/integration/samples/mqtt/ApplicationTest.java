/*
 * Copyright 2018 the original author or authors.
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

package org.springframework.integration.samples.mqtt;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Gary Russell
 * @since 5.0
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

	@ClassRule
	public static final BrokerRunning brokerRunning = BrokerRunning.isRunning(1883);

	@Autowired
	private IntegrationFlow mqttOutFlow;

	@Autowired
	@Qualifier("mqttInFlow.channel#1")
	private AbstractMessageChannel inbound;

	@Test
	public void test() throws Exception {
		final AtomicReference<Object> payload = new AtomicReference<>();
		final CountDownLatch latch = new CountDownLatch(1);
		this.inbound.addInterceptor(new ChannelInterceptor() {

			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				payload.set(message.getPayload());
				latch.countDown();
				return message;
			}

		});
		this.mqttOutFlow.getInputChannel().send(new GenericMessage<>("foo"));
		assertThat(latch.await(10, TimeUnit.SECONDS)).isTrue();
		assertThat(payload.get()).isEqualTo("foo sent to MQTT, received from MQTT");
	}

}
