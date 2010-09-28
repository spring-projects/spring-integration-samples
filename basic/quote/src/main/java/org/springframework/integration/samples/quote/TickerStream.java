/*
 * Copyright 2002-2008 the original author or authors.
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

import java.util.Random;

/**
 * @author Mark Fisher
 */
public class TickerStream {

	public String nextTicker() {
		char[] chars = new char[3];
		for (int i = 0; i < 3; i++) {
			chars[i] = (char) (new Random().nextInt(25) + 65);
		}
		return new String(chars);
	}

}
