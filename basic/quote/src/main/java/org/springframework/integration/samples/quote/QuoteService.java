/*
 * Copyright 2002-2015 the original author or authors.
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

package org.springframework.integration.samples.quote;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

/**
 * @author Mark Fisher
 * @author Gary Russell
 */
@MessageEndpoint
public class QuoteService {

	@ServiceActivator(inputChannel="tickers", outputChannel="quotes")
	public Quote lookupQuote(String ticker) {
		BigDecimal price = new BigDecimal(new Random().nextDouble() * 100);//NOSONAR
		return new Quote(ticker, price.setScale(2, RoundingMode.HALF_EVEN));
	}

}
