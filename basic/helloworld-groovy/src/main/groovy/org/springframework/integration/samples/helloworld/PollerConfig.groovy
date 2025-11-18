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
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.dsl.Pollers
import org.springframework.integration.handler.LoggingHandler
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import static org.springframework.integration.groovy.dsl.IntegrationGroovyDsl.integrationFlow

import java.util.concurrent.Executor

/**
 * Configuration for the Poller integration flow using the Groovy DSL.
 * This flow polls for the current system time every 20 seconds and logs it.
 *
 * @author Glenn Renfro
 */
@Configuration
@EnableIntegration
class PollerConfig {
	@Bean
	pollerFlow() {
		integrationFlow(
				{ System.currentTimeMillis() },
				{ e -> e.poller(Pollers.fixedDelay(20000).maxMessagesPerPoll(2)) })
				{
					log LoggingHandler.Level.INFO, 'org.springframework.integration.samples.helloworld'
					channel { queue 'loggerChannel' }
				}
	}

	@Bean
	Executor executor() {
		new ThreadPoolTaskExecutor().with {
			corePoolSize = 5
			maxPoolSize = 20
			queueCapacity = 20
			threadNamePrefix = 'executor-'
			initialize()
			it
		}
	}
}

