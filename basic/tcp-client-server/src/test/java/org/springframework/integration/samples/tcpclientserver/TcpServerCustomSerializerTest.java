/*
 * Copyright 2002-2018 the original author or authors.
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

package org.springframework.integration.samples.tcpclientserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.util.TestingUtilities;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Some use cases may dictate you needing to create your own stream handling serializers
 * and deserializers. This sample shows a custom serializer/deserializer being used with
 * the Java socket API on the front end (client) and the Spring Integration TCP inbound
 * gateway with the custom serializer/deserializers.
 *
 * @author Christian Posta
 * @author Gunnar Hillert
 * @author Gary Russell
 * @author Artem Bilan
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/spring/integration/tcpServerCustomSerialize-context.xml")
@DirtiesContext
public class TcpServerCustomSerializerTest {

	private static final Log LOGGER = LogFactory.getLog(TcpServerCustomSerializerTest.class);

	@Autowired
	@Qualifier("incomingServerChannel")
	MessageChannel incomingServerChannel;

	@Autowired
	AbstractServerConnectionFactory serverConnectionFactory;

	@Before
	public void setup() {
		TestingUtilities.waitListening(this.serverConnectionFactory, 10000L);
	}

	@Test
	public void testHappyPath() {

		// add a listener to this channel, otherwise there is not one defined
		// the reason we use a listener here is so we can assert truths on the
		// message and/or payload
		SubscribableChannel channel = (SubscribableChannel) incomingServerChannel;
		channel.subscribe(new AbstractReplyProducingMessageHandler() {

			@Override
			protected Object handleRequestMessage(Message<?> requestMessage) {
				CustomOrder payload = (CustomOrder) requestMessage.getPayload();

				// we assert during the processing of the messaging that the
				// payload is just the content we wanted to send without the
				// framing bytes (STX/ETX)
				assertEquals(123, payload.getNumber());
				assertEquals("PINGPONG02", payload.getSender());
				assertEquals("You got it to work!", payload.getMessage());
				return requestMessage;
			}
		});

		String sourceMessage = "123PINGPONG02000019You got it to work!";

		// use the java socket API to make the connection to the server
		Socket socket = null;
		Writer out = null;
		BufferedReader in = null;

		try {
			socket = new Socket("localhost", this.serverConnectionFactory.getPort());
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			out.write(sourceMessage);
			out.flush();

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			StringBuffer str = new StringBuffer();

			int c;
			while ((c = in.read()) != -1) {
				str.append((char) c);
			}

			String response = str.toString();
			assertEquals(sourceMessage, response);

		}
		catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			fail(String.format("Test (port: %s) ended with an exception: %s", this.serverConnectionFactory.getPort(),
					e.getMessage()));
		}
		finally {
			try {
				socket.close();
				out.close();
				in.close();

			}
			catch (Exception e) {
				// swallow exception
			}

		}
	}

}
