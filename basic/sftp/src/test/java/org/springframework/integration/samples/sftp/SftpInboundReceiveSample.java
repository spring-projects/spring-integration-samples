/**
 * 
 */
package org.springframework.integration.samples.sftp;

import org.junit.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.core.PollableChannel;

/**
 * @author ozhurakousky
 *
 */
public class SftpInboundReceiveSample {

	@Test
	public void runDemo(){
		ApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/integration/SftpInboundReceiveSample-context.xml", this.getClass());
		PollableChannel localFileChannel = context.getBean("receiveChannel", PollableChannel.class);
		System.out.println("Received first file message: " + localFileChannel.receive());
		System.out.println("Received second file message: " + localFileChannel.receive());
		System.out.println("No third file was received " + localFileChannel.receive(1000));
	}
}
