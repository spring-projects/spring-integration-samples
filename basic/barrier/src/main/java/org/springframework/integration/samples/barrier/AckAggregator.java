/*
 * Copyright 2015 the original author or authors.
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
package org.springframework.integration.samples.barrier;

import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.integration.aggregator.MessageGroupProcessor;
import org.springframework.integration.store.MessageGroup;
import org.springframework.messaging.Message;

/**
 * @author Gary Russell
 * @since 4.2
 *
 */
public class AckAggregator implements MessageGroupProcessor {

	@Override
	public Object processMessageGroup(MessageGroup group) {
		StringBuilder builder = new StringBuilder("Result: ");
		for (Message<?> message : group.getMessages()) {
			if (builder.length() > 8) {
				builder.append(", ");
			}
			builder.append(message.getPayload() + ": ack=" + message.getHeaders().get(AmqpHeaders.PUBLISH_CONFIRM));
		}
		return builder.toString();
	}

}
