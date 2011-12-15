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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.integration.MessageChannel;

/**
 * Demonstrates how a dynamic Spring Integration flow snippet can be used
 * to send files to dynamic destinations.
 *
 * @author Gary Russell
 * @since 2.1
 *
 */
public class DynamicFtpChannelResolver {

	private final Map<String, MessageChannel> channels = new HashMap<String, MessageChannel>();

	/**
	 * Resolve a customer to a channel, where each customer gets a private
	 * application context and the channel is the inbound channel to that
	 * application context.
	 *
	 * @param customer
	 * @return a channel
	 */
	public MessageChannel resolve(String customer) {
		MessageChannel channel = this.channels.get(customer);
		if (channel == null) {
			ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext(
					new String[] { "/META-INF/spring/integration/dynamic-ftp-outbound-adapter-context.xml" },
					false);
			this.setEnvironmentForCustomer(ctx, customer);
			ctx.refresh();
			channel = ctx.getBean("toFtpChannel", MessageChannel.class);
			this.channels.put(customer, channel);
		}
		return channel;
	}

	/**
	 * Use Spring 3.1. environment support to set properties for the
	 * customer-specific application context.
	 *
	 * @param ctx
	 * @param customer
	 */
	private void setEnvironmentForCustomer(ConfigurableApplicationContext ctx,
			String customer) {
		StandardEnvironment env = new StandardEnvironment();
		Properties props = new Properties();
		// populate properties for customer
		props.setProperty("host", "host.for." + customer);
		props.setProperty("user", "user");
		props.setProperty("password", "password");
		props.setProperty("remote.directory", "/tmp");
		PropertiesPropertySource pps = new PropertiesPropertySource("ftpprops", props);
		env.getPropertySources().addLast(pps);
		ctx.setEnvironment(env);
	}
}
