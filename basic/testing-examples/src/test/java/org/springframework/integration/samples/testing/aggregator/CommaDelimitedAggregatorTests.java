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
package org.springframework.integration.samples.testing.aggregator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.springframework.integration.test.matcher.PayloadMatcher.hasPayload;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.samples.testing.splitter.CommaDelimitedSplitter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * 
 * Shows how to test a custom aggregator. Unit test for the class and
 * tests for the integration subflow.
 * The subflow has direct input and output channels. The flow would
 * be a fragment of a larger flow. Since the output channel is direct,
 * it has no subscribers outside the context of a larger flow. So, 
 * in this test case, we bridge it to a {@link QueueChannel} to
 * facilitate easy testing.
 * 
 * @author Gary Russell
 * @since 2.0.2
 *
 */
@ContextConfiguration	// default context name is <ClassName>-context.xml
@RunWith(SpringJUnit4ClassRunner.class)
public class CommaDelimitedAggregatorTests {
	
	@Autowired
	MessageChannel inputChannel;
	
	@Autowired
	QueueChannel testChannel;

	@Test
	public void unitTestClass3() {
		List<String> splits = new CommaDelimitedSplitter().split("   a    , b,    c ");
		String out = new CommaDelimitedAggregator().aggregate(splits);
		assertEquals("a,b,c", out);
	}
	
	@Test
	public void unitTestClass2() {
		List<String> splits = new CommaDelimitedSplitter().split("   a    ,,    c ");
		String out = new CommaDelimitedAggregator().aggregate(splits);
		assertEquals("a,c", out);
	}

	@Test
	public void unitTestClass0() {
		List<String> splits = new CommaDelimitedSplitter().split(",,, ,,    ,,  ,,");
		String out = new CommaDelimitedAggregator().aggregate(splits);
		assertNull(out);
	}

	@Test
	public void testOne() {
		inputChannel.send(MessageBuilder.withPayload("   a   ").build());
		Message<?> outMessage = testChannel.receive(0);
		assertNotNull(outMessage);
		assertThat(outMessage, hasPayload("A"));
		outMessage = testChannel.receive(0);
		assertNull("Only one message expected", outMessage);
	}

	@Test
	public void testTwo() {
		inputChannel.send(MessageBuilder.withPayload("   a ,z  ").build());
		Message<?> outMessage = testChannel.receive(0);
		assertNotNull(outMessage);
		assertThat(outMessage, hasPayload("A,Z"));
		outMessage = testChannel.receive(0);
		assertNull("Only one message expected", outMessage);
	}

	@Test
	public void testSkipEmpty() {
		inputChannel.send(MessageBuilder.withPayload("   a ,,z  ").build());
		Message<?> outMessage = testChannel.receive(0);
		assertNotNull(outMessage);
		assertThat(outMessage, hasPayload("A,Z"));
		outMessage = testChannel.receive(0);
		assertNull("Only one message expected", outMessage);
	}

	@Test
	public void testNone() {
		inputChannel.send(MessageBuilder.withPayload("  ,, ,,, ,,,,, ,,,,,,,  ").build());
		Message<?> outMessage = testChannel.receive(0);
		assertNull("No messages expected", outMessage);
	}

	@Test
	public void testEmpty() {
		inputChannel.send(MessageBuilder.withPayload("").build());
		Message<?> outMessage = testChannel.receive(0);
		assertNull("No messages expected", outMessage);
	}
}
