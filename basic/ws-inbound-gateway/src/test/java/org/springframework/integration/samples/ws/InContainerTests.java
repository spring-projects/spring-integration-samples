/*
 * Copyright 2002-present the original author or authors.
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

import javax.xml.transform.Source;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;

import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * System tests ensuring the Spring WS MessageDispatcherServlet is correctly
 * set up and configured to delegate incoming requests to our ws:inbound-gateway.
 * <p>
 * Use 'mvn package' to create a war file for this project, then deploy before
 * attempting to run this test.
 *
 * @author Chris Beams
 * @author Gary Russell
 */
public class InContainerTests {

	private static final Log LOGGER = LogFactory.getLog(InContainerTests.class);

	private static final String WS_URI = "http://localhost:8080/ws-inbound-gateway/echoservice";

	private final WebServiceTemplate template = new WebServiceTemplate();

	@Test
	public void testWebServiceRequestAndResponse() {
		StringResult result = new StringResult();
		Source payload = new StringSource(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
						"<echoRequest xmlns=\"http://www.springframework.org/spring-ws/samples/echo\">hello</echoRequest>");

		template.sendSourceAndReceiveToResult(WS_URI, payload, result);
		LOGGER.info("RESULT: " + result);
		assertThat(result.toString()).isEqualTo(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
						"<echoResponse xmlns=\"http://www.springframework.org/spring-ws/samples/echo\">hello</echoResponse>");
	}

}
