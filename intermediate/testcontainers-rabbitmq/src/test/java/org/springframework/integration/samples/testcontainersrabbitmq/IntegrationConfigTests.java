/*
 * Copyright 2002-2020 the original author or authors.
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

package org.springframework.integration.samples.testcontainersrabbitmq;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringIntegrationTest
@Import({ Receiver.class, IntegrationConfigTests.Config.class })
@Disabled
class IntegrationConfigTests {

	@Autowired
	@Qualifier("request.input")
	private MessageChannel requestInput;

	@Test
	public void test() {
		MessagingTemplate messagingTemplate = new MessagingTemplate();
		UUID requestId = UUID.randomUUID();
		Request fakeRequest = new Request(requestId, 1);

		Message<?> receive =
				messagingTemplate
						.sendAndReceive(requestInput,
								MessageBuilder
										.withPayload(fakeRequest)
										.setHeader("Content-Type", "application/json")
										.build()
						);
		assertThat(receive).isNotNull();
		assertThat(receive.getPayload()).isInstanceOf(Response.class);

		Response actual = (Response) receive.getPayload();
		assertThat(actual.getRequestId()).isEqualTo(requestId);
		assertThat(actual.getMessage()).isEqualTo("This is message 1");

	}

	@TestConfiguration
	public static class Config {

		public static final String TOPIC_EXCHANGE = "downstream";

		public static final String RESULTS_QUEUE = "downstream.results";

		public static final String RESULTS_ROUTING_KEY = "downstream.results.#";

		@Bean
		TopicExchange topicExchange() {

			return ExchangeBuilder
					.topicExchange(TOPIC_EXCHANGE)
					.build();
		}

		@Bean
		Queue resultsQueue() {

			return QueueBuilder
					.nonDurable(RESULTS_QUEUE)
					.build();
		}

		@Bean
		Binding resultsBinding(TopicExchange topicExchange, Queue resultsQueue) {

			return BindingBuilder.bind(resultsQueue)
					.to(topicExchange)
					.with(RESULTS_ROUTING_KEY);
		}

	}

}
