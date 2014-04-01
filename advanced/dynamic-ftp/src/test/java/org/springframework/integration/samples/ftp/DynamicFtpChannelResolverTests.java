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
package org.springframework.integration.samples.ftp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.messaging.MessageChannel;

/**
 * @author Gary Russell
 * @since 2.1
 *
 */
public class DynamicFtpChannelResolverTests {

	/**
	 * Test method for {@link org.springframework.integration.samples.ftp.DynamicFtpChannelResolver#resolve(java.lang.String)}.
	 */
	@Test
	public void testResolve() {
		DynamicFtpChannelResolver dynamicFtpChannelResolver = new DynamicFtpChannelResolver();
		MessageChannel channel1 = dynamicFtpChannelResolver.resolve("customer1");
		assertNotNull(channel1);
		MessageChannel channel2 = dynamicFtpChannelResolver.resolve("customer2");
		assertNotNull(channel2);
		assertNotSame(channel1, channel2);
		MessageChannel channel1a = dynamicFtpChannelResolver.resolve("customer1");
		assertSame(channel1, channel1a);
	}

}
