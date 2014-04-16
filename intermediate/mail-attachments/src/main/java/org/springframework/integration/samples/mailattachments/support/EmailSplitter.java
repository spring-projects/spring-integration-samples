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
package org.springframework.integration.samples.mailattachments.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.support.MessageBuilder;

/**
 * Splits a {@link List} of {@link EmailFragment}s into individual Spring Integration
 * {@link Message}s.
 *
 * @author Gunnar Hillert
 * @since 2.2
 *
 */
public class EmailSplitter {

	@Splitter
	public List<Message<?>> splitIntoMessages(final List<EmailFragment> emailFragments) {

		final List<Message<?>> messages = new ArrayList<Message<?>>();

		for (EmailFragment emailFragment : emailFragments) {
			Message<?> message = MessageBuilder.withPayload(emailFragment.getData())
											.setHeader(FileHeaders.FILENAME, emailFragment.getFilename())
											.setHeader("directory", emailFragment.getDirectory())
											.build();
			messages.add(message);
		}

		return messages;
	}

}
