/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
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
		LOGGER.info("""
				
				=========================================================
				                                                         
				          Welcome to Spring Integration!                 
				                                                         
				    For more information please visit:                   
				    https://www.springsource.org/spring-integration       
				                                                         
				=========================================================""");

		final AbstractApplicationContext context =
				new ClassPathXmlApplicationContext("classpath:META-INF/spring/integration/expression-advice-context.xml");

		context.registerShutdownHook();

		@SuppressWarnings("unchecked")
		SessionFactory<FTPFile> sessionFactory = context.getBean(SessionFactory.class);
		SourcePollingChannelAdapter fileInbound = context.getBean(SourcePollingChannelAdapter.class);

		when(sessionFactory.getSession()).thenThrow(new RuntimeException("Force Failure"));
		fileInbound.start();

		LOGGER.info("""
				
				=========================================================
				                                                          
				    This is the Expression Advice Sample -                
				                                                          
				    Press 'Enter' to terminate.                           
				                                                          
				    Place a file in """ + System.getProperty("java.io.tmpdir") + """
				/adviceDemo ending
				    with .txt
				    The demo simulates a file transfer failure followed
				    by the Advice renaming the file; the result of the
				    rename is logged.
				                                                          
				=========================================================""");

		System.in.read();
		context.close();
		System.exit(0);
	}
}
