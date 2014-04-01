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
package org.springframework.integration.samples.twitter;

import org.junit.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

/**
 * @author Oleg Zhurakousky
 *
 */
public class TwitterSendUpdatesSample {

	@Test
	public void runDemo() throws Exception{
		ApplicationContext context = 
			new ClassPathXmlApplicationContext("META-INF/spring/integration/TwitterSendUpdates-context.xml");
		
		MessageChannel twitterOutChannel = context.getBean("twitterOut", MessageChannel.class);
		Message<String> twitterUpdate = new GenericMessage<String>("Testing new Twitter samples for #springintegration");
		twitterOutChannel.send(twitterUpdate);
	}
}
