/*
 * Copyright 2002-2010 the original author or authors.
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

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The configured inbound gateway uses CRLF delimited messages which means
 * it works fine as a very simple Telnet server - connect using
 * telnet localhost 11111 - each time you hit enter you should see your input
 * echoed back, preceded by 'echo:'.
 * 
 * @author Gary Russell
 *
 */
public class TelnetServer {

	public static void main(String[] args) throws Exception {
		new ClassPathXmlApplicationContext("/META-INF/spring/integration/tcpClientServerDemo-context.xml");
		System.out.println("Press Enter/Return in the console to exit");
		System.in.read();
		System.exit(0);
	}

}
