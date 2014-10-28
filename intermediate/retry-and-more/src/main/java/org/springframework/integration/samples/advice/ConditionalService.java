/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.integration.samples.advice;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.messaging.handler.annotation.Header;

/**
 * @author Gary Russell
 * @since 2.2
 *
 */
public class ConditionalService {

	private final Log logger = LogFactory.getLog(this.getClass());

	private final Map<String, AtomicInteger> failCount = new HashMap<String, AtomicInteger>();

	/**
	 * If this service receives a payload 'failnnn' where nnn is the number of failures,
	 * it will fail that many times for a given message id.
	 * @param payload
	 * @param id
	 */
	public void testRetry(String payload, @Header("failingId") String id) {
		if (payload.startsWith("fail")) {
			int failHowManyTimes = Integer.parseInt(payload.substring(4).trim());
			AtomicInteger failures = failCount.get(id);
			if (failures == null) {
				failures = new AtomicInteger();
				failCount.put(id, failures);
			}
			int currentFailures = failures.incrementAndGet();
			if (currentFailures <= failHowManyTimes) {
				String message = "Failure " + currentFailures + " of " + failHowManyTimes;
				logger.info("Service failure " + message);
				throw new RuntimeException(message);
			}
		}
		logger.info("Service success for " + payload);
		failCount.remove(id);
	}

	/**
	 * Succeeds only if called any time in the fourth quarter of any minute (seconds 45 thru 59)
	 * @param payload
	 */
	public void testCircuitBreaker(String payload) {
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.SECOND) < 45) {
			logger.info("Service failure");
			throw new RuntimeException("Service failed");
		}
		logger.info("Service success for " + payload);
	}

}
