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

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.PollableChannel
import org.springframework.messaging.support.GenericMessage

import static org.assertj.core.api.Assertions.assertThat


/**
 * Integration tests for HelloWorld flow.
 *
 * @author Glenn Renfro
 */
class HelloWorldConfigTests {

	AnnotationConfigApplicationContext context
	MessageChannel inputChannel
	PollableChannel outputChannel

	@BeforeEach
	void setup() {
		context = new AnnotationConfigApplicationContext(HelloWorldConfig)
		inputChannel = context.getBean('inputChannel', MessageChannel)
		outputChannel = context.getBean('outputChannel', PollableChannel)
	}

	@AfterEach
	void cleanup() {
		context?.close()
	}

	@Test
	void testHelloWorldFlow() {
		inputChannel.send(new GenericMessage<String>('World'))
		def message = outputChannel.receive(1000)
		assertThat(message).isNotNull()
		assertThat(message.payload).isNotNull()
	}

	@Test
	void testMultipleMessages() {
		inputChannel.send(new GenericMessage<String>('Test1'))
		inputChannel.send(new GenericMessage<String>('Test2'))
		
		def message1 = outputChannel.receive(1000)
		assertThat(message1).isNotNull()
		assertThat(message1.payload).isEqualTo('Hello Test1')
		
		def message2 = outputChannel.receive(1000)
		assertThat(message2).isNotNull()
		assertThat(message2.payload).isEqualTo('Hello Test2')
	}

}

