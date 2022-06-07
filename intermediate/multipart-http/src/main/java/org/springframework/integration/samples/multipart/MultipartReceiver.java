/*
 * Copyright 2002-2017 the original author or authors.
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
package org.springframework.integration.samples.multipart;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.integration.http.multipart.UploadedMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
/**
 * @author Oleg Zhurakousky
 * @author Gary Russell
 *
 */
public class MultipartReceiver {
	private static final Log logger = LogFactory.getLog(MultipartReceiver.class);

	@SuppressWarnings("rawtypes")
	public void receive(LinkedMultiValueMap<String, Object> multipartRequest){
		logger.info("Successfully received multipart request: " + multipartRequest);
		for (String elementName : multipartRequest.keySet()) {
			if (elementName.equals("company")){
				LinkedList value =  (LinkedList)multipartRequest.get("company");
				String[] multiValues = (String[]) value.get(0);
				for (String companyName : multiValues) {
					logger.info(elementName + " - " + companyName);
				}
			} else if (elementName.equals("company-logo")){
				logger.info(elementName + " - as UploadedMultipartFile: "
						+ ((UploadedMultipartFile) multipartRequest.getFirst("company-logo")).getOriginalFilename());
			}
		}
	}
}
