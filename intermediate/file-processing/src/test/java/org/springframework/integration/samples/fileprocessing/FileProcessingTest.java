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

package org.springframework.integration.samples.fileprocessing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.messaging.PollableChannel;

/**
 * @author Oleg Zhurakousky
 * @author Gary Russell
 *
 */
public class FileProcessingTest {
	private final int fileCount = 5;
	private final Log logger = LogFactory.getLog(FileProcessingTest.class);

	@Before
	public void createDirectory(){
		File directory = new File("input");
		if (directory.exists()){
			directory.delete();
		}
		directory.mkdir();
	}

	@Test
	public void testSequentialFileProcessing() throws Exception {
		logger.info("\n\n#### Starting Sequential processing test ####");
		logger.info("Populating directory with files");
		for (int i = 0; i < fileCount; i++) {
			File file = new File("input/file_" + i + ".txt");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
		    out.write("hello " + i);
		    out.close();
		}
		logger.info("Populated directory with files");
		Thread.sleep(2000);
		logger.info("Starting Spring Integration Sequential File processing");
		ConfigurableApplicationContext ac = new ClassPathXmlApplicationContext("META-INF/spring/integration/sequentialFileProcessing-config.xml");
		PollableChannel filesOutChannel = ac.getBean("filesOutChannel", PollableChannel.class);
		for (int i = 0; i < fileCount; i++) {
			logger.info("Finished processing " + filesOutChannel.receive(10000).getPayload());
		}
		ac.stop();
	}
	@Test
	public void testConcurrentFileProcessing() throws Exception {
		logger.info("\n\n#### Starting Concurrent processing test #### ");
		logger.info("Populating directory with files");
		for (int i = 0; i < fileCount; i++) {
			File file = new File("input/file_" + i + ".txt");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
		    out.write("hello " + i);
		    out.close();
		}
		logger.info("Populated directory with files");
		Thread.sleep(2000);
		logger.info("Starting Spring Integration Sequential File processing");
		ConfigurableApplicationContext ac = new ClassPathXmlApplicationContext("/META-INF/spring/integration/concurrentFileProcessing-config.xml");
		PollableChannel filesOutChannel = ac.getBean("filesOutChannel", PollableChannel.class);
		for (int i = 0; i < fileCount; i++) {
			logger.info("Finished processing " + filesOutChannel.receive(10000).getPayload());
		}
		ac.close();
	}
}
