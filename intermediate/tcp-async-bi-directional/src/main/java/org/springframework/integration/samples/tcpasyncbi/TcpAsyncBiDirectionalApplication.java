/*
 * Copyright 2020-present the original author or authors.
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

package org.springframework.integration.samples.tcpasyncbi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Demonstrates independent bi-directional communication between peers.
 * One side opens the connection and they proceed to send messages to each
 * other on different schedules.
 * There are two client instances.
 *
 * @author Gary Russell
 * @since 5.3
 *
 */
@SpringBootApplication
@EnableConfigurationProperties(SampleProperties.class)
public final class TcpAsyncBiDirectionalApplication {

	private TcpAsyncBiDirectionalApplication() {
	}

	public static void main(String[] args) {
		SpringApplication.run(TcpAsyncBiDirectionalApplication.class, args);
	}

}
