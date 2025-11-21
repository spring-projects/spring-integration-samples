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
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import java.time.Duration


/**
 * Integration tests for Poller flow.
 * Tests verify that the poller actually polls and produces messages.
 *
 * @author Glenn Renfro
 */
@SpringJUnitConfig(PollerConfig::class)
@DirtiesContext
class PollerConfigTests {

    @Autowired
    lateinit var pollerFlow: IntegrationFlow

    companion object {
        lateinit var listAppender: ListAppender

        @JvmStatic
        @BeforeAll
        fun setupLogger() {
            val loggerContext = LogManager.getContext(false) as LoggerContext
            listAppender = ListAppender("TestAppender")
            listAppender.start()
            loggerContext.configuration.addAppender(listAppender)
            loggerContext.rootLogger.addAppender(listAppender)
            loggerContext.updateLoggers()
        }

        @JvmStatic
        @AfterAll
        fun cleanupLogger() {
            val loggerContext = LogManager.getContext(false) as LoggerContext
            loggerContext.rootLogger.removeAppender(listAppender)
            listAppender.stop()
        }
    }

    @Test
    fun testPollerFlowBeanExists() {
        assertThat(pollerFlow).isNotNull
    }

    @Test
    fun testPollerFlowConfiguration() {
        val integrationComponents = pollerFlow.integrationComponents
        assertThat(integrationComponents).isNotNull
        assertThat(integrationComponents.size).isGreaterThan(0)
    }

    @Test
    fun testPollerIsActiveAndRunning() {
        await()
            .atMost(Duration.ofSeconds(5))
            .until { listAppender.events.isNotEmpty() }

        assertThat(listAppender.events)
            .anyMatch { event ->
                event.toString()
                    .contains("org.springframework.integration.samples.helloworld Level=INFO " +
                            "Message=GenericMessage [payload=")}

    }
}
