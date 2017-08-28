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
package org.springframework.integration.samples.advice;

import static org.mockito.Mockito.when;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPFile;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.integration.file.remote.session.SessionFactory;

/**
 * @author Gary Russell
 * @since 2.2
 *
 */
public class FileTransferRenameAfterFailureDemo {

	private static final Log LOGGER = LogFactory.getLog(FileTransferRenameAfterFailureDemo.class);

	public static void main(String[] args) throws Exception {
		LOGGER.info("\n========================================================="
				  + "\n                                                         "
				  + "\n          Welcome to Spring Integration!                 "
				  + "\n                                                         "
				  + "\n    For more information please visit:                   "
				  + "\n    http://www.springsource.org/spring-integration       "
				  + "\n                                                         "
				  + "\n=========================================================" );

		final AbstractApplicationContext context =
				new ClassPathXmlApplicationContext("classpath:META-INF/spring/integration/expression-advice-context.xml");

		context.registerShutdownHook();

		@SuppressWarnings("unchecked")
		SessionFactory<FTPFile> sessionFactory = context.getBean(SessionFactory.class);
		SourcePollingChannelAdapter fileInbound = context.getBean(SourcePollingChannelAdapter.class);

		when(sessionFactory.getSession()).thenThrow(new RuntimeException("Force Failure"));
		fileInbound.start();

		LOGGER.info("\n========================================================="
				  + "\n                                                          "
				  + "\n    This is the Expression Advice Sample -                "
				  + "\n                                                          "
				  + "\n    Press 'Enter' to terminate.                           "
				  + "\n                                                          "
				  + "\n    Place a file in "+ System.getProperty("java.io.tmpdir") +"/adviceDemo ending   "
				  + "\n    with .txt                                             "
				  + "\n    The demo simulates a file transfer failure followed   "
				  + "\n    by the Advice renaming the file; the result of the    "
				  + "\n    rename is logged.                                     "
				  + "\n                                                          "
				  + "\n=========================================================" );

		System.in.read();
		context.close();
		System.exit(0);
	}
}
