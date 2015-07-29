/*
 * Copyright 2002-2012 the original author or authors.
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
package org.springframework.integration.samples.splitteraggregator;

import java.util.Collection;

import org.springframework.integration.samples.splitteraggregator.support.AbstractCriteria;
import org.springframework.integration.samples.splitteraggregator.support.CompositeCriteria;

/**
 * Given CompositeCriteria, return a collection of the individual criterion.
 * 
 * @author Christopher Hunt
 * 
 */
public class SearchRequestSplitter {

	public Collection<AbstractCriteria> split(CompositeCriteria criteria) {
		return criteria.getCriteria();
	}
}
