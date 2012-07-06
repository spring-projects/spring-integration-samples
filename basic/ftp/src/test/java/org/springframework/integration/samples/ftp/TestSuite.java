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

import java.io.File;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.io.FileUtils;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
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

	public static final String FTP_ROOT_DIR       = "target" + File.separator + "ftproot";
	public static final String LOCAL_FTP_TEMP_DIR = "target" + File.separator + "local-ftp-temp";

	@ClassRule
	public static final TemporaryFolder temporaryFolder = new TemporaryFolder();

	public static FtpServer server;

	@BeforeClass
	public static void setupFtpServer() throws FtpException, SocketException, IOException {

		File ftpRoot = new File (FTP_ROOT_DIR);
		ftpRoot.mkdirs();

		TestUserManager userManager = new TestUserManager(ftpRoot.getAbsolutePath());

		FtpServerFactory serverFactory = new FtpServerFactory();
		serverFactory.setUserManager(userManager);
		ListenerFactory factory = new ListenerFactory();

		factory.setPort(3333);

		serverFactory.addListener("default", factory.createListener());

		server = serverFactory.createServer();

		server.start();

	}

	@AfterClass
	public static void shutDown() {
		server.stop();
		FileUtils.deleteQuietly(new File(FTP_ROOT_DIR));
		FileUtils.deleteQuietly(new File(LOCAL_FTP_TEMP_DIR));
	}

}
