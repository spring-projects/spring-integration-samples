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

package org.springframework.integration;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.service.CustomerService;

/**
 * Verify that the Spring Integration Application Context starts successfully.
 *
 * @author Gunnar Hillert
 * @since 1.0
 *
 */
public class CustomerServiceTest {

	@Test
	public void testStartupOfSpringInegrationContext() throws Exception{
		new ClassPathXmlApplicationContext("/META-INF/spring/integration/spring-integration-context.xml",
											CustomerServiceTest.class);
		Thread.sleep(2000);
	}

	@Test
	public void testExtractCustomerNames() {
		final ApplicationContext context
			= new ClassPathXmlApplicationContext("/META-INF/spring/integration/spring-integration-context.xml",
												CustomerServiceTest.class);

		final CustomerService service = context.getBean(CustomerService.class);

		final String sourceXml = "<customers><customer id=\"1\"><name>Foo Industries</name>"
				+ "<industry>Chemical</industry><city>Glowing City</city></customer></customers>";
		final String expectedResult  = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			+ "<customers>\n"
			+ "   <name>Foo Industries</name>\n"
			+ "</customers>\n";

		final String extractedCustomerNames = service.getCustomerNames(sourceXml);

		System.out.println(">>" + expectedResult + "<<");
		System.out.println(">>" + extractedCustomerNames + "<<");

		Assert.assertEquals("The expected list of customer names did not match.",
					expectedResult, extractedCustomerNames);

	}

}
