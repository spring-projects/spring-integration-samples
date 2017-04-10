/*
 * Copyright 2002-2016 the original author or authors.
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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.integration.samples.ftp.support.TestUserManager;

/**
 * Test Suite that will bootstrap an embedded Apache FTP Server. Additionally some
 * test files will be send to the FTP Server.
 *
 *
 * @author Gunnar Hillert
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
		FtpOutboundChannelAdapterSample.class,
		FtpInboundChannelAdapterSample.class,
		FtpOutboundGatewaySample.class
})
public class TestSuite {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestSuite.class);

	public static final String FTP_ROOT_DIR = "target" + File.separator + "ftproot";

	public static final String LOCAL_FTP_TEMP_DIR = "target" + File.separator + "local-ftp-temp";

	public static final String SERVER_PORT_SYSTEM_PROPERTY = "availableServerPort";

	@ClassRule
	public static final TemporaryFolder temporaryFolder = new TemporaryFolder();

	public static FtpServer server;

	@BeforeClass
	public static void setupFtpServer() throws FtpException, IOException {

		Integer availableServerSocket;

		if (System.getProperty(SERVER_PORT_SYSTEM_PROPERTY) == null) {
			availableServerSocket = 0;
		}
		else {
			availableServerSocket = Integer.valueOf(System.getProperty(SERVER_PORT_SYSTEM_PROPERTY));
		}

		File ftpRoot = new File(FTP_ROOT_DIR);
		ftpRoot.mkdirs();

		TestUserManager userManager = new TestUserManager(ftpRoot.getAbsolutePath());

		FtpServerFactory serverFactory = new FtpServerFactory();
		serverFactory.setUserManager(userManager);
		ListenerFactory factory = new ListenerFactory();

		factory.setPort(availableServerSocket);
		factory.setIdleTimeout(600);
		serverFactory.addListener("default", factory.createListener());

		server = serverFactory.createServer();

		server.start();

		Listener listener = serverFactory.getListeners().values().iterator().next();
		availableServerSocket = listener.getPort();
		LOGGER.info("Using open server port..." + availableServerSocket);
		System.setProperty(SERVER_PORT_SYSTEM_PROPERTY, availableServerSocket.toString());
	}

	@AfterClass
	public static void shutDown() {
		server.stop();
		FileUtils.deleteQuietly(new File(FTP_ROOT_DIR));
		FileUtils.deleteQuietly(new File(LOCAL_FTP_TEMP_DIR));
	}

}
