/*
 * Copyright 2025-present the original author or authors.
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
package org.springframework.integration.samples.controlbus.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.messaging.MessageChannel;

/**
 * Java-based Spring Integration configuration for the Control Bus sample.
 * <p>
 * This configuration demonstrates the Control Bus component functionality,
 * which uses SpEL expressions to control Spring Integration endpoints
 * (e.g., start/stop an inbound adapter).
 * <p>
 * Activate this configuration using the "java-config" profile.
 * <p>
 * This configuration is functionally equivalent to {@code ControlBusDemo-context.xml}
 * and defines:
 * <ul>
 *   <li>A control channel for sending control messages</li>
 *   <li>An output channel (queue-based) for the inbound adapter</li>
 *   <li>A control bus endpoint that processes control messages</li>
 *   <li>An inbound channel adapter that generates "Hello" messages at a fixed rate</li>
 * </ul>
 *
 * @author Glenn Renfro
 */
@Configuration
@EnableIntegration
@Profile("java-config")
public class ControlBusConfiguration {

	/**
	 * Create a direct channel for sending control messages to the control bus.
	 * <p>
	 * This channel is used to send SpEL expressions that control Spring Integration
	 * endpoints (e.g., {@code @inboundAdapter.start()} or {@code @inboundAdapter.stop()}).
	 *
	 * @return a DirectChannel for control messages
	 */
	@Bean
	public MessageChannel controlChannel() {
		return new DirectChannel();
	}

	/**
	 * Create a queue-based channel for receiving messages from the inbound adapter.
	 * <p>
	 * This channel uses a queue to allow asynchronous message consumption.
	 *
	 * @return a QueueChannel for adapter output
	 */
	@Bean
	public MessageChannel adapterOutputChannel() {
		return new QueueChannel();
	}

	/**
	 * Create an integration flow that connects the control channel to the control bus.
	 * <p>
	 * Messages sent to the control channel are processed by the control bus,
	 * which evaluates SpEL expressions to control Spring Integration endpoints.
	 *
	 * @param controlChannel input channel for control commands
	 * @return an IntegrationFlow connecting controlChannel to controlBus
	 */
	@Bean
	public IntegrationFlow controlBusFlow(@Qualifier("controlChannel") MessageChannel controlChannel) {
		return IntegrationFlow.from(controlChannel)
				.controlBus()
				.get();
	}

	/**
	 * Create an inbound channel adapter that generates messages.
	 * <p>
	 * The adapter:
	 * <ul>
	 *   <li>Generates the string "Hello" as the payload</li>
	 *   <li>Sends messages to the adapterOutputChannel</li>
	 *   <li>Starts with auto-startup disabled (must be started via control bus)</li>
	 *   <li>Polls at a fixed rate of 1000ms</li>
	 * </ul>
	 *
	 * @return an IntegrationFlow representing the inbound adapter
	 */
	@Bean
	public IntegrationFlow inboundAdapter() {
		return IntegrationFlow.fromSupplier(() -> "Hello",
						c -> c.id("inboundAdapter")
								.autoStartup(false)
								.poller(Pollers.fixedRate(1000)))
				.channel(adapterOutputChannel())
				.get();
	}

}

