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
package org.springframework.integration.samples.mailattachments.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.integration.annotation.Transformer;

/**
 * Parses the E-mail Message and converts each containing message and/or attachment into
 * a {@link List} of {@link EmailFragment}s.
 *
 * @author Gunnar Hillert
 * @author Gary Russell
 * @since 2.2
 *
 */
public class EmailTransformer {

	private static final Log LOGGER = LogFactory.getLog(EmailTransformer.class);

	@Transformer
	public List<EmailFragment> transformit(javax.mail.Message mailMessage) {

		final List<EmailFragment> emailFragments = new ArrayList<EmailFragment>();

		EmailParserUtils.handleMessage(null, mailMessage, emailFragments);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("Email contains %s fragments.", emailFragments.size()));
		}

		return emailFragments;
	}

}
