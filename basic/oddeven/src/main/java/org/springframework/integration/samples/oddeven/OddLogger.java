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

package org.springframework.integration.samples.oddeven;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

/**
 * A POJO Service Activator that logs odd numbers and the current time.
 *
 * @author Mark Fisher
 * @author Marius Bogoevici
 * @author Gary Russell
 */
@MessageEndpoint
public class OddLogger {
	private static Log logger = LogFactory.getLog(OddLogger.class);

	@ServiceActivator
	public void log(int i) {
		logger.info("odd:  " + i + " at " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
	}

}
