/*
 * Copyright 2002-2022 the original author or authors.
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

package org.springframework.integration.samples.jms;

import org.apache.activemq.artemis.api.core.SimpleString;
import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.remoting.impl.invm.InVMAcceptorFactory;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.apache.activemq.artemis.core.settings.impl.AddressSettings;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.artemis.utils.ObjectInputStreamWithClassLoader;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import org.springframework.jms.connection.CachingConnectionFactory;

/**
 * Keeps an ActiveMQ open for the duration of
 * all tests (avoids cycling the transport each time the last
 * connection is closed).
 *
 * @author Gary Russell
 * @author Artem Bilan
 *
 * @since 3.0
 */
public abstract class ActiveMQMultiContextTests {

	public static final ActiveMQConnectionFactory amqFactory = new ActiveMQConnectionFactory("vm://0");

	public static final CachingConnectionFactory connectionFactory = new CachingConnectionFactory(amqFactory);

	private static final EmbeddedActiveMQ broker = new EmbeddedActiveMQ();

	static {
		amqFactory.setDeserializationWhiteList(ObjectInputStreamWithClassLoader.CATCH_ALL_WILDCARD);
		amqFactory.setRetryInterval(0);
	}

	@BeforeAll
	public static void startUp() throws Exception {
		Configuration configuration =
				new ConfigurationImpl()
						.setName("embedded-server")
						.setPersistenceEnabled(false)
						.setSecurityEnabled(false)
						.setJMXManagementEnabled(false)
						.setJournalDatasync(false)
						.addAcceptorConfiguration(new TransportConfiguration(InVMAcceptorFactory.class.getName()))
						.addAddressSetting("#",
								new AddressSettings()
										.setDeadLetterAddress(SimpleString.toSimpleString("dla"))
										.setExpiryAddress(SimpleString.toSimpleString("expiry")));
		broker.setConfiguration(configuration).start();
		connectionFactory.setCacheConsumers(false);
	}

	@AfterAll
	public static void shutDown() throws Exception {
		connectionFactory.destroy();
		amqFactory.createConnection().close();
		broker.stop();
	}

}
