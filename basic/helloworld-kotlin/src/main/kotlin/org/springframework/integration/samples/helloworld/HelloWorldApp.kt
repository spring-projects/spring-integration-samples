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

import org.apache.commons.logging.LogFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.PollableChannel
import org.springframework.messaging.support.GenericMessage

/**
 * Demonstrates a basic Message Endpoint that simply prepends a greeting
 * ("Hello ") to an inbound String payload from a Message.
 *
 * @author Glenn Renfro
 */
object HelloWorldApp {

    private val logger = LogFactory.getLog(HelloWorldApp::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        val context = AnnotationConfigApplicationContext(HelloWorldConfig::class.java)
        val inputChannel = context.getBean("inputChannel", MessageChannel::class.java)
        val outputChannel = context.getBean("outputChannel", PollableChannel::class.java)
        inputChannel.send(GenericMessage("World"))
        logger.info("==> HelloWorldDemo: ${outputChannel.receive(0)?.payload}")
        context.close()
    }

}
