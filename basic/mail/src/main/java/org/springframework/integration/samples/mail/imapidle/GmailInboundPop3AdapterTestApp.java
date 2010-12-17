/**
 * 
 */
package org.springframework.integration.samples.mail.imapidle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageHandler;

/**
 * @author Oleg Zhurakousky
 *
 */
public class GmailInboundPop3AdapterTestApp {

	private static Logger logger = Logger.getLogger(GmailInboundPop3AdapterTestApp.class);
	
	public static void main (String[] args) throws Exception {
		ApplicationContext ac = new ClassPathXmlApplicationContext("/META-INF/spring/integration/gmail-pop3-config.xml");
		DirectChannel inputChannel = ac.getBean("recieveChannel", DirectChannel.class);
		inputChannel.subscribe(new MessageHandler() {
			public void handleMessage(Message<?> message) throws MessagingException {
				logger.info("Message: " + message);
			}
		});
	}
}
