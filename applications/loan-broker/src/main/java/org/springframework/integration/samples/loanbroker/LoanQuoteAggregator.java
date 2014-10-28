/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.integration.samples.loanbroker;

import java.util.Collections;
import java.util.List;

import org.springframework.integration.samples.loanbroker.domain.LoanQuote;
import org.springframework.messaging.handler.annotation.Header;

/**
 * Aggregates {@link LoanQuote}s based on the value of the 'RESPONSE_TYPE' message header.
 * When only the <i>best</i> quote is desired, the 'RESPONSE_TYPE' header should have a value
 * of 'BEST'. In this example, that value is set by the 'gateway' when the
 * {@link LoanBrokerGateway#getBestLoanQuote(org.springframework.integration.samples.loanbroker.domain.LoanRequest)}
 * method is invoked by the client.
 *
 * @author Oleg Zhurakousky
 */
public class LoanQuoteAggregator {

	/**
	 * Aggregates LoanQuote Messages to return a single reply Message.
	 *
	 * @param quotes list of loan quotes received from upstream lenders
	 * @param responseType header that indicates the response type
	 * @return the best {@link LoanQuote} if the 'RESPONSE_TYPE' header value is 'BEST' else all quotes
	 */
	public Object aggregateQuotes(List<LoanQuote> quotes,
			@Header(value="RESPONSE_TYPE", required=false) String responseType) {
		Collections.sort(quotes);
		return ("BEST".equals(responseType)) ? quotes.get(0) : quotes;
	}

}
