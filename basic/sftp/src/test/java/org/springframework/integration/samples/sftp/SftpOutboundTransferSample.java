/*
 * Copyright 2002-2012 the original author or authors.
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
package org.springframework.integration.samples.sftp;

import java.io.File;

import org.junit.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.util.Assert;

/**
 *
 * @author Oleg Zhurakousky
 * @author Gunnar Hillert
 *
 */
public class SftpOutboundTransferSample {

	@Test
	public void testOutbound() throws Exception{

		final String sourceFileName = "README.md";
		final String destinationFileName = sourceFileName +"_foo";
		final String destinationFilePath = "remote-target-dir/" + destinationFileName;

		final ClassPathXmlApplicationContext ac =
			new ClassPathXmlApplicationContext("/META-INF/spring/integration/SftpOutboundTransferSample-context.xml", SftpOutboundTransferSample.class);
		ac.start();

		final File file = new File(sourceFileName);

		Assert.isTrue(file.exists(), String.format("File '%s' does not exist.", sourceFileName));

		final Message<File> message = MessageBuilder.withPayload(file).build();
		final MessageChannel inputChannel = ac.getBean("inputChannel", MessageChannel.class);

		inputChannel.send(message);
		Thread.sleep(2000);

		Assert.isTrue(new File(destinationFilePath).exists(), String.format("File '%s' does not exist.", destinationFilePath));

		System.out.println(String.format("Successfully transferred '%s' file to a " +
				"remote location under the name '%s'", sourceFileName, destinationFileName));

		ac.stop();

	}

}
