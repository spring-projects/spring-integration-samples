/*
 * Copyright 2002-2012 the original author or authors.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.UnknownHostException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.MessageHandlingException;
import org.springframework.integration.MessagingException;
import org.springframework.integration.support.MessageBuilder;

/**
 * @author Gary Russell
 * @author Amol Nayak
 *
 */
public class FtpOutboundChannelAdapterSample {

	@Test
	public void runDemo() throws Exception{
		ApplicationContext ctx =
			new ClassPathXmlApplicationContext("META-INF/spring/integration/DynamicFtpOutboundChannelAdapterSample-context.xml");
		MessageChannel channel = ctx.getBean("toDynRouter", MessageChannel.class);
		File file = File.createTempFile("temp", "txt");
		Message<File> message = MessageBuilder.withPayload(file)
						.setHeader("customer", "cust1")
						.build();
		try {
			channel.send(message);
		} catch (MessagingException e) {
			assertTrue(e.getCause().getCause() instanceof UnknownHostException);
			assertEquals("host.for.cust1", e.getCause().getCause().getMessage());
		}
		// send another so we can see in the log we don't create the ac again.
		try {
			channel.send(message);
		} catch (MessagingException e) {
			assertTrue(e.getCause().getCause() instanceof UnknownHostException);
			assertEquals("host.for.cust1", e.getCause().getCause().getMessage());
		}
		// send to a different customer; again, check the log to see a new ac is built
		message = MessageBuilder.withPayload(file)
				.setHeader("customer", "cust2").build();
		try {
			channel.send(message);
		} catch (MessagingException e) {
			assertTrue(e.getCause().getCause() instanceof UnknownHostException);
			assertEquals("host.for.cust2", e.getCause().getCause().getMessage());
		}
		
		// send to a different customer; again, check the log to see a new ac is built 
		//and the first one created (cust1) should be closed and removed as per the max cache size restriction
		message = MessageBuilder.withPayload(file)
				.setHeader("customer", "cust3").build();
		try {
			channel.send(message);
		} catch (MessagingException e) {
			assertTrue(e.getCause().getCause() instanceof UnknownHostException);
			assertEquals("host.for.cust3", e.getCause().getCause().getMessage());
		}
		
		//send to cust1 again, since this one has been invalidated before, we should 
		//see a new ac created (with ac of cust2 destroyed and removed)
		message = MessageBuilder.withPayload(file)
				.setHeader("customer", "cust1").build();
		try {
			channel.send(message);
		} catch (MessagingException e) {
			assertTrue(e.getCause().getCause() instanceof UnknownHostException);
			assertEquals("host.for.cust1", e.getCause().getCause().getMessage());
		}
	}
}
