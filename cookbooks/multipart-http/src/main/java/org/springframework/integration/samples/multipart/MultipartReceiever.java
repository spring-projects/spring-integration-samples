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
package org.springframework.integration.samples.multipart;

import org.apache.log4j.Logger;
import org.springframework.integration.http.UploadedMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
/**
 * @author Oleg Zhurakousky
 *
 */
public class MultipartReceiever {
	private static Logger logger = Logger.getLogger(MultipartClient.class);

	public void recieve(LinkedMultiValueMap<String, Object> multipartRequest){	
		logger.info("Successfully recieved multipart request: " + multipartRequest);
		for (String elementName : multipartRequest.keySet()) {
			if (elementName.equals("company")){
				logger.info(elementName + " - " + ((String[]) multipartRequest.getFirst("company"))[0]);
			} else if (elementName.equals("company-logo")){
				logger.info(elementName + " - as UploadedMultipartFile: " 
						+ ((UploadedMultipartFile) multipartRequest.getFirst("company-logo")).getOriginalFilename());
			}
		}
	}
}
