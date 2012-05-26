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
package org.springframework.integration.samples.tcpclientserver.support;

import static org.junit.Assert.fail;

import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;

/**
 * @author Gary Russell
 *
 */
public class ServerUtils {

	public static void waitListening(AbstractServerConnectionFactory serverConnectionFactory) {
		int n = 0;
		while (!serverConnectionFactory.isListening()) {

			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
				throw new IllegalStateException(e1);
			}

			if (n++ > 100) {
				fail("Server didn't begin listening.");
			}
		}
	}

}
