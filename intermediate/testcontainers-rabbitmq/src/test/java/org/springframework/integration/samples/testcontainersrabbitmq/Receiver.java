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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class Receiver {

	private static final Logger log = LoggerFactory.getLogger(Receiver.class);

	private static final Map<Integer, String> messages;

	static {

		messages = new HashMap<>();

		messages.put(1, "This is message 1");
		messages.put(2, "This is message 2");
		messages.put(3, "This is message 3");
		messages.put(4, "This is message 4");
		messages.put(5, "This is message 5");

	}

	@PostConstruct
	public void initialize() {
		log.info("Receiver initialized!");
	}

	@RabbitListener(
			bindings = @QueueBinding(
					value = @Queue(value = "downstream.request", durable = "true"),
					exchange = @Exchange(value = "downstream", ignoreDeclarationExceptions = "true", type = "topic"),
					key = "downstream.request.#"
			)
	)
	@SendTo("downstream.results")
	public Response handleMessage(Request request) {
		log.info("handleMessage : received message [{}]", request);

		Integer messageId;
		if (null != request.getMessageId()) {

			messageId = request.getMessageId();

		}
		else {

			messageId = new Random().ints(1, 5).findFirst().getAsInt();

		}

		return new Response(request.getId(), messages.get(messageId));
	}

}
