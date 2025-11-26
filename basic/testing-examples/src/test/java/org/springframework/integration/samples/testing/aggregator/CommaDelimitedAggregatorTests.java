/*
 * Copyright 2002-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.samples.testing.aggregator;

import java.util.List;

import org.assertj.core.api.HamcrestCondition;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.samples.testing.splitter.CommaDelimitedSplitter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.test.matcher.PayloadMatcher;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

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
 * @author Artem Bilan
 *
 * @since 2.0.2
 *
 */
@SpringJUnitConfig
@DirtiesContext
public class CommaDelimitedAggregatorTests {

	@Autowired
	MessageChannel inputChannel;

	@Autowired
	QueueChannel testChannel;

	@Test
	public void unitTestClass3() {
		List<String> splits = new CommaDelimitedSplitter().split("   a    , b,    c ");
		String out = new CommaDelimitedAggregator().aggregate(splits);
		assertThat(out).isEqualTo("a,b,c");
	}

	@Test
	public void unitTestClass2() {
		List<String> splits = new CommaDelimitedSplitter().split("   a    ,,    c ");
		String out = new CommaDelimitedAggregator().aggregate(splits);
		assertThat(out).isEqualTo("a,c");
	}

	@Test
	public void unitTestClass0() {
		List<String> splits = new CommaDelimitedSplitter().split(",,, ,,    ,,  ,,");
		String out = new CommaDelimitedAggregator().aggregate(splits);
		assertThat(out).isNull();
	}

	@Test
	public void testOne() {
		inputChannel.send(MessageBuilder.withPayload("   a   ").build());
		Message<?> outMessage = testChannel.receive(0);
		assertThat(outMessage).is(new HamcrestCondition<>(PayloadMatcher.hasPayload("A")));
		outMessage = testChannel.receive(0);
		assertThat(outMessage).isNull();
	}

	@Test
	public void testTwo() {
		inputChannel.send(MessageBuilder.withPayload("   a ,z  ").build());
		Message<?> outMessage = testChannel.receive(0);
		assertThat(outMessage).is(new HamcrestCondition<>(PayloadMatcher.hasPayload("A,Z")));
		outMessage = testChannel.receive(0);
		assertThat(outMessage).isNull();
	}

	@Test
	public void testSkipEmpty() {
		inputChannel.send(MessageBuilder.withPayload("   a ,,z  ").build());
		Message<?> outMessage = testChannel.receive(0);
		assertThat(outMessage).is(new HamcrestCondition<>(PayloadMatcher.hasPayload("A,Z")));
		outMessage = testChannel.receive(0);
		assertThat(outMessage).isNull();
	}

	@Test
	public void testNone() {
		inputChannel.send(MessageBuilder.withPayload("  ,, ,,, ,,,,, ,,,,,,,  ").build());
		Message<?> outMessage = testChannel.receive(0);
		assertThat(outMessage).isNull();
	}

	@Test
	public void testEmpty() {
		inputChannel.send(MessageBuilder.withPayload("").build());
		Message<?> outMessage = testChannel.receive(0);
		assertThat(outMessage).isNull();
	}

}
