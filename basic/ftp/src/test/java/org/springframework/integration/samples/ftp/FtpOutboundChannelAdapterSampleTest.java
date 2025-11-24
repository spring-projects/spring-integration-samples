/*
 * Copyright 2002-present the original author or authors.
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

package org.springframework.integration.samples.ftp;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Oleg Zhurakousky
 * @author Gunnar Hillert
 *
 */
public class FtpOutboundChannelAdapterSampleTest extends BaseFtpTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(FtpOutboundChannelAdapterSampleTest.class);

	private final File baseFolder = new File("target" + File.separator + "toSend");

	@Test
	public void runDemo() throws Exception {

		ConfigurableApplicationContext ctx =
				new ClassPathXmlApplicationContext("META-INF/spring/integration/FtpOutboundChannelAdapterSample-context.xml");

		MessageChannel ftpChannel = ctx.getBean("ftpChannel", MessageChannel.class);

		baseFolder.mkdirs();

		final File fileToSendA = new File(baseFolder, "a.txt");
		final File fileToSendB = new File(baseFolder, "b.txt");

		final InputStream inputStreamA = FtpOutboundChannelAdapterSampleTest.class.getResourceAsStream("/test-files/a.txt");
		final InputStream inputStreamB = FtpOutboundChannelAdapterSampleTest.class.getResourceAsStream("/test-files/b.txt");

		FileUtils.copyInputStreamToFile(inputStreamA, fileToSendA);
		FileUtils.copyInputStreamToFile(inputStreamB, fileToSendB);

		assertThat(fileToSendA).exists();
		assertThat(fileToSendB).exists();

		final Message<File> messageA = MessageBuilder.withPayload(fileToSendA).build();
		final Message<File> messageB = MessageBuilder.withPayload(fileToSendB).build();

		ftpChannel.send(messageA);
		ftpChannel.send(messageB);

		Thread.sleep(2000);

		assertThat(new File(BaseFtpTest.FTP_ROOT_DIR + File.separator + "a.txt")).exists();
		assertThat(new File(BaseFtpTest.FTP_ROOT_DIR + File.separator + "b.txt")).exists();

		LOGGER.info("Successfully transferred file 'a.txt' and 'b.txt' to a remote FTP location.");
		ctx.close();
	}

	@AfterEach
	public void cleanup() {
		FileUtils.deleteQuietly(baseFolder);
	}

}
