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

package org.springintegration;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Run this class to run the sample from the command line.
 *
 * @author Gary Russell
 */
public class SpringIntegrationTest {

	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext context =
				new ClassPathXmlApplicationContext("/META-INF/spring/integration/spring-integration-context.xml", SpringIntegrationTest.class);
		System.out.println("Hit Enter to terminate");
		System.in.read();
		context.close();
	}

}
