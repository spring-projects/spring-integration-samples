/*
 * Copyright 2002-2011 the original author or authors.
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

import org.junit.jupiter.api.Test;

import org.springframework.integration.samples.dynamicftp.DynamicFtpChannelResolver;
import org.springframework.messaging.MessageChannel;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Gary Russell
 * @since 2.1
 *
 */
public class DynamicFtpChannelResolverTests {

	/**
	 * Test method for {@link DynamicFtpChannelResolver#resolve(java.lang.String)}.
	 */
	@Test
	public void testResolve() {
		DynamicFtpChannelResolver dynamicFtpChannelResolver = new DynamicFtpChannelResolver();
		MessageChannel channel1 = dynamicFtpChannelResolver.resolve("customer1");
		assertThat(channel1).isNotNull();
		MessageChannel channel2 = dynamicFtpChannelResolver.resolve("customer2");
		assertThat(channel1).isNotSameAs(channel2);
		MessageChannel channel1a = dynamicFtpChannelResolver.resolve("customer1");
		assertThat(channel1).isSameAs(channel1a);
	}

}
