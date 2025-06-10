/*
 * Copyright 2025 the original author or authors.
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

package org.springframework.integration.samples.mqtt5;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.mqtt.inbound.Mqttv5PahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.Mqttv5PahoMessageHandler;
import org.springframework.integration.stream.CharacterStreamReadingMessageSource;
import org.springframework.messaging.MessageHandler;

import java.nio.charset.StandardCharsets;

/**
 * Starts the Spring Context and will initialize the Spring Integration message flow.
 *
 * @author Minyoung Noh
 *
 */
@SpringBootApplication
public class Application {

	@Value("${mqtt.broker.host:localhost}")
	private String mqttHost;

	@Value("${mqtt.broker.port:1883}")
	private int mqttPort;

	private static final Log LOGGER = LogFactory.getLog(Application.class);

	/**
	 * Load the Spring Integration Application Context
	 *
	 * @param args - command line arguments
	 */
	public static void main(final String... args) {

		LOGGER.info("\n========================================================="
				+ "\n                                                         "
				+ "\n          Welcome to Spring Integration!                 "
				+ "\n                                                         "
				+ "\n    For more information please visit:                   "
				+ "\n    https://spring.io/projects/spring-integration        "
				+ "\n                                                         "
				+ "\n=========================================================");

		LOGGER.info("\n========================================================="
				+ "\n                                                          "
				+ "\n    This is the MQTT5 Sample -                            "
				+ "\n                                                          "
				+ "\n    Please enter some text and press return. The entered  "
				+ "\n    Message will be sent to the configured MQTT topic,    "
				+ "\n    then again immediately retrieved from the Message     "
				+ "\n    Broker and ultimately printed to the command line.    "
				+ "\n                                                          "
				+ "\n=========================================================");

		SpringApplication.run(Application.class, args);
	}

	@Bean
	public MqttConnectionOptions mqttConnectionOptions() {
		MqttConnectionOptions options = new MqttConnectionOptions();
		options.setServerURIs(new String[]{ String.format("tcp://%s:%d", mqttHost, mqttPort) });
		options.setUserName("guest");
		options.setPassword("guest".getBytes(StandardCharsets.UTF_8));
		return options;
	}

	// publisher

	@Bean
	public IntegrationFlow mqttOutFlow() {
		return IntegrationFlow.from(CharacterStreamReadingMessageSource.stdin(),
						e -> e.poller(Pollers.fixedDelay(1000)))
				.transform(p -> p + " sent to MQTT5")
				.handle(mqttOutbound())
				.get();
	}

	@Bean
	public MessageHandler mqttOutbound() {
		Mqttv5PahoMessageHandler messageHandler = new Mqttv5PahoMessageHandler(mqttConnectionOptions(), "siSamplePublisher");
		messageHandler.setAsync(true);
		messageHandler.setAsyncEvents(true);
		messageHandler.setDefaultTopic("siSampleTopic");
		return messageHandler;
	}

	// consumer

	@Bean
	public IntegrationFlow mqttInFlow() {
		return IntegrationFlow.from(mqttInbound())
				.transform(p -> p + ", received from MQTT5")
				.handle(logger())
				.get();
	}

	private LoggingHandler logger() {
		LoggingHandler loggingHandler = new LoggingHandler("INFO");
		loggingHandler.setLoggerName("siSample");
		return loggingHandler;
	}

	@Bean
	public MessageProducerSupport mqttInbound() {
		Mqttv5PahoMessageDrivenChannelAdapter adapter =
				new Mqttv5PahoMessageDrivenChannelAdapter(mqttConnectionOptions(),"siSampleConsumer", "siSampleTopic");
		adapter.setCompletionTimeout(5000);
		adapter.setPayloadType(String.class);
		adapter.setMessageConverter(new MqttStringToBytesConverter());
		adapter.setQos(1);
		return adapter;
	}

}
