/*
 * Copyright 2002-2022 the original author or authors.
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

package org.springframework.integration.samples.tcpclientserver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.endpoint.AbstractEndpoint;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.util.TestingUtilities;
import org.springframework.messaging.MessagingException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


/**
 * Demonstrates the use of a gateway as an entry point into the integration flow,
 * with two pairs of collaborating channel adapters (client and server), which
 * enables multiplexing multiple messages over the same connection.
 *
 * Requires correlation data in the payload.
 *
 * @author Gary Russell
 * @author Artem Bilan
 *
 * @since 2.1
 *
 */
@SpringJUnitConfig(locations = "/META-INF/spring/integration/tcpClientServerDemo-conversion-context.xml")
@DirtiesContext
public class TcpClientServerDemoTest {

	@Autowired
	SimpleGateway gw;

	@Autowired
	AbstractServerConnectionFactory crLfServer;

	@Autowired
	AbstractClientConnectionFactory client;

	@Autowired
	@Qualifier("outAdapter.client")
	AbstractEndpoint outAdapterClient;

	@Autowired
	@Qualifier("inAdapter.client")
	AbstractEndpoint inAdapterClient;

	@BeforeEach
	public void setup() {
		if (!this.outAdapterClient.isRunning()) {
			TestingUtilities.waitListening(this.crLfServer, 10000L);
			this.client.setPort(this.crLfServer.getPort());
			this.outAdapterClient.start();
			this.inAdapterClient.start();
		}
	}

	@Test
	public void testHappyDay() {
		String result = gw.send("999Hello world!"); // first 3 bytes is correlationid
		assertThat(result).isEqualTo("999Hello world!:echo");
	}

	@Test
	public void testMultiPlex() throws Exception {
		TaskExecutor executor = new SimpleAsyncTaskExecutor();
		final CountDownLatch latch = new CountDownLatch(100);
		final BlockingQueue<Integer> results = new LinkedBlockingQueue<>();
		for (int i = 100; i < 200; i++) {
			results.add(i);
			final int j = i;
			executor.execute(() -> {
				String result = gw.send(j + "Hello world!"); // first 3 bytes is correlationId
				assertThat(result).isEqualTo(j + "Hello world!:echo");
				results.remove(j);
				latch.countDown();
			});
		}
		assertThat(latch.await(60, TimeUnit.SECONDS)).isTrue();
		assertThat(results).hasSize(0);
	}

	@Test
	public void testTimeoutThrow() {
		assertThatExceptionOfType(MessagingException.class)
				.isThrownBy(() -> gw.send("TIMEOUT_TEST_THROW"))
				.withMessageContaining("No response received for TIMEOUT_TEST");
	}

	@Test
	public void testTimeoutReturn() {
		assertThatExceptionOfType(MessagingException.class)
				.isThrownBy(() -> gw.send("TIMEOUT_TEST_RETURN"))
				.withMessageContaining("No response received for TIMEOUT_TEST");
	}

}
