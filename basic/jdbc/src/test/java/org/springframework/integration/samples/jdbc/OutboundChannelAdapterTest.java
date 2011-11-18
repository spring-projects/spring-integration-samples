/*
 * Copyright 2002-2011 the original author or authors.
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
package org.springframework.integration.samples.jdbc;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The test class for jdbc outbound channel adapter
 * @author Amol Nayak
 *
 */
@ContextConfiguration("classpath:jdbcOutboundAdapterConfig.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class OutboundChannelAdapterTest {

	private Logger logger = Logger.getLogger(OutboundChannelAdapterTest.class);
	@Autowired
	@Qualifier("outboundJdbcChannelOne")
	private MessageChannel channel;
	
	@Test
	public void insertPersonRecord() {
		
		logger.info("Creating person Instance");
		Person person = new Person();
		Calendar dateOfBirth = Calendar.getInstance();
		dateOfBirth.set(1980, 0, 1);
		person.setDateOfBirth(dateOfBirth.getTime());
		person.setName("Name Of The Person");
		person.setGender(Gender.MALE);
		Message<Person> message = MessageBuilder.withPayload(person).build();
		channel.send(message);
		logger.info("Sent person Instance");
		
	}
	
}
