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
public class CircuitBreakerDemo {

	private static final Log LOGGER = LogFactory.getLog(CircuitBreakerDemo.class);

	public static void main(String[] args) throws Exception {
		LOGGER.info("""
				
				=========================================================
				                                                         
				          Welcome to Spring Integration!                 
				                                                         
				    For more information please visit:                   
				    https://www.springsource.org/spring-integration       
				                                                         
				=========================================================""");

		final AbstractApplicationContext context =
				new ClassPathXmlApplicationContext("classpath:META-INF/spring/integration/circuit-breaker-advice-context.xml");

		context.registerShutdownHook();

		LOGGER.info("""
				
				=========================================================
				                                                          
				    This is the Circuit Breaker Sample -                  
				                                                          
				    Please enter some text and press return a few times.  
				    Service will succeed only in the last quarter         
				    minute. Breaker will open after 2 failures and        
				    will go half-open after 15 seconds.                   
				    Demo will terminate in 2 minutes.                     
				                                                          
				=========================================================""");

		Thread.sleep(120000);
		context.close();
	}

}
