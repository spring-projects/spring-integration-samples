/*
 * Copyright 2002-2010 the original author or authors.
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

package org.springframework.integration.samples.ws;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.ws.inbound.SimpleWebServiceInboundGateway;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.pox.dom.DomPoxMessage;
import org.springframework.ws.pox.dom.DomPoxMessageFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Out-of-container tests for ws:inbound-gateway message processing.
 *
 * @author Mark Fisher
 * @author Chris Beams
 * @author Artem Bilan
 */
@SpringJUnitConfig(locations = "/META-INF/spring/integration/inbound-gateway-config.xml")
@DirtiesContext
public class InboundGatewayTests {

	@Autowired
	private SimpleWebServiceInboundGateway gateway;

	/**
	 * Emulate the Spring WS MessageDispatcherServlet by calling the gateway
	 * with a DOMSource object representing the payload of the original SOAP
	 * 'echoRequest' message.  Expect an 'echoResponse' DOMSource object
	 * to be returned in synchronous fashion, which the MessageDispatcherServlet
	 * would in turn wrap in a SOAP envelope and return to the client.
	 */
	@Test
	public void testSendAndReceive() throws Exception {
		String xml = "<echoRequest xmlns=\"http://www.springframework.org/spring-ws/samples/echo\">hello</echoRequest>";
		DomPoxMessageFactory messageFactory = new DomPoxMessageFactory();
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(new InputSource(new StringReader(xml)));
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		DomPoxMessage request = new DomPoxMessage(document, transformer, "text/xml");
		MessageContext messageContext = new DefaultMessageContext(request, messageFactory);
		gateway.invoke(messageContext);
		Object reply = messageContext.getResponse().getPayloadSource();
		assertThat(reply).isInstanceOf(DOMSource.class);
		DOMSource replySource = (DOMSource) reply;
		Element element = (Element) replySource.getNode().getFirstChild();
		assertThat(element.getTagName()).isEqualTo("echoResponse");
	}

}
