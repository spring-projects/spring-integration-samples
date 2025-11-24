/*
 * Copyright 2016-present the original author or authors.
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.stream.inbound.CharacterStreamReadingMessageSource;
import org.springframework.messaging.MessageHandler;

/**
 * Starts the Spring Context and will initialize the Spring Integration message flow.
 *
 * @author Gary Russell
 *
 */
@SpringBootApplication
public class Application {

	private static final Log LOGGER = LogFactory.getLog(Application.class);

	/**
	 * Load the Spring Integration Application Context
	 *
	 * @param args - command line arguments
	 */
	public static void main(final String... args) {

		LOGGER.info("""
				=========================================================\
				          Welcome to Spring Integration!                 \
				
				    For more information please visit:                   \
				
				    https://spring.io/projects/spring-integration        \
				                                                         \
				=========================================================""");

		LOGGER.info("""
				=========================================================\
				    This is the MQTT Sample -                             \
				
				    Please enter some text and press return. The entered  \
				    Message will be sent to the configured MQTT topic,    \
				    then again immediately retrieved from the Message     \
				    Broker and ultimately printed to the command line.    \
				
				=========================================================""");

		SpringApplication.run(Application.class, args);
	}

	@Bean
	public MqttPahoClientFactory mqttClientFactory(@Value("${mqtt.url}") String mqttUrl) {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		MqttConnectOptions options = new MqttConnectOptions();
		options.setServerURIs(new String[] {mqttUrl});
		options.setUserName("guest");
		options.setPassword("guest".toCharArray());
		factory.setConnectionOptions(options);
		return factory;
	}

	// publisher

	@Bean
	public IntegrationFlow mqttOutFlow(MessageHandler mqttOutbound) {
		return IntegrationFlow.from(CharacterStreamReadingMessageSource.stdin(),
						e -> e.poller(Pollers.fixedDelay(1000)))
				.transform(p -> p + " sent to MQTT")
				.handle(mqttOutbound)
				.get();
	}

	@Bean
	public MessageHandler mqttOutbound(MqttPahoClientFactory mqttClientFactory) {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("siSamplePublisher", mqttClientFactory);
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic("siSampleTopic");
		return messageHandler;
	}

	// consumer

	@Bean
	public IntegrationFlow mqttInFlow(MessageProducerSupport mqttInbound) {
		return IntegrationFlow.from(mqttInbound)
				.transform(p -> p + ", received from MQTT")
				.handle(logger())
				.get();
	}

	private LoggingHandler logger() {
		LoggingHandler loggingHandler = new LoggingHandler("INFO");
		loggingHandler.setLoggerName("siSample");
		return loggingHandler;
	}

	@Bean
	public MessageProducerSupport mqttInbound(MqttPahoClientFactory mqttClientFactory) {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("siSampleConsumer",
				mqttClientFactory, "siSampleTopic");
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		return adapter;
	}

}
