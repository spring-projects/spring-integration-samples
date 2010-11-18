/*
 * Copyright 2002-2010 the original author or authors.
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

import org.junit.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.core.PollableChannel;

/**
 * @author Oleg Zhurakousky
 *
 */
public class FtpInboundChannelAdapterSample {

	@Test
	public void runDemo() throws Exception{
		ApplicationContext ac = 
			new ClassPathXmlApplicationContext("META-INF/spring/integration/FtpInboundChannelAdapterSample-context.xml");
		PollableChannel ftpChannel = ac.getBean("ftpChannel", PollableChannel.class);
		System.out.println("Received first file message: " + ftpChannel.receive(5000));
		System.out.println("Received scond file message: " + ftpChannel.receive(5000));
		System.out.println("Received nothing else: " + ftpChannel.receive(2000));
	}
}
