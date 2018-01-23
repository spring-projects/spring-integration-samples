/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.samples.tcpclientserver;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.util.TestingUtilities;
import org.springframework.integration.samples.tcpclientserver.support.CustomTestContextLoader;
import org.springframework.messaging.MessagingException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


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
@ContextConfiguration(loader = CustomTestContextLoader.class,
		locations = { "/META-INF/spring/integration/tcpClientServerDemo-conversion-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class TcpClientServerDemoTest {

	@Autowired
	SimpleGateway gw;

	@Autowired
	AbstractServerConnectionFactory crLfServer;

	@Before
	public void setup() {
		TestingUtilities.waitListening(this.crLfServer, 10000L);
	}

	@Test
	public void testHappyDay() {
		String result = gw.send("999Hello world!"); // first 3 bytes is correlationid
		assertEquals("999Hello world!:echo", result);
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
				String result = gw.send(j + "Hello world!"); // first 3 bytes is correlationid
				assertEquals(j + "Hello world!:echo", result);
				results.remove(j);
				latch.countDown();
			});
		}
		assertTrue(latch.await(60, TimeUnit.SECONDS));
		assertEquals(0, results.size());
	}

	@Test
	public void testTimeoutThrow() {
		try {
			gw.send("TIMEOUT_TEST_THROW");
			fail("expected exception");
		}
		catch (MessagingException e) {
			assertThat(e.getMessage(), containsString("No response received for TIMEOUT_TEST"));
		}
	}

	@Test
	public void testTimeoutReturn() {
		try {
			gw.send("TIMEOUT_TEST_RETURN");
			fail("expected exception");
		}
		catch (MessagingException e) {
			assertThat(e.getMessage(), containsString("No response received for TIMEOUT_TEST"));
		}
	}

}
