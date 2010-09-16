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

package org.springframework.integration.samples.errorhandling;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates the handling of Exceptions in an asynchronous messaging
 * environment. View the 'errorHandlingDemo.xml' configuration file within
 * this same package. Notice the use of a &lt;header-enricher/&gt; element
 * within a &lt;chain/&gt; that establishes an 'error-channel' reference
 * prior to passing the message to a &lt;service-activator/&gt;.
 *
 * @author Iwein Fuld
 */
public class PartyDemoTest {
	@Test
	public void runPartyDemoTest() throws Exception{
		new ClassPathXmlApplicationContext("/META-INF/spring/integration/errorHandlingDemo.xml", PartyDemoTest.class);
		Thread.sleep(5000);
	}
}
