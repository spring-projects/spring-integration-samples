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
package org.springframework.integration.samples.dynamicftp;

import java.io.File;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Gary Russell
 * @author Amol Nayak
 * @author Artem Bilan
 *
 */
public class FtpOutboundChannelAdapterSampleTests {

	@Test
	public void runDemo() throws Exception {
		ConfigurableApplicationContext ctx =
				new ClassPathXmlApplicationContext(
						"META-INF/spring/integration/DynamicFtpOutboundChannelAdapterSample-context.xml");
		MessageChannel channel = ctx.getBean("toDynRouter", MessageChannel.class);
		File file = File.createTempFile("temp", "txt");
		Message<File> message = MessageBuilder.withPayload(file)
				.setHeader("customer", "cust1")
				.build();

		assertThatExceptionOfType(MessagingException.class)
				.isThrownBy(() -> channel.send(message))
				.withRootCauseInstanceOf(UnknownHostException.class)
				.withStackTraceContaining("host.for.cust1");

		// send another so we can see in the log we don't create the ac again.
		assertThatExceptionOfType(MessagingException.class)
				.isThrownBy(() -> channel.send(message))
				.withRootCauseInstanceOf(UnknownHostException.class)
				.withStackTraceContaining("host.for.cust1");

		// send to a different customer; again, check the log to see a new ac is built
		assertThatExceptionOfType(MessagingException.class)
				.isThrownBy(() -> channel.send(
						MessageBuilder.withPayload(file)
								.setHeader("customer", "cust2")
								.build()))
				.withRootCauseInstanceOf(UnknownHostException.class)
				.withStackTraceContaining("host.for.cust2");

		// send to a different customer; again, check the log to see a new ac is built
		//and the first one created (cust1) should be closed and removed as per the max cache size restriction
		assertThatExceptionOfType(MessagingException.class)
				.isThrownBy(() -> channel.send(
						MessageBuilder.withPayload(file)
								.setHeader("customer", "cust3")
								.build()))
				.withRootCauseInstanceOf(UnknownHostException.class)
				.withStackTraceContaining("host.for.cust3");

		//send to cust1 again, since this one has been invalidated before, we should
		//see a new ac created (with ac of cust2 destroyed and removed)
		assertThatExceptionOfType(MessagingException.class)
				.isThrownBy(() -> channel.send(
						MessageBuilder.withPayload(file)
								.setHeader("customer", "cust1")
								.build()))
				.withRootCauseInstanceOf(UnknownHostException.class)
				.withStackTraceContaining("host.for.cust1");

		ctx.close();
	}

}
