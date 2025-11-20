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
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.integrationFlow
import org.springframework.messaging.MessageChannel

/**
 * Configuration for the HelloWorld integration flow using Kotlin DSL.
 *
 * @author Glenn Renfro
 */
@Configuration
@EnableIntegration
open class HelloWorldConfig {

    @Bean
    open fun inputChannel() = DirectChannel()

    @Bean
    open fun outputChannel() = QueueChannel(10)

    @Bean
    open fun helloService() = HelloService()

    @Bean
    open fun helloWorldFlow(inputChannel: MessageChannel,
                            outputChannel: MessageChannel) = integrationFlow(inputChannel) {
            handle(helloService(), "sayHello")
            channel(outputChannel)
        }

}
