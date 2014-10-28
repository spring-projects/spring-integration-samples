/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.integration.samples.feed;

import org.junit.Test;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;

import com.rometools.rome.feed.synd.SyndEntry;

/**
 * @author Oleg Zhurakousky
 *
 */
public class FeedInboundChannelAdapterSample {

	@SuppressWarnings("unchecked")
	@Test
	public void runDemo(){
		ConfigurableApplicationContext ac =
			new ClassPathXmlApplicationContext("META-INF/spring/integration/FeedInboundChannelAdapterSample-context.xml");
		PollableChannel feedChannel = ac.getBean("feedChannel", PollableChannel.class);
		for (int i = 0; i < 10; i++) {
			Message<SyndEntry> message = (Message<SyndEntry>) feedChannel.receive(1000);
			if (message != null){
				SyndEntry entry = message.getPayload();
				System.out.println(entry.getPublishedDate() + " - " + entry.getTitle());
			}
			else {
				break;
			}
		}
		ac.close();
	}

}
