/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 */

package org.springframework.integration.samples.amqp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Starts the Spring Context and will initialize the Spring Integration message flow.
 *
 * @author Gary Russell.
 * @since 4.0
 *
 */
public final class SamplePubConfirmsReturns {

	private static final Log LOGGER = LogFactory.getLog(SamplePubConfirmsReturns.class);

	private SamplePubConfirmsReturns() { }

	/**
	 * Load the Spring Integration Application Context
	 *
	 * @param args - command line arguments
	 */
	public static void main(final String... args) {

		LOGGER.info("""
				
				=========================================================
				                                                         
				          Welcome to Spring Integration!                 
				                                                         
				    For more information please visit:                   
				    https://www.springsource.org/spring-integration       
				                                                         
				=========================================================""");

		@SuppressWarnings("resource")
		final AbstractApplicationContext context =
				new ClassPathXmlApplicationContext("classpath:META-INF/spring/integration/spring-integration-confirms-context.xml");

		context.registerShutdownHook();

		LOGGER.info("""
				
				=========================================================
				                                                          
				    This is the AMQP Sample with confirms/returns -       
				                                                          
				    Please enter some text and press return. The entered  
				    Message will be sent to the configured RabbitMQ Queue,
				    then again immediately retrieved from the Message     
				    Broker and ultimately printed to the command line.    
				    Send 'fail' to demonstrate a return because the       
				    message couldn't be routed to a queue.                
				    Send 'nack' to demonstrate a NACK because the         
				    exchange doesn't exist, causing the channel to be     
				    closed in error by the broker.                        
				                                                          
				=========================================================""");

	}
}
