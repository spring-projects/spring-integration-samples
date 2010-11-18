/*
 * Copyright 2002-2008 the original author or authors.
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
package org.springframework.integration.samples.sftp;

import org.junit.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.core.PollableChannel;

/**
 * @author Oleg Zhurakousky
 *
 */
public class SftpInboundReceiveSample {

	@Test
	public void runDemo(){
		ApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/integration/SftpInboundReceiveSample-context.xml", this.getClass());
		PollableChannel localFileChannel = context.getBean("receiveChannel", PollableChannel.class);
		System.out.println("Received first file message: " + localFileChannel.receive());
		System.out.println("Received second file message: " + localFileChannel.receive());
		System.out.println("No third file was received " + localFileChannel.receive(1000));
	}
}
