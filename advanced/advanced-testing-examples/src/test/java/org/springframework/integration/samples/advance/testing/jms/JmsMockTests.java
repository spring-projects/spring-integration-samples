/*
 * Copyright 2002-2022 the original author or authors.
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

package org.springframework.integration.samples.advance.testing.jms;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

/**
 * @author David Turanski
 * @author Gunnar Hillert
 * @author Gary Russell
 * @author Artem Bilan
 */
@SpringJUnitConfig
@ContextConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JmsMockTests {

	private static final Log LOGGER = LogFactory.getLog(JmsMockTests.class);

	private final AtomicReference<String> testMessageHolder = new AtomicReference<>();

	@Autowired
	private JmsTemplate mockJmsTemplate;

	@Autowired
	private SourcePollingChannelAdapter jmsInboundChannelAdapter;

	@Autowired
	@Qualifier("inputChannel")
	private MessageChannel inputChannel;

	@Autowired
	@Qualifier("outputChannel")
	private SubscribableChannel outputChannel;

	@Autowired
	@Qualifier("invalidMessageChannel")
	private SubscribableChannel invalidMessageChannel;

	@BeforeEach
	public void setup() throws JMSException {
		Mockito.reset(this.mockJmsTemplate);
		TextMessage message = mock(TextMessage.class);

		willReturn(new SimpleMessageConverter())
				.given(this.mockJmsTemplate).getMessageConverter();

		willReturn(message)
				.given(this.mockJmsTemplate).receiveSelected(isNull());

		willAnswer((Answer<String>) invocation -> testMessageHolder.get())
				.given(message).getText();
	}

	/**
	 * This test verifies that a message received on a polling JMS inbound channel adapter is
	 * routed to the designated channel and that the message payload is as expected
	 */
	@Test
	public void testReceiveMessage() throws JMSException, InterruptedException, IOException {
		String msg = "hello";

		boolean sent = verifyJmsMessageReceivedOnOutputChannel(msg, outputChannel, new CountDownHandler() {

					@Override
					protected void verifyMessage(Message<?> message) {
						assertThat(message.getPayload()).isEqualTo("hello");
					}
				}
		);
		assertThat(sent).as("message not sent to expected output channel").isTrue();
	}

	/**
	 * This test verifies that a message received on a polling JMS inbound channel adapter is
	 * routed to the errorChannel and that the message payload is the expected exception
	 */
	@Test
	public void testReceiveInvalidMessage() throws JMSException, IOException, InterruptedException {
		String msg = "whoops";
		boolean sent = verifyJmsMessageReceivedOnOutputChannel(msg, invalidMessageChannel, new CountDownHandler() {

					@Override
					protected void verifyMessage(Message<?> message) {
						assertThat(message.getPayload()).isEqualTo("invalid payload");
					}

				}
		);
		assertThat(sent).as("message not sent to expected output channel").isTrue();
	}

	/**
	 * Provide a message via a mock JMS template and wait for the default timeout to receive the message
	 * on the expected channel
	 * @param obj The message provided to the poller (currently must be a String)
	 * @param expectedOutputChannel The expected output channel
	 * @param handler An instance of CountDownHandler to handle (verify) the output message
	 * @return true if the message was received on the expected channel
	 */
	protected boolean verifyJmsMessageReceivedOnOutputChannel(Object obj, SubscribableChannel expectedOutputChannel,
			CountDownHandler handler) throws InterruptedException {

		return verifyJmsMessageOnOutputChannel(obj, expectedOutputChannel, handler, 10000);
	}

	/**
	 * Provide a message via a mock JMS template and wait for the specified timeout to receive the message
	 * on the expected channel
	 * @param obj The message provided to the poller (currently must be a String)
	 * @param expectedOutputChannel The expected output channel
	 * @param handler An instance of CountDownHandler to handle (verify) the output message
	 * @param timeoutMillisec The timeout period. Note that this must allow at least enough time
	 * to process the entire flow. Only set if the default is
	 * not long enough
	 * @return true if the message was received on the expected channel
	 */
	protected boolean verifyJmsMessageOnOutputChannel(Object obj, SubscribableChannel expectedOutputChannel,
			CountDownHandler handler, int timeoutMillisec) throws InterruptedException {

		if (!(obj instanceof String)) {
			throw new IllegalArgumentException("Only TextMessage is currently supported");
		}

		/*
		 * Use mocks to create a message returned to the JMS inbound adapter. It is assumed that the JmsTemplate
		 * is also a mock.
		 */

		this.testMessageHolder.set((String) obj);
		CountDownLatch latch = new CountDownLatch(1);
		handler.setLatch(latch);

		expectedOutputChannel.subscribe(handler);

		this.jmsInboundChannelAdapter.start();

		boolean latchCountedToZero = latch.await(timeoutMillisec, TimeUnit.MILLISECONDS);

		if (!latchCountedToZero) {
			LOGGER.warn(String.format("The specified waiting time of the latch (%s ms) elapsed.", timeoutMillisec));
		}

		return latchCountedToZero;

	}

	/*
	 * A MessageHandler that uses a CountDownLatch to synchronize with the calling thread
	 */
	private abstract static class CountDownHandler implements MessageHandler {

		CountDownLatch latch;

		public final void setLatch(CountDownLatch latch) {
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
		@Override
		public void handleMessage(Message<?> message) throws MessagingException {
			verifyMessage(message);
			latch.countDown();
		}

	}

}
