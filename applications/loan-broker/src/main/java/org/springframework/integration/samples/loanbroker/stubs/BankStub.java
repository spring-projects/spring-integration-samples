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

package org.springframework.integration.samples.loanbroker.stubs;

import java.util.Calendar;
import java.util.Random;

import org.springframework.integration.samples.loanbroker.domain.LoanQuote;
import org.springframework.integration.samples.loanbroker.domain.LoanRequest;

/**
 * @author Oleg Zhurakousky
 */
public class BankStub {

	private volatile String name;

	private float baseRate = 6.0f;

	public void setName(String name) {
		this.name = name;
	}

	public void setBaseRate(float baseRate) {
		this.baseRate = baseRate;
	}

	/**
	 * @param loanRequest the loan request
	 * @return a LoanQuote for the given request
	 */
	public LoanQuote quote(LoanRequest loanRequest) {
		Calendar calendar = Calendar.getInstance();
		LoanQuote loanQuote = new LoanQuote();
		Random random = new Random();
		loanQuote.setQuoteDate(calendar.getTime());
		calendar.add(Calendar.DAY_OF_YEAR, random.nextInt(25));
		loanQuote.setExpirationDate(calendar.getTime());
		loanQuote.setRate(random.nextFloat() + this.baseRate);
		loanQuote.setTerm(10 + random.nextInt(10));
		loanQuote.setAmount(250000 + random.nextInt(40000));
		loanQuote.setLender(this.name);
		return loanQuote;
	}

}
