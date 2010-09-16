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

import java.util.Random;

import org.apache.log4j.Logger;

import org.springframework.integration.samples.loanbroker.domain.CreditScore;
import org.springframework.integration.samples.loanbroker.domain.LoanRequest;

/**
 * @author Oleg Zhurakousky
 */
public class CreditBureauStub {

	private static Logger logger = Logger.getLogger(CreditBureauStub.class);

	/**
	 * @param loanRequest the loan request
	 * @return the CreditScore for the given loan request
	 */
	public CreditScore getCreditScore(LoanRequest loanRequest){
		Random random =  new Random();
		int creditScore = 700 + random.nextInt(150);
		logger.info("Credit Score: " + creditScore);
		return new CreditScore(creditScore);
	}

}
