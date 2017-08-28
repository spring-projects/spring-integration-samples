/*
 * Copyright 2002-2017 the original author or authors.
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

package org.springframework.integration.samples.loanbroker.demo;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.loanbroker.LoanBrokerGateway;
import org.springframework.integration.samples.loanbroker.domain.Customer;
import org.springframework.integration.samples.loanbroker.domain.LoanQuote;
import org.springframework.integration.samples.loanbroker.domain.LoanRequest;

/**
 * @author Oleg Zhurakousky
 * @author Gary Russell
 */
public class LoanBrokerDemo {

	private static Log logger = LogFactory.getLog(LoanBrokerDemo.class);

	public static void main(String[] args) {
		new LoanBrokerDemo().runDemo();
	}

	public void runDemo() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"META-INF/spring/integration/bootstrap-config/stubbed-loan-broker.xml");
		LoanBrokerGateway broker = context.getBean("loanBrokerGateway", LoanBrokerGateway.class);
		LoanRequest loanRequest = new LoanRequest();
		loanRequest.setCustomer(new Customer());
		LoanQuote loan = broker.getBestLoanQuote(loanRequest);
		logger.info("********* Best Quote *********\n" + loan);
		System.out.println("==============================");
		List<LoanQuote> loanQuotes = broker.getAllLoanQuotes(loanRequest);
		logger.info("********* All Quotes *********");
		for (LoanQuote loanQuote : loanQuotes) {
			logger.info(loanQuote);
		}
		context.close();
	}

}
