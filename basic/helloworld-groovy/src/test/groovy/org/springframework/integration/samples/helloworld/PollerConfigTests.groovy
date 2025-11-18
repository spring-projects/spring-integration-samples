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
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

import java.time.Duration

import static org.assertj.core.api.Assertions.assertThat
import static org.awaitility.Awaitility.await

/**
 * Integration tests for Poller flow.
 * Tests verify that the poller actually polls and produces messages.
 *
 * @author Glenn Renfro
 */
@SpringJUnitConfig(PollerConfig.class)
class PollerConfigTests {

	@Autowired
	IntegrationFlow pollerFlow

	static ListAppender listAppender

	@BeforeAll
	static void setupLogger() {
		LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false)
		listAppender = new ListAppender('TestAppender')
		listAppender.start()
		loggerContext.getConfiguration().addAppender(listAppender)
		loggerContext.getRootLogger().addAppender(listAppender)
		loggerContext.updateLoggers()
	}

	@AfterAll
	static void cleanupLogger() {
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
		// Poll until we get the expected log event
		await()
				.atMost(Duration.ofSeconds(5))
				.until(() -> !listAppender.getEvents().isEmpty())

		assertThat(listAppender.getEvents().get(0).toString())
				.contains('org.springframework.integration.samples.helloworld Level=INFO Message=GenericMessage [payload=')
	}

}

