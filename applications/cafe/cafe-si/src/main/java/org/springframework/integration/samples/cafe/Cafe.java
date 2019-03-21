/*
 * Copyright 2002-2012 the original author or authors.
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

package org.springframework.integration.samples.cafe;

import org.springframework.integration.annotation.Gateway;

/**
 * The entry point for the Cafe Demo. The demo's main() method invokes the
 * '<code>placeOrder</code>' method on a generated MessagingGateway proxy.
 * The gateway then passes the {@link Order} as the payload of a
 * {@link org.springframework.integration.Message} to the
 * configured <em>requestChannel</em>. The channel ('orders') is
 * defined in the 'cafeDemo.xml' file.
 *
 * @author Mark Fisher
 */
public interface Cafe {

	@Gateway(requestChannel="orders")
	void placeOrder(Order order);

}
