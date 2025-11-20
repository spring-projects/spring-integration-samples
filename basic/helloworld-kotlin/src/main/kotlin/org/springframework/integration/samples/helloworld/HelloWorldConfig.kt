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

package org.springframework.integration.samples.helloworld

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.channel.QueueChannel
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.dsl.integrationFlow
import org.springframework.messaging.MessageChannel

/**
 * Configuration for the HelloWorld integration flow using Kotlin DSL.
 *
 * @author Glenn Renfro
 */
@Configuration(proxyBeanMethods = false)
@EnableIntegration
class HelloWorldConfig {

    /**
     * Creates the input channel for inbound messages.
     *
     * A [DirectChannel] is used for synchronous, immediate message delivery.
     * Messages arriving on this channel are processed on the sender's thread
     * without any buffering or queuing.
     *
     * @return A [DirectChannel] instance for synchronous inbound message delivery
     */
    @Bean
    fun inputChannel() = DirectChannel()

    /**
     * Creates the output channel for outbound messages.
     *
     * A [QueueChannel] with default capacity provides asynchronous,
     * buffered message delivery. Results from the integration flow are queued
     * and available for downstream consumption.
     *
     * @return A [QueueChannel] instance with default capacity.
     */
    @Bean
    fun outputChannel() = QueueChannel()

    /**
     * Creates the Hello World business service.
     *
     * [HelloService] implements the core greeting logic that transforms
     * input messages into personalized greeting responses.
     *
     * @return A [HelloService] instance
     */
    @Bean
    fun helloService() = HelloService()

    /**
     * Defines the main integration flow for message processing.
     *
     * @param inputChannel The synchronous input channel receiving messages
     * @param outputChannel The asynchronous output channel for results
     * @param helloService The service implementing the greeting logic
     * @return An IntegrationFlow representing the complete message flow
     */
    @Bean
    fun helloWorldFlow(inputChannel: MessageChannel, outputChannel: MessageChannel, helloService: HelloService) =
        integrationFlow(inputChannel) {
            handle(helloService, "sayHello")
            channel(outputChannel)
        }
}
