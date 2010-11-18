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

import java.io.File;

import org.junit.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

/**
 * @author Oleg Zhurakousky
 *
 */
public class FtpOutboundChannelAdapterSample {

	@Test
	public void runDemo() throws Exception{
		ApplicationContext ac = 
			new ClassPathXmlApplicationContext("META-INF/spring/integration/FtpOutboundChannelAdapterSample-context.xml");
		MessageChannel ftpChannel = ac.getBean("ftpChannel", MessageChannel.class);
		File file = new File("readme.txt");
		if (file.exists()){
			Message<File> message = MessageBuilder.withPayload(file).build();
			ftpChannel.send(message);
			Thread.sleep(2000);
		}
		if (new File("remote-target-dir/readme.txt").exists()){
			System.out.println("Successfully transfered 'readme.txt' file to a remote location under the name 'readme.txt'");
		}
	}
}
