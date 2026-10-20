/*
 * Copyright 2015-present the original author or authors.
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

package org.springframework.integration.samples.barrier;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.rabbitmq.RabbitMQContainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Gary Russell
 * @author Artem Bilan
 *
 * @since 4.2
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Application.class)
@Testcontainers(disabledWithoutDocker = true)
public class ApplicationTests {

	@Container
	static final RabbitMQContainer rabbit = new RabbitMQContainer("rabbitmq:4.1.0-alpine");

	@DynamicPropertySource
	static void rabbitProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.rabbitmq.host", rabbit::getHost);
		registry.add("spring.rabbitmq.port", rabbit::getAmqpPort);
		registry.add("spring.rabbitmq.username", rabbit::getAdminUsername);
		registry.add("spring.rabbitmq.password", rabbit::getAdminPassword);
	}

	@Autowired
	MessageChannel receiveChannel;

	@Autowired
	AbstractMessageChannel release;

	@Test
	public void contextLoads() {
		QueueChannel replies = new QueueChannel();
		receiveChannel.send(
				new GenericMessage<>("A,B,C", Collections.singletonMap(MessageHeaders.REPLY_CHANNEL, replies)));
		assertThat(replies.receive(10_000)).isNotNull();
	}

}
