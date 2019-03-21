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

import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;

/**
 * @author Gary Russell
 *
 * @since 5.2
 *
 */
public class BrokerRunning extends TestWatcher {

	private static Log logger = LogFactory.getLog(BrokerRunning.class);

	// Static so that we only test once on failure: speeds up test suite
	private static Map<Integer, Boolean> brokerOnline = new HashMap<>();

	private final int port;

	private BrokerRunning(int port) {
		this.port = port;
		brokerOnline.put(port, true);
	}

	@Override
	public Statement apply(Statement base, Description description) {
		assumeTrue(brokerOnline.get(port));
		String url = "tcp://localhost:" + port;
		IMqttClient client = null;
		try {
			client = new DefaultMqttPahoClientFactory().getClientInstance(url, "junit-" + System.currentTimeMillis());
			client.connect();
		}
		catch (MqttException e) {
			logger.warn("Tests not running because no broker on " + url + ":", e);
			assumeNoException(e);
		}
		finally {
			if (client != null) {
				try {
					client.disconnect();
					client.close();
				}
				catch (MqttException e) {
				}
			}
		}
		return super.apply(base, description);
	}


	public static BrokerRunning isRunning(int port) {
		return new BrokerRunning(port);
	}

}
