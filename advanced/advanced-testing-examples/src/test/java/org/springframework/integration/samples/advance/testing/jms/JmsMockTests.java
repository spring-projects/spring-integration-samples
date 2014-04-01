/*
 * Copyright 2002-2012 the original author or authors.
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
package org.springframework.integration.samples.advance.testing.jms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class JmsMockTests {

	private static final Logger LOGGER = Logger.getLogger(JmsMockTests.class);

	@Autowired
	JmsTemplate mockJmsTemplate;

	@Autowired
	@Qualifier("inputChannel")
	MessageChannel inputChannel;

	@Autowired
	@Qualifier("outputChannel")
	SubscribableChannel outputChannel;

	@Autowired
	@Qualifier("invalidMessageChannel")
	SubscribableChannel invalidMessageChannel;

	/**
	 * This test verifies that a message received on a polling JMS inbound channel adapter is
	 * routed to the designated channel and that the message payload is as expected
	 *
	 * @throws JMSException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@Test
	public void testReceiveMessage() throws JMSException, InterruptedException, IOException {
		String msg = "hello";

		boolean sent = verifyJmsMessageReceivedOnOutputChannel(msg, outputChannel,new CountDownHandler() {

			@Override
			protected void verifyMessage(Message<?> message) {
				assertEquals("hello",message.getPayload());
		 	}
		}
		);
		assertTrue("message not sent to expected output channel", sent);
	}

	/**
	 * This test verifies that a message received on a polling JMS inbound channel adapter is
	 * routed to the errorChannel and that the message payload is the expected exception
	 *
	 * @throws JMSException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void testReceiveInvalidMessage() throws JMSException, IOException, InterruptedException {
		String msg = "whoops";
		boolean sent = verifyJmsMessageReceivedOnOutputChannel(msg, invalidMessageChannel,new CountDownHandler() {

			@Override
			protected void verifyMessage(Message<?> message) {
				assertEquals("invalid payload",message.getPayload());
			}

		}
		);
		assertTrue("message not sent to expected output channel", sent);
	}

	/**
	 * Provide a message via a mock JMS template and wait for the default timeout to receive the message on the expected channel
	 * @param obj The message provided to the poller (currently must be a String)
	 * @param expectedOutputChannel The expected output channel
	 * @param handler An instance of CountDownHandler to handle (verify) the output message
	 * @return true if the message was received on the expected channel
	 * @throws JMSException
	 * @throws InterruptedException
	 */
	protected boolean verifyJmsMessageReceivedOnOutputChannel(Object obj, SubscribableChannel expectedOutputChannel, CountDownHandler handler) throws JMSException, InterruptedException{
		return verifyJmsMessageOnOutputChannel(obj, expectedOutputChannel, handler, 7000);
	}


	/**
	  * Provide a message via a mock JMS template and wait for the specified timeout to receive the message on the expected channel
	 * @param obj The message provided to the poller (currently must be a String)
	 * @param expectedOutputChannel The expected output channel
	 * @param handler An instance of CountDownHandler to handle (verify) the output message
	 * @param timeoutMillisec The timeout period. Note that this must allow at least enough time to process the entire flow. Only set if the default is
	 * not long enough
	 * @return true if the message was received on the expected channel
	 * @throws JMSException
	 * @throws InterruptedException
	 */
	protected boolean verifyJmsMessageOnOutputChannel(Object obj, SubscribableChannel expectedOutputChannel, CountDownHandler handler,int timeoutMillisec) throws JMSException,
			InterruptedException {

		if (!(obj instanceof String)) {
			throw new IllegalArgumentException("Only TextMessage is currently supported");
		}

		/*
		 * Use mocks to create a message returned to the JMS inbound adapter. It is assumed that the JmsTemplate
		 * is also a mock.
		 */

		TextMessage message = mock(TextMessage.class);
		doReturn(new SimpleMessageConverter()).when(mockJmsTemplate).getMessageConverter();
		doReturn(message).when(mockJmsTemplate).receiveSelected(anyString());

		String text = (String) obj;

		CountDownLatch latch = new CountDownLatch(1);
		handler.setLatch(latch);

		doReturn(text).when(message).getText();

		expectedOutputChannel.subscribe(handler);

		boolean latchCountedToZero = latch.await(timeoutMillisec, TimeUnit.MILLISECONDS);

		if (!latchCountedToZero) {
			LOGGER.warn(String.format("The specified waiting time of the latch (%s ms) elapsed.", timeoutMillisec));
		}

		return latchCountedToZero;

	}
	/*
	 * A MessageHandler that uses a CountDownLatch to synchronize with the calling thread
	 */
	private abstract class CountDownHandler implements MessageHandler {

		CountDownLatch latch;

		public final void setLatch(CountDownLatch latch){
			this.latch = latch;
		}

		protected abstract void verifyMessage(Message<?> message);

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.springframework.integration.core.MessageHandler#handleMessage
		 * (org.springframework.integration.Message)
		 */
		public void handleMessage(Message<?> message) throws MessagingException {
			verifyMessage(message);
			latch.countDown();
		}
	}
}
