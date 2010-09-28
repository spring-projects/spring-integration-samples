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

package org.springframework.integration.samples.loanbroker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.integration.Message;
import org.springframework.integration.samples.loanbroker.domain.LoanQuote;

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
	 * @param messages Messages received from upstream lenders.
	 * @return the best {@link LoanQuote} if the 'RESPONSE_TYPE' header value is 'BEST' else all quotes
	 */
	public Object aggregateQuotes(List<Message<LoanQuote>> messages) {
		ArrayList<LoanQuote> payloads = new ArrayList<LoanQuote>(messages.size());
		for (Message<LoanQuote> message : messages) {
			payloads.add(message.getPayload());
		}
		Collections.sort(payloads);
		String responseType = messages.get(0).getHeaders().get("RESPONSE_TYPE", String.class);
		return ("BEST".equals(responseType)) ? payloads.get(0) : payloads;
	}

}
