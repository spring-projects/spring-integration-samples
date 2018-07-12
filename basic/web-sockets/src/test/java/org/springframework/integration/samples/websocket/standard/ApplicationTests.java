/*
 * Copyright 2014-2018 the original author or authors.
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

package org.springframework.integration.samples.websocket.standard;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.samples.websocket.standard.server.Application;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Artem Bilan
 * @author Gary Russell
 * @since 3.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class ApplicationTests {

	@LocalServerPort
	private String port;

	@Test
	public void testWebSockets() throws InterruptedException {
		System.setProperty("local.server.port", this.port);
		ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("client-context.xml",
				org.springframework.integration.samples.websocket.standard.client.Application.class);
		DirectChannel webSocketInputChannel = ctx.getBean("webSocketInputChannel", DirectChannel.class);

		final CountDownLatch stopLatch = new CountDownLatch(2);

		webSocketInputChannel.addInterceptor(new ChannelInterceptor() {

			@Override
			public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
				Object payload = message.getPayload();
				assertThat(payload, instanceOf(String.class));
				Date date = null;
				try {
					date = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.DEFAULT).parse((String) payload);
				}
				catch (ParseException e) {
					fail("fail to parse date");
				}
				assertThat(new Date().compareTo(date), greaterThanOrEqualTo(0));
				stopLatch.countDown();
			}

		});
		assertTrue(stopLatch.await(10, TimeUnit.SECONDS));
		ctx.close();
	}

}
