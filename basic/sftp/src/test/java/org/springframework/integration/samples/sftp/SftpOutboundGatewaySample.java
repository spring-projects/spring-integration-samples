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
package org.springframework.integration.samples.sftp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.file.remote.RemoteFileTemplate;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;

import com.jcraft.jsch.ChannelSftp.LsEntry;

/**
 * Demonstrates use of the outbound gateway to use ls, get and rm.
 * Creates a temporary directory with 2 files; retrieves and removes them.
 * @author Gary Russell
 * @since 2.1
 *
 */
public class SftpOutboundGatewaySample {

	@Test
	public void testLsGetRm() throws Exception {
		ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath:/META-INF/spring/integration/SftpOutboundGatewaySample-context.xml");
		ToSftpFlowGateway toFtpFlow = ctx.getBean(ToSftpFlowGateway.class);
		RemoteFileTemplate<LsEntry> template = null;
		String file1 = "1.ftptest";
		String file2 = "2.ftptest";
		File tmpDir = new File(System.getProperty("java.io.tmpdir"));

		try {
			// remove the previous output files if necessary
			new File(tmpDir, file1).delete();
			new File(tmpDir, file2).delete();

			@SuppressWarnings("unchecked")
			SessionFactory<LsEntry> sessionFactory = ctx.getBean(CachingSessionFactory.class);
			template = new RemoteFileTemplate<LsEntry>(sessionFactory);
			SftpTestUtils.createTestFiles(template, file1, file2);

			// execute the flow (ls, get, rm, aggregate results)
			List<Boolean> rmResults = toFtpFlow.lsGetAndRmFiles("si.sftp.sample");


			//Check everything went as expected, and clean up
			assertEquals(2, rmResults.size());
			for (Boolean result : rmResults) {
				assertTrue(result);
			}

		}
		finally {
			SftpTestUtils.cleanUp(template, file1, file2);
			ctx.close();
			assertTrue("Could note delete retrieved file", new File(tmpDir, file1).delete());
			assertTrue("Could note delete retrieved file", new File(tmpDir, file2).delete());
		}
	}

}



