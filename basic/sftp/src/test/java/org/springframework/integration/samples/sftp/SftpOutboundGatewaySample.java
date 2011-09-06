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
import java.util.Random;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
		try {
			String tmpDir = System.getProperty("java.io.tmpdir");

			// remove the previous output files if necessary
			new File(new File(tmpDir), "1.ftptest").delete();
			new File(new File(tmpDir), "2.ftptest").delete();

			// create a couple of files in a temp dir
			File dir = new File(tmpDir + "/" + new Random().nextInt());
			dir.mkdir();
			File f1 = new File(dir, "1.ftptest");
			f1.createNewFile();
			File f2 = new File(dir, "2.ftptest");
			f2.createNewFile();


			// execute the flow (ls, get, rm, aggregate results)
			List<Boolean> rmResults = toFtpFlow.lsGetAndRmFiles(dir.getAbsolutePath());


			//Check everything went as expected, and clean up
			assertEquals(2, rmResults.size());
			for (Boolean result : rmResults) {
				assertTrue(result);
			}
			assertTrue("Expected remote dir to be empty", dir.delete());
			assertTrue("Could note delete retrieved file", new File(new File(tmpDir), "1.ftptest").delete());
			assertTrue("Could note delete retrieved file", new File(new File(tmpDir), "2.ftptest").delete());
		} finally {
			ctx.close();
		}
	}
}



