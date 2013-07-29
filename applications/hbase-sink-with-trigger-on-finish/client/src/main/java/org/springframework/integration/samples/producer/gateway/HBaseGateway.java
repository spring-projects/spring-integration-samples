/*
 * Copyright 2002-2010 the original author or authors.
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

package org.springframework.integration.samples.producer.gateway;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.core.support.RabbitGatewaySupport;
import org.springframework.integration.samples.beans.DocumentToIndex;
import org.springframework.integration.samples.beans.RoutingKey;
import org.springframework.integration.samples.producer.gateway.interfaces.StorageGateway;

/**
 * Implementation of the {@link StorageGateway} for sending documents to the Buffer
 * 
 * @author Flavio Pompermaier
 */
public class HBaseGateway extends RabbitGatewaySupport implements StorageGateway {

	private static Log logger = LogFactory.getLog(HBaseGateway.class);
	
	@Override
	public void sendDocumentToPersist(DocumentToIndex d) {
		final RoutingKey rk = d.getRoutingKey();
		logger.info("Sending document to route " + rk);
		getRabbitTemplate().convertAndSend(rk.toString(), d);
	}

}
