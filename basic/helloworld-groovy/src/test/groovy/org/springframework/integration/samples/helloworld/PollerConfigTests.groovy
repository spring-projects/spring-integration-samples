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

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.test.appender.ListAppender
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.integration.dsl.IntegrationFlow

import java.time.Duration

import static org.assertj.core.api.Assertions.assertThat
import static org.awaitility.Awaitility.await

/**
 * Integration tests for Poller flow.
 * Tests verify that the poller actually polls and produces messages.
 *
 * @author Glenn Renfro
 */
class PollerConfigTests {

	AnnotationConfigApplicationContext context
	IntegrationFlow pollerFlow
	ListAppender listAppender


	@BeforeEach
	void setup() {
		// Setup Log4j2 ListAppender to capture logs
		LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false)
		listAppender = new ListAppender('TestAppender')
		listAppender.start()
		loggerContext.getConfiguration().addAppender(listAppender)
		loggerContext.getRootLogger().addAppender(listAppender)
		loggerContext.updateLoggers()

		context = new AnnotationConfigApplicationContext(PollerConfig)
		pollerFlow = context.getBean('pollerFlow', IntegrationFlow)
	}

	@AfterEach
	void cleanup() {
		context?.close()
		LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false)
		loggerContext.getRootLogger().removeAppender(listAppender)
		listAppender.stop()
	}

	@Test
	void testPollerFlowBeanExists() {
		assertThat(pollerFlow).isNotNull()
	}

	@Test
	void testPollerFlowConfiguration() {
		// Verify the flow is properly configured
		def integrationComponents = pollerFlow.integrationComponents
		assertThat(integrationComponents).isNotNull()
		assertThat(integrationComponents.size()).isGreaterThan(0)
	}

	@Test
	void testPollerIsActiveAndRunning() {
		assertThat(context.isActive()).isTrue()
		assertThat(context.isRunning()).isTrue()

		// Poll until we get the expected log event
		await()
				.atMost(Duration.ofSeconds(5))
				.until(() -> !listAppender.getEvents().isEmpty())

		assertThat(context.isRunning()).isTrue()
		assertThat(listAppender.getEvents().get(0).toString())
				.contains('org.springframework.integration.samples.helloworld Level=INFO Message=GenericMessage [payload=')
	}

}

