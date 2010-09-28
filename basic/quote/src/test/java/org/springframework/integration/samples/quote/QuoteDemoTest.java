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

package org.springframework.integration.samples.quote;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates a method-invoking inbound Channel Adapter and an
 * outbound Channel Adapter that writes to stdout. Between them
 * is an annotated Service Activator. See QuoteService and the
 * 'quoteDemo.xml' configuration file for more detail.
 * 
 * @author Mark Fisher
 */
public class QuoteDemoTest {

	@Test
	public  void testDemo()  throws Exception{
		ClassPathXmlApplicationContext context = 
			new ClassPathXmlApplicationContext("/META-INF/spring/integration/quoteDemo.xml", QuoteDemoTest.class);
		Thread.sleep(5000);
		context.close();
	}
}
