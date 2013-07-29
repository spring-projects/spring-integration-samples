/*
 * Copyright 2002-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.springframework.integration.samples.json.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.integration.samples.beans.Document;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Main client application, can run as an application or unit test.
 * 
 * @author Flavio Pompermaier
 */
public class Serialization {

	final String OUTPUT_TEST_FOLDER = "/output";
	final String DOCUMENT_OUTPUT_FILE = "document.json";
	final String ENTITY_OUTPUT_FILE = "bill-gates.json";
	private ObjectMapper mapper;
	
	@Before 
	public void init(){
		mapper = new ObjectMapper();
	}
	@Test
	public void testSerializationOfDocument() throws JsonParseException,
			JsonMappingException, IOException, URISyntaxException {
		final URL resource = getClass().getResource("/");
		URI testBaseFolder = resource.toURI();
		File outputDir = new File(new File(testBaseFolder), OUTPUT_TEST_FOLDER);
		File outputFile = new File(outputDir, DOCUMENT_OUTPUT_FILE);

		Document doc = JsonMocksGenerator.getDoc1();
		String json = mapper.writeValueAsString(doc);
		FileUtils.writeStringToFile(outputFile, json, "UTF-8");
	}

	
}
