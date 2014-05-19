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

package org.springframework.integration.samples.producer.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.junit.After;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.beans.DocumentToIndex;
import org.springframework.integration.samples.beans.RoutingKey;
import org.springframework.integration.samples.producer.gateway.HBaseGateway;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Main client application, can run as an application or unit test.
 * 
 * @author Flavio Pompermaier
 */
public class QueueProducer {

	private ConfigurableApplicationContext context;

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		new QueueProducer().run();
	}

	@Test
	public void run() throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		context = new ClassPathXmlApplicationContext("bootstrap-config.xml");
		
		final URL resource = getClass().getResource("/queue");
		URI detectionsFolderUri = resource.toURI();
		File samplesDir = new File(detectionsFolderUri);
		
		Date startDate = new Date();
		HBaseGateway queueGateway = (HBaseGateway) context.getBean("hbaseGateway");
		//test detections
		for (File file : samplesDir.listFiles()) {
			String jsonString = readFile(file);
			DocumentToIndex d = new ObjectMapper().readValue(jsonString, DocumentToIndex.class);
			queueGateway.sendDocumentToPersist(d);
		}
		//test end of process
		DocumentToIndex d = new DocumentToIndex();
		d.setRoutingKey(RoutingKey.mysource);
		d.setSource(RoutingKey.mysource.toString());
		d.setStop(true);
		d.setStartTimestamp(startDate.getTime());
		queueGateway.sendDocumentToPersist(d);
	}
	static String readFile(File file) throws IOException {
		Charset encoding = Charset.forName("UTF-8");
		byte[] encoded = Files.readAllBytes(Paths.get(file.toURI()));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
	@After
	public void close() {
		if (context != null) {
			context.close();
		}
	}

}
