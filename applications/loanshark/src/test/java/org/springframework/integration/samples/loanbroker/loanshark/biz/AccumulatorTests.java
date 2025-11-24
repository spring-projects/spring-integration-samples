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

package org.springframework.integration.samples.loanbroker.loanshark.biz;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.samples.loanbroker.loanshark.domain.LoanShark;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Gary Russell
 * @author Artem Bilan
 */
@SpringJUnitConfig(locations = "classpath:META-INF/spring/applicationContext.xml")
@DirtiesContext
@Transactional
@Disabled("Not clear what is going on with @Configurable")
public class AccumulatorTests {

	@Autowired
	Accumulator accumulator;

	@Test
	public void test() {
		accumulator.accumulate(new SharkQuote("fred", 6.0d));
		accumulator.accumulate(new SharkQuote("fred", 6.2d));
		LoanShark shark = (LoanShark) LoanShark.findLoanSharksByName("fred").getSingleResult();
		assertThat(shark.getCounter()).isEqualTo(2);
		assertThat(shark.getAverageRate()).isEqualTo(6.1);
	}

}
