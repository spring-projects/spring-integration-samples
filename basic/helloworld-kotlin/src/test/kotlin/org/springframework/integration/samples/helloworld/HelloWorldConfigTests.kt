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

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.PollableChannel
import org.springframework.messaging.support.GenericMessage
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

/**
 * Integration tests for HelloWorld flow.
 *
 * @author Glenn Renfro
 */
@SpringJUnitConfig(HelloWorldConfig::class)
class HelloWorldConfigTests {

    @Autowired
    lateinit var inputChannel: MessageChannel

    @Autowired
    lateinit var outputChannel: PollableChannel

    @Test
    fun testHelloWorldFlow() {
        inputChannel.send(GenericMessage("World"))
        val message = outputChannel.receive(1000)
        assertThat(message).isNotNull
        assertThat(message?.payload).isNotNull
    }

    @Test
    fun testMultipleMessages() {
        inputChannel.send(GenericMessage("Test1"))
        inputChannel.send(GenericMessage("Test2"))

        val message1 = outputChannel.receive(1000)
        assertThat(message1).isNotNull
        assertThat(message1?.payload).isEqualTo("Hello Test1")

        val message2 = outputChannel.receive(1000)
        assertThat(message2).isNotNull
        assertThat(message2?.payload).isEqualTo("Hello Test2")
    }

}
