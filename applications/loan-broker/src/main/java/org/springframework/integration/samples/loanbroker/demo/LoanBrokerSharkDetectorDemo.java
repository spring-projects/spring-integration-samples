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

package org.springframework.integration.samples.loanbroker.demo;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.loanbroker.LoanBrokerGateway;
import org.springframework.integration.samples.loanbroker.domain.Customer;
import org.springframework.integration.samples.loanbroker.domain.LoanQuote;
import org.springframework.integration.samples.loanbroker.domain.LoanRequest;

/**
 * @author Gary Russell
 */
public final class LoanBrokerSharkDetectorDemo {

	private static final Log LOGGER = LogFactory.getLog(LoanBrokerSharkDetectorDemo.class);

	private LoanBrokerSharkDetectorDemo() {
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context =
				new ClassPathXmlApplicationContext("META-INF/spring/integration/bootstrap-config/stubbed-loan-broker-multicast.xml");
		LoanBrokerGateway broker = context.getBean("loanBrokerGateway", LoanBrokerGateway.class);
		LoanRequest loanRequest = new LoanRequest();
		loanRequest.setCustomer(new Customer());
		LoanQuote loan = broker.getBestLoanQuote(loanRequest);
		LOGGER.info("\n********* Best Quote: " + loan);
		List<LoanQuote> loanQuotes = broker.getAllLoanQuotes(loanRequest);
		LOGGER.info("\n********* All Quotes: ");
		for (LoanQuote loanQuote : loanQuotes) {
			LOGGER.info(loanQuote);
		}
		context.close();
	}

}
