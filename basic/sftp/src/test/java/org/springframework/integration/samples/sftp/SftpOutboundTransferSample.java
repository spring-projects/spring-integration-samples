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

package org.springframework.integration.samples.sftp;

import java.io.File;

import org.junit.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.file.remote.RemoteFileTemplate;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.Assert;

import com.jcraft.jsch.ChannelSftp.LsEntry;

/**
 *
 * @author Oleg Zhurakousky
 * @author Gunnar Hillert
 * @author Gary Russell
 *
 */
public class SftpOutboundTransferSample {

	@Test
	public void testOutbound() throws Exception{

		final String sourceFileName = "README.md";
		final String destinationFileName = sourceFileName +"_foo";

		final ClassPathXmlApplicationContext ac =
			new ClassPathXmlApplicationContext("/META-INF/spring/integration/SftpOutboundTransferSample-context.xml",
					SftpOutboundTransferSample.class);
		@SuppressWarnings("unchecked")
		SessionFactory<LsEntry> sessionFactory = ac.getBean(CachingSessionFactory.class);
		RemoteFileTemplate<LsEntry> template = new RemoteFileTemplate<LsEntry>(sessionFactory);
		SftpTestUtils.createTestFiles(template); // Just the directory

		try {
			final File file = new File(sourceFileName);

			Assert.isTrue(file.exists(), String.format("File '%s' does not exist.", sourceFileName));

			final Message<File> message = MessageBuilder.withPayload(file).build();
			final MessageChannel inputChannel = ac.getBean("inputChannel", MessageChannel.class);

			inputChannel.send(message);
			Thread.sleep(2000);

			Assert.isTrue(SftpTestUtils.fileExists(template, destinationFileName),
					String.format("File '%s' does not exist.", destinationFileName));

			System.out.println(String.format("Successfully transferred '%s' file to a " +
					"remote location under the name '%s'", sourceFileName, destinationFileName));
		}
		finally {
			SftpTestUtils.cleanUp(template, destinationFileName);
			ac.close();
		}
	}

}
