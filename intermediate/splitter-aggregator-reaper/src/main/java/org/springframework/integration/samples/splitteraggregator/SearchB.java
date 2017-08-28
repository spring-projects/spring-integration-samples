/*
 * Copyright 2002-2017 the original author or authors.
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
package org.springframework.integration.samples.splitteraggregator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.integration.samples.splitteraggregator.support.CriteriaB;

/**
 * Another type of search.
 *
 * @author Christopher Hunt
 * @author Gary Russell
 *
 */
public class SearchB {

	private static final Log LOGGER = LogFactory.getLog(SearchB.class);

	private long executionTime = 1000L;

	public Result search(CriteriaB criteria) {

		LOGGER.info(String.format("This search will take %sms.", executionTime));

		try {
			Thread.sleep(executionTime);
		} catch (InterruptedException e) {
		}
		return new Result();
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}
}
