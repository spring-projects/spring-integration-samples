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
package org.springframework.integration.samples.loanbroker.loanshark.biz;


import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.samples.loanbroker.loanshark.domain.LoanShark;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;



/**
 * @author Gary Russell
 *
 */
@ContextConfiguration(locations="classpath:META-INF/spring/applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class AccumulatorTests {

	@Autowired
	Accumulator accumulator;

	@Test
	@Transactional
	@Ignore
	public void test() {
		accumulator.accumulate(new SharkQuote("fred", 6.0d));
		accumulator.accumulate(new SharkQuote("fred", 6.2d));
		LoanShark shark = (LoanShark) LoanShark.findLoanSharksByName("fred").getSingleResult();
		assertEquals(new Long(2), shark.getCounter());
		assertEquals(new Double(6.1), shark.getAverageRate());
	}
}
