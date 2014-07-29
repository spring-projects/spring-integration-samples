/*
 * Copyright 2002-2008 the original author or authors.
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.integration.file.remote.RemoteFileTemplate;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;

import com.jcraft.jsch.ChannelSftp.LsEntry;

/**
 * @author Oleg Zhurakousky
 * @author Gary Russell
 *
 */
public class SftpInboundReceiveSample {

	@Test
	public void runDemo(){
		ConfigurableApplicationContext context =
				new ClassPathXmlApplicationContext("/META-INF/spring/integration/SftpInboundReceiveSample-context.xml", this.getClass());
		RemoteFileTemplate<LsEntry> template = null;
		String file1 = "a.txt";
		String file2 = "b.txt";
		String file3 = "c.bar";
		new File("local-dir", file1).delete();
		new File("local-dir", file2).delete();
		try {
			PollableChannel localFileChannel = context.getBean("receiveChannel", PollableChannel.class);
			@SuppressWarnings("unchecked")
			SessionFactory<LsEntry> sessionFactory = context.getBean(CachingSessionFactory.class);
			template = new RemoteFileTemplate<LsEntry>(sessionFactory);
			SftpTestUtils.createTestFiles(template, file1, file2, file3);

			SourcePollingChannelAdapter adapter = context.getBean(SourcePollingChannelAdapter.class);
			adapter.start();

			Message<?> received = localFileChannel.receive();
			assertNotNull("Expected file", received);
			System.out.println("Received first file message: " + received);
			received = localFileChannel.receive();
			assertNotNull("Expected file", received);
			System.out.println("Received second file message: " + received);
			received = localFileChannel.receive(1000);
			assertNull("Expected null", received);
			System.out.println("No third file was received as expected");
		}
		finally {
			SftpTestUtils.cleanUp(template, file1, file2, file3);
			context.close();
			assertTrue("Could note delete retrieved file", new File("local-dir", file1).delete());
			assertTrue("Could note delete retrieved file", new File("local-dir", file2).delete());
		}
	}

}
