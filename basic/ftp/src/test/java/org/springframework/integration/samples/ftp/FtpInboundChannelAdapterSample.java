/*
 * Copyright 2002-2011 the original author or authors.
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

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.core.PollableChannel;

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
		
		Message<?> message1 = ftpChannel.receive(2000);
		Message<?> message2 = ftpChannel.receive(2000);
		Message<?> message3 = ftpChannel.receive(1000);

		LOGGER.info("Received first file message: {}.", message1);
		LOGGER.info("Received second file message: {}.", message2);
		LOGGER.info("Received nothing else: {}.", message3);
		
		assertNotNull(message1);
		assertNotNull(message2);
		assertNull("Was NOT expecting a third message.", message3);
		
	}
	
	@After
	public void cleanup() {
		FileUtils.deleteQuietly(new File(TestSuite.LOCAL_FTP_TEMP_DIR));
	}
}
