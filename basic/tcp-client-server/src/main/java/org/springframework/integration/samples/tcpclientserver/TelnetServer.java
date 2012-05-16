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
package org.springframework.integration.samples.tcpclientserver;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.MapPropertySource;

/**
 * The configured inbound gateway uses CRLF delimited messages which means
 * it works fine as a very simple Telnet server - connect using
 * telnet localhost 11111 - each time you hit enter you should see your input
 * echoed back, preceded by 'echo:'.
 *
 * Alternatively, you can also customize the port by providing an additional system
 * property at startup e.g. <i>-DavailableServerSocket=7777</i>
 *
 * @author Gary Russell
 * @author Gunnar Hillert
 *
 */
public class TelnetServer {

	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"/META-INF/spring/integration/tcpClientServerDemo-context.xml"}, false);

		final Map<String, Object> sockets = new HashMap<String, Object>();
		sockets.put("availableServerSocket", 11111);

		final MapPropertySource propertySource = new MapPropertySource("sockets", sockets);

		context.getEnvironment().getPropertySources().addLast(propertySource);
		context.refresh();

		System.out.println("Use telnet and connect to port: " + context.getEnvironment().getProperty("availableServerSocket"));
		System.out.println("Press Enter/Return in the console to exit");
		System.in.read();
		System.out.println("exiting application...bye.\n\n");
		System.exit(0);
	}

}
