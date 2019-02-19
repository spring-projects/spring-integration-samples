/*
 * Copyright 2002-2019 the original author or authors.
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

package org.springframework.integration.samples.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.integration.samples.rest.domain.EmployeeList;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * RestHttpClientTest.java: Functional Test to test the REST HTTP Path usage. This test requires
 * rest-http application running in HTTP environment.
 *
 * @author Vigil Bose
 * @author Gary Russell
 * @author Artem Bilan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/integration/http-outbound-config.xml" })
public class RestHttpClientTest {

	@Autowired
	private RestTemplate restTemplate;

	private HttpMessageConverterExtractor<EmployeeList> responseExtractor;

	private static Log logger = LogFactory.getLog(RestHttpClientTest.class);

	@Autowired
	private Jaxb2Marshaller marshaller;

	@Autowired
	private ObjectMapper jaxbJacksonObjectMapper;

	@Before
	public void setUp() {
		responseExtractor = new HttpMessageConverterExtractor<>(EmployeeList.class, restTemplate
				.getMessageConverters());

		Map<String, Object> properties = new HashMap<>();
		properties.put(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8");
		properties.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setMarshallerProperties(properties);
	}

	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetEmployeeAsXml() {

		Map<String, Object> employeeSearchMap = getEmployeeSearchMap("0");

		final String fullUrl = "http://localhost:8080/rest-http/services/employee/{id}/search";

		EmployeeList employeeList = restTemplate.execute(fullUrl, HttpMethod.GET,
				request -> {
					HttpHeaders headers = getHttpHeadersWithUserCredentials(request);
					headers.add("Accept", "application/xml");
				}, responseExtractor, employeeSearchMap);

		logger.info("The employee list size :" + employeeList.getEmployee().size());

		StringWriter sw = new StringWriter();
		StreamResult sr = new StreamResult(sw);

		marshaller.marshal(employeeList, sr);
		logger.info(sr.getWriter().toString());
		assertTrue(employeeList.getEmployee().size() > 0);
	}

	private Map<String, Object> getEmployeeSearchMap(String id) {
		Map<String, Object> employeeSearchMap = new HashMap<>();
		employeeSearchMap.put("id", id);
		return employeeSearchMap;
	}

	@Test
	public void testGetEmployeeAsJson() throws Exception {
		Map<String, Object> employeeSearchMap = getEmployeeSearchMap("0");

		final String fullUrl = "http://localhost:8080/rest-http/services/employee/{id}/search?format" +
				"=json";
		HttpHeaders headers = getHttpHeadersWithUserCredentials(new HttpHeaders());
		headers.add("Accept", "application/json");
		HttpEntity<Object> request = new HttpEntity<>(headers);

		ResponseEntity<?> httpResponse = restTemplate
				.exchange(fullUrl, HttpMethod.GET, request, EmployeeList.class, employeeSearchMap);
		logger.info("Return Status :" + httpResponse.getHeaders().get("X-Return-Status"));
		logger.info("Return Status Message :" + httpResponse.getHeaders().get("X-Return-Status-Msg"));
		assertEquals(httpResponse.getStatusCode(), HttpStatus.OK);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		jaxbJacksonObjectMapper.writeValue(out, httpResponse.getBody());
		logger.info(new String(out.toByteArray()));
	}

	private HttpHeaders getHttpHeadersWithUserCredentials(ClientHttpRequest request) {
		return (getHttpHeadersWithUserCredentials(request.getHeaders()));
	}

	private HttpHeaders getHttpHeadersWithUserCredentials(HttpHeaders headers) {

		String username = "SPRING";
		String password = "spring";

		String combinedUsernamePassword = username + ":" + password;
		byte[] base64Token = Base64.getEncoder().encode(combinedUsernamePassword.getBytes());
		String base64EncodedToken = new String(base64Token);
		//adding Authorization header for HTTP Basic authentication
		headers.add("Authorization", "Basic " + base64EncodedToken);

		return headers;
	}

}


