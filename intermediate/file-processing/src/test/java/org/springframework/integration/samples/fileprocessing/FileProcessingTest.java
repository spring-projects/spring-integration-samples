/**
 *
 */
package org.springframework.integration.samples.fileprocessing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.messaging.PollableChannel;

/**
 * @author Oleg Zhurakousky
 *
 */
public class FileProcessingTest {
	private int fileCount = 5;
	private Logger logger = Logger.getLogger(FileProcessingTest.class);

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
		ac.stop();
	}
}
