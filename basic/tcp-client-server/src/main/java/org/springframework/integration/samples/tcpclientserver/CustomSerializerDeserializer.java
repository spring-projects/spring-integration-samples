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
package org.springframework.integration.samples.tcpclientserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;

/**
 * This class is used to demonstrate how you can create a custom serializer/deserializer
 * to convert a TCP stream into custom objects which your domain-specific code can use.
 *
 * Since this is custom, it will have to have its own predefined assumptions for dealing
 * with the stream. In other words, there will have to be some indication within the
 * contents of the stream where the beginning/end is and how to extract the contents
 * into something meaningful (like an object). An example would be a fixed-file formatted
 * stream with the length encoded in some well known part of the stream (for example, the
 * first 8 bytes of the stream?).
 *
 * This custom serializer/deserializer assumes the first 3 bytes of the stream will be
 * considered an Order Number, the next 10 bytes will be the Sender's Name, the next 6 bytes
 * represents an left-zero-padded integer that specifies how long the rest of the message
 * content is. After that message content is parsed from the stream, the stream is assumed
 * to not have anything after it. In your code you could have delimiters to mark the end
 * of the stream, or could agree with the client that a valid stream is only n characters,
 * etc. Either way, since its custom, the client and server must have some predefined
 * assumptions in place for the communication to take place.
 *
 *
 * @author Christian Posta
 * @author Gunnar Hillert
 */
public class CustomSerializerDeserializer implements Serializer<CustomOrder>, Deserializer<CustomOrder>{

	protected final Log logger = LogFactory.getLog(this.getClass());

	private static final int ORDER_NUMBER_LENGTH = 3;
	private static final int SENDER_NAME_LENGTH = 10;
	private static final int MESSAGE_LENGTH_LENGTH = 6;

	/**
	 * Convert a CustomOrder object into a byte-stream
	 *
	 * @param object
	 * @param outputStream
	 * @throws IOException
	 */
	public void serialize(CustomOrder object, OutputStream outputStream) throws IOException {
		byte[] number = Integer.toString(object.getNumber()).getBytes();
		outputStream.write(number);

		byte[] senderName = object.getSender().getBytes();
		outputStream.write(senderName);

		String lenghtPadded = pad(6, object.getMessage().length());
		byte[] length = lenghtPadded.getBytes();
		outputStream.write(length);

		outputStream.write(object.getMessage().getBytes());
		outputStream.flush();
	}

	private String pad(int desiredLength, int length) {
		return StringUtils.leftPad(Integer.toString(length), desiredLength, '0');
	}

	/**
	 * Convert a raw byte stream into a CustomOrder
	 *
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public CustomOrder deserialize(InputStream inputStream) throws IOException {
		int orderNumber = parseOrderNumber(inputStream);
		String senderName = parseSenderName(inputStream);

		CustomOrder order = new CustomOrder(orderNumber, senderName);
		String message = parseMessage(inputStream);
		order.setMessage(message);
		return order;
	}

	private String parseMessage(InputStream inputStream) throws IOException {
		String lengthString = parseString(inputStream, MESSAGE_LENGTH_LENGTH);
		int lengthOfMessage = Integer.valueOf(lengthString);

		String message = parseString(inputStream, lengthOfMessage);
		return message;
	}

	private String parseString(InputStream inputStream, int length) throws IOException {
		StringBuilder builder = new StringBuilder();

		int c;
		for (int i = 0; i < length; ++i) {
			c = inputStream.read();
			checkClosure(c);
			builder.append((char)c);
		}

		return builder.toString();
	}

	private String parseSenderName(InputStream inputStream) throws IOException {
		return parseString(inputStream, SENDER_NAME_LENGTH);
	}

	private int parseOrderNumber(InputStream inputStream) throws IOException {
		String value = parseString(inputStream, ORDER_NUMBER_LENGTH);
		return Integer.valueOf(value.toString());
	}

	/**
	 * Check whether the byte passed in is the "closed socket" byte
	 * Note, I put this in here just as an example, but you could just extend the
	 * {@link org.springframework.integration.ip.tcp.serializer.AbstractByteArraySerializer} class
	 * which has this method
	 *
	 * @param bite
	 * @throws IOException
	 */
	protected void checkClosure(int bite) throws IOException {
		if (bite < 0) {
			logger.debug("Socket closed during message assembly");
			throw new IOException("Socket closed during message assembly");
		}
	}
}
