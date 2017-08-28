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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates use of the outbound gateway to use ls, get and rm.
 *
 * The previous Test {@link FtpOutboundChannelAdapterSample} was uploading 2 test
 * files:
 *
 * <ul>
 *     <li>a.txt</li>
 *     <li>b.txt</li>
 * </ul>
 *
 * This test will now retrieves those 2 files and removes them. Instead of just
 * polling the file, the files are instead retrieved and deleted using explicit
 * FTP commands (LS and RM)
 *
 * @author Gary Russell
 * @since 2.1
 *
 */
public class FtpOutboundGatewaySample {


	@Test
	public void testLsGetRm() throws Exception {
		ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath:/META-INF/spring/integration/FtpOutboundGatewaySample-context.xml");

		final ToFtpFlowGateway toFtpFlow = ctx.getBean(ToFtpFlowGateway.class);

		// execute the flow (ls, get, rm, aggregate results)
		List<Boolean> rmResults = toFtpFlow.lsGetAndRmFiles("/");

		//Check everything went as expected, and clean up
		assertEquals("Was expecting the collection 'rmResults' to contain 2 elements.", 2, rmResults.size());

		for (Boolean result : rmResults) {
			assertTrue(result);
		}

		assertTrue("Expected FTP remote directory to be empty",  new File(TestSuite.FTP_ROOT_DIR).delete());

		ctx.close();
	}

}
