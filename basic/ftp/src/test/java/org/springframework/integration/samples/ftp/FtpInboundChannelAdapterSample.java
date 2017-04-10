/*
 * Copyright 2002-2017 the original author or authors.
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

package org.springframework.integration.samples.ftp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;

/**
 *
 * @author Oleg Zhurakousky
 * @author Gunnar Hillert
 *
 */
public class FtpInboundChannelAdapterSample {

	private static final Logger LOGGER = LoggerFactory.getLogger(FtpInboundChannelAdapterSample.class);

	@Test
	public void runDemo() throws Exception{
		ConfigurableApplicationContext ctx =
			new ClassPathXmlApplicationContext("META-INF/spring/integration/FtpInboundChannelAdapterSample-context.xml");

		PollableChannel ftpChannel = ctx.getBean("ftpChannel", PollableChannel.class);

		Message<?> message1 = ftpChannel.receive(10000);
		Message<?> message2 = ftpChannel.receive(10000);
		Message<?> message3 = ftpChannel.receive(1000);

		LOGGER.info(String.format("Received first file message: %s.", message1));
		LOGGER.info(String.format("Received second file message: %s.", message2));
		LOGGER.info(String.format("Received nothing else: %s.", message3));

		assertNotNull(message1);
		assertNotNull(message2);
		assertNull("Was NOT expecting a third message.", message3);

		ctx.close();
	}

}
