/*
 * Copyright 2002-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.samples.testing.externalgateway;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Shows how to stub-out external outbound-gateways with service activators
 * enabling isolated and repeatable testing.
 *
 * @author Gary Russell
 * @author Artem Bilan
 *
 * @since 2.0.2
 *
 */
@SpringJUnitConfig
public class ExternalGatewaySubstitutionTests {

	@Autowired
	WeatherAndTraffic weatherAndTraffic;

	@Test
	public void doTest() {
		List<String> results = this.weatherAndTraffic.getByZip("12345");
		Collections.sort(results);
		assertThat(results)
				.containsExactly("Dummy traffic for zip:12345", "Dummy weather for zip:12345");
	}

}
