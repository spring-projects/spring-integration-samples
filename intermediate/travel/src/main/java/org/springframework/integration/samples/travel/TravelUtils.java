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
package org.springframework.integration.samples.travel;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.springframework.xml.transform.StringSource;

/**
 * Provides utility methods for the Travel application.
 *
 * @author Gunnar Hillert
 * @since 2.2
 *
 */
public final class TravelUtils {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private TravelUtils() {
	}

	/**
	 * Prettifies XML data, e.g. add indentation.
	 *
	 * @param result The XML to prettify
	 * @return Prettified XML
	 * @throws Exception
	 */
	public static String formatXml(String result) throws Exception{
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(2));
		StreamResult streamResult = new StreamResult(new StringWriter());
		Source source = new StringSource(result);
		transformer.transform(source, streamResult);
		String xmlString = streamResult.getWriter().toString();
		return xmlString;
	}

}
