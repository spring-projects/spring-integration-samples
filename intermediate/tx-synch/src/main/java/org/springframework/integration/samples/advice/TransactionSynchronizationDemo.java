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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Gary Russell
 * @since 2.2
 *
 */
public class TransactionSynchronizationDemo {

	private static final Log LOGGER = LogFactory.getLog(TransactionSynchronizationDemo.class);

	public static void main(String[] args) throws Exception {
		LOGGER.info("""
				
				=========================================================
				                                                         
				          Welcome to Spring Integration!                 
				                                                         
				    For more information please visit:                   
				    https://www.springsource.org/spring-integration       
				                                                         
				=========================================================""");

		final AbstractApplicationContext context =
				new ClassPathXmlApplicationContext("classpath:META-INF/spring/integration/transaction-synch-context.xml");

		context.registerShutdownHook();

		LOGGER.info("""
				
				=========================================================
				                                                          
				    This is the Transaction Synchronization Sample -
				                                                          
				    Please press 'Enter' to terminate.
				                                                          
				    Place a file in """ + System.getProperty("java.io.tmpdir") + """
				/txSynchDemo ending
				    with .txt
				    If the first line begins with 'fail' the transaction
				    transaction will be rolled back.The result of the
				    expression evaluation is logged.
				                                                          
				=========================================================""");
		System.out.println(System.getProperty("java.io.tmpdir") + "/txSynchDemo");
		System.in.read();
		context.close();
	}

}
