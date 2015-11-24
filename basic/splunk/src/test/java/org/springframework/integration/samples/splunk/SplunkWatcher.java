/*
 * Copyright 2015 the original author or authors.
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

package org.springframework.integration.samples.splunk;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assume;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.integration.splunk.support.SplunkServer;
import org.springframework.integration.splunk.support.SplunkServiceFactory;

/**
 * @author fbalicchia
 * @since 4.2
 */
public class SplunkWatcher extends TestWatcher {

	private static final Log logger = LogFactory.getLog(SplunkWatcher.class);
	
	@Override
	public Statement apply(Statement base, Description description) {
		try {
			
			SplunkServer splunkServer = new SplunkServer(); 
			splunkServer.setPassword("admin");
			splunkServer.setUsername("admin");
			SplunkServiceFactory splunkServiceFactory = new SplunkServiceFactory(splunkServer);
			splunkServiceFactory.getService().open(splunkServer.getPort());
		} catch (Exception e) {
			logger.warn("Not executing tests because basic connectivity test failed");
			Assume.assumeNoException(e);
		}

		return super.apply(base, description);
	}

}
