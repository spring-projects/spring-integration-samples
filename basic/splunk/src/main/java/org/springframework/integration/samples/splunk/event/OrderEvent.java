/*
 * Copyright 2015 the original author or authors.
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
package org.springframework.integration.samples.splunk.event;

import org.springframework.integration.splunk.event.SplunkEvent;

/**
 * @author Filippo Balicchia
 * @since 4.2
 */
public class OrderEvent extends SplunkEvent {

	private static final long serialVersionUID = 4203808769092252241L;

	public static String ORDER_NUMBER = "order_number";

	public static String EAN_ITEM = "ean";

	public static String EMAIL_USER = "email";

	public OrderEvent() {
		super();
	}

	public void setOrderNumber(String orderNumber) {
		addPair(ORDER_NUMBER, orderNumber);
	}

	public void setEan(String eanNumber) {
		addPair(EAN_ITEM, eanNumber);
	}

	public void setEmailuser(String email) {
		addPair(EMAIL_USER, email);
	}

}
