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
package org.springframework.integration.samples.travel;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * This test will test the weather service (does not require an API key).
 *
 * @author Gunnar Hillert
 * @since 2.2
 *
 */
@ContextConfiguration({
	"classpath:META-INF/spring/integration-context.xml",
	"classpath:META-INF/spring/integration-ws-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TravelGatewayTest {

	private static final Logger LOGGER = Logger.getLogger(TravelGatewayTest.class);

	@Autowired
	private TravelGateway travelGateway;

	@Test
	public void testGetWeatherByCity() {
		final String weatherInformation = travelGateway.getWeatherByCity(City.ATLANTA);
		LOGGER.info("Weather information for Atlanta:\n\n" + weatherInformation + "\n\n");
		Assert.assertNotNull(weatherInformation);
	}

}
