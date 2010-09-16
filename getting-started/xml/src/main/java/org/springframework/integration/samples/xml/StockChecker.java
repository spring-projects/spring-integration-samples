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

package org.springframework.integration.samples.xml;

import org.w3c.dom.Document;

import org.springframework.xml.xpath.XPathExpression;

/**
 * @author Jonas Partner
 */
public class StockChecker {

	private final XPathExpression isbnSelectingXPath;

	public StockChecker(XPathExpression isbnSelectingXPath) {
		this.isbnSelectingXPath = isbnSelectingXPath;
	}

	public Document checkStockLevel(Document doc) {
		String isbn = isbnSelectingXPath.evaluateAsString(doc);
		boolean inStock = false;

		// we only carry stock of one book currently
		if ("0321200683".equals(isbn)) {
			inStock = true;
		}
		doc.getDocumentElement().setAttribute("in-stock", String.valueOf(inStock));
		return doc;
	}

}
