/*
 * Copyright 2010 the original author or authors
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package org.springframework.integration.samples.testing.externalgateway;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
/**
 * 
 * @author Oleg Zhurakousky
 * @author Mark Fisher
 * @since SpringOne2GX - 2010, Chicago
 */
public class TrafficHttpConverter implements HttpMessageConverter<Traffic> {
	private List<MediaType> supportedMediaTypes = Collections.emptyList();
	
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return Traffic.class.equals(clazz);
	}

	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return false;
	}

	public List<MediaType> getSupportedMediaTypes() {
		return supportedMediaTypes;
	}

	public Traffic read(Class<? extends Traffic> clazz, HttpInputMessage inputMessage) throws IOException,
																	HttpMessageNotReadableException {
		Traffic traffic = new Traffic();
		try {		
			Document document =
				DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputMessage.getBody());

			XPathExpression titlesXp = XPathExpressionFactory.createXPathExpression("/ResultSet/Result[@type='incident']/Title");
			List<Node> titles = titlesXp.evaluateAsNodeList(document);
			XPathExpression descXp = XPathExpressionFactory.createXPathExpression("/ResultSet/Result[@type='incident']/Description");
			List<Node> description = descXp.evaluateAsNodeList(document);
			int counter = 0;
			for (Node node : titles) {
				traffic.addIncident(((Element)node).getTextContent(), ((Element)description.get(counter++)).getTextContent());
			}
		} catch (Exception e) {
			throw new HttpMessageConversionException("Failed to convert response to: " + clazz, e);
		} 
		return traffic;
	}

	public void write(Traffic t, MediaType contentType,
			HttpOutputMessage outputMessage) throws IOException,
			HttpMessageNotWritableException {
		// for now this converter is only used for converting response
	}

}
