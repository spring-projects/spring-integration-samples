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
public class StatefulRetryDemo {

	private static final Log LOGGER = LogFactory.getLog(StatefulRetryDemo.class);

	public static void main(String[] args) throws Exception {
		LOGGER.info("""
				
				=========================================================
				                                                         
				          Welcome to Spring Integration!                 
				                                                         
				    For more information please visit:                   
				    https://www.springsource.org/spring-integration       
				                                                         
				=========================================================""");

		final AbstractApplicationContext context =
				new ClassPathXmlApplicationContext("classpath:META-INF/spring/integration/stateful-retry-advice-context.xml");

		context.registerShutdownHook();

		LOGGER.info("""
				
				=========================================================
				                                                          
				    This is the Stateful Sample -
				                                                          
				    Please enter some text and press return.
				    'fail 2' will fail twice, then succeed
				    'fail 3' will fail and never succeed
				    Demo will terminate in 60 seconds.
				                                                          
				=========================================================""");

		Thread.sleep(60000);
		context.close();
	}

}
