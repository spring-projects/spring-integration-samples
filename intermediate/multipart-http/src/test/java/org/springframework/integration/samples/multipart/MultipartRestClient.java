/*
 * Copyright 2002-2017 the original author or authors.
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
package org.springframework.integration.samples.multipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
/**
 * @author Oleg Zhurakousky
 * @author Gary Russell
 *
 */
public class MultipartRestClient {

	private static Log logger = LogFactory.getLog(MultipartRestClient.class);

	private static String uri = "http://localhost:8080/multipart-http/inboundAdapter.htm";

	private static String resourcePath = "org/springframework/integration/samples/multipart/spring09_logo.png";

	public static void main(String[] args) throws Exception{
		RestTemplate template = new RestTemplate();
		Resource s2logo = new ClassPathResource(resourcePath);
		MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
		multipartMap.add("company", "SpringSource");
		multipartMap.add("company-logo", s2logo);
		logger.info("Created multipart request: " + multipartMap);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("multipart", "form-data"));
		HttpEntity<Object> request = new HttpEntity<Object>(multipartMap, headers);
		logger.info("Posting request to: " + uri);
		ResponseEntity<?> httpResponse = template.exchange(uri, HttpMethod.POST, request, Object.class);
		if (!httpResponse.getStatusCode().equals(HttpStatus.OK)){
			logger.error("Problems with the request. Http status: " + httpResponse.getStatusCode());
		}
	}
}
