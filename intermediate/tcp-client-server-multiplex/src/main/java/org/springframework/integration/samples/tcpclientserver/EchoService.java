/*
 * Copyright 2002-2015 the original author or authors.
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

import org.springframework.integration.MessageTimeoutException;

/**
 * Simple service that receives data in a byte array,
 * converts it to a String and appends it with ':echo'.
 *
 * @author Gary Russell
 * @since 2.1
 *
 */
public class EchoService {

	public String test(String input) throws InterruptedException {
		if ("FAIL".equals(input)) {
			throw new RuntimeException("Failure Demonstration");
		}
		else if(input.startsWith("TIMEOUT_TEST")) {
			Thread.sleep(3000);
		}

		return input + ":echo";
	}

	public MessageTimeoutException noResponse(String input) {
		if ("TIMEOUT_TEST_THROW".equals(input)) {
			throw new MessageTimeoutException("No response received for " + input);
		}
		else {
			return new MessageTimeoutException("No response received for " + input);
		}
	}

}
