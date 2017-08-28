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
package org.springframework.integration.samples.advice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Gary Russell
 * @since 2.2
 *
 */
public class StatelessRetryDemo {

	private static final Log LOGGER = LogFactory.getLog(StatelessRetryDemo.class);

	public static void main(String[] args) throws Exception {
		LOGGER.info("\n========================================================="
				  + "\n                                                         "
				  + "\n          Welcome to Spring Integration!                 "
				  + "\n                                                         "
				  + "\n    For more information please visit:                   "
				  + "\n    http://www.springsource.org/spring-integration       "
				  + "\n                                                         "
				  + "\n=========================================================" );

		final AbstractApplicationContext context =
				new ClassPathXmlApplicationContext("classpath:META-INF/spring/integration/stateless-retry-advice-context.xml");

		context.registerShutdownHook();

		LOGGER.info("\n========================================================="
				  + "\n                                                          "
				  + "\n    This is the Stateless Sample -                        "
				  + "\n                                                          "
				  + "\n    Please enter some text and press return.              "
				  + "\n    'fail 2' will fail twice, then succeed                "
				  + "\n    'fail 3' will fail and never succeed                  "
				  + "\n    Demo will terminate in 60 seconds.                    "
				  + "\n                                                          "
				  + "\n=========================================================" );

		Thread.sleep(60000);
		context.close();
	}

}
