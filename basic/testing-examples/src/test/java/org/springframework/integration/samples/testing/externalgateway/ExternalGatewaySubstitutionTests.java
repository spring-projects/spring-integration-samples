/*
 * Copyright 2002-2011 the original author or authors.
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
package org.springframework.integration.samples.testing.externalgateway;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Shows how to stub-out external outbound-gateways with service activators
 * enabling isolated and repeatable testing.
 * 
 * @author Gary Russell
 * @since 2.0.2
 *
 */
@ContextConfiguration // default context name is <ClassName>-context.xml
@RunWith(SpringJUnit4ClassRunner.class)
public class ExternalGatewaySubstitutionTests {
	
	@Autowired
	WeatherAndTraffic weatherAndTraffic;
	
	@Test
	public void doTest() {
		List<String> results = weatherAndTraffic.getByZip("12345");
		assertEquals(2, results.size());
		Collections.sort(results);
		Iterator<String> result = results.iterator();
		assertEquals("Dummy traffic for zip:12345", result.next());
		assertEquals("Dummy weather for zip:12345", result.next());
	}

}
