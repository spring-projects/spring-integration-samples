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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.ParseException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

/**
 * Utility Class for parsing mail messages.
 *
 * @author Gunnar Hillert
 * @since 2.2
 *
 */
public final class EmailParserUtils {

	private static final Logger LOGGER = Logger.getLogger(EmailParserUtils.class);

	/**
	 * Parses a mail message. The respective message can either be the root message
	 * or another message that is attached to another message.
	 *
	 * @param directory The directory for storing the message. If null this is the root message.
	 * @param mailMessage The mail message to be parsed. Must not be null.
	 * @param emailFragments Must not be null.
	 */
	public static final void handleMessage(File directory, javax.mail.Message mailMessage, List<EmailFragment> emailFragments) {

		Assert.notNull(mailMessage, "The mail message to be parsed must not be null.");
		Assert.notNull(emailFragments, "The collection of emailfragments must not be null.");

		final Object content;
		final String subject;

		try {
			content = mailMessage.getContent();
			subject = mailMessage.getSubject();
		} catch (IOException e) {
			throw new IllegalStateException("Error while retrieving the email contents.", e);
		} catch (MessagingException e) {
			throw new IllegalStateException("Error while retrieving the email contents.", e);
		}

		if (directory == null) {
			directory = new File(subject);
		} else {
			directory = new File(directory, subject);
		}

		if (content instanceof String) {
			emailFragments.add(new EmailFragment(new File(subject), "message.txt", content));
		}
		else if (content instanceof Multipart) {
			Multipart multipart = (Multipart) content;
			handleMultipart(directory, multipart, mailMessage, emailFragments);
		}
		else {
			throw new IllegalStateException("This content type is not handled - " + content.getClass().getSimpleName());
		}

	}

	/**
	 *
	 * @param directory Must not be null
	 * @param multipart Must not be null
	 * @param mailMessage Must not be null
	 * @param emailFragments Must not be null
	 */
	public static final void handleMultipart(File directory, Multipart multipart, javax.mail.Message mailMessage, List<EmailFragment> emailFragments) {

		Assert.notNull(directory, "The directory must not be null.");
		Assert.notNull(multipart, "The multipart object to be parsed must not be null.");
		Assert.notNull(mailMessage, "The mail message to be parsed must not be null.");
		Assert.notNull(emailFragments, "The collection of emailfragments must not be null.");

		final int count;

		try {
			count = multipart.getCount();

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(String.format("Number of enclosed BodyPart objects: %s.", count));
			}

		}
		catch (MessagingException e) {
			throw new IllegalStateException("Error while retrieving the number of enclosed BodyPart objects.", e);
		}

		for (int i = 0; i < count; i++) {

			final BodyPart bp;

			try {
				bp = multipart.getBodyPart(i);
			}
			catch (MessagingException e) {
				throw new IllegalStateException("Error while retrieving body part.", e);
			}

			final String contentType;
			String filename;
			final String disposition;
			final String subject;

			try {

				contentType = bp.getContentType();
				filename    = bp.getFileName();
				disposition = bp.getDisposition();
				subject     = mailMessage.getSubject();

				if (filename == null && bp instanceof MimeBodyPart) {
					filename = ((MimeBodyPart) bp).getContentID();
				}

			}
			catch (MessagingException e) {
				throw new IllegalStateException("Unable to retrieve body part meta data.", e);
			}

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(String.format("BodyPart - Content Type: '%s', filename: '%s', disposition: '%s', subject: '%s'",
							 new Object[]{contentType, filename, disposition, subject}));
			}

			if (Part.ATTACHMENT.equalsIgnoreCase(disposition)) {
				LOGGER.info(String.format("Handdling attachment '%s', type: '%s'", filename, contentType));
			}

			final Object content;

			try {
				content = bp.getContent();
			}
			catch (IOException e) {
				throw new IllegalStateException("Error while retrieving the email contents.", e);
			}
			catch (MessagingException e) {
				throw new IllegalStateException("Error while retrieving the email contents.", e);
			}

			if (content instanceof String) {

				if (Part.ATTACHMENT.equalsIgnoreCase(disposition)) {
					emailFragments.add(new EmailFragment(directory, i + "-" + filename, content));
					LOGGER.info(String.format("Handdling attachment '%s', type: '%s'", filename, contentType));
				}
				else {

					final String textFilename;
					final ContentType ct;

					try {
						ct = new ContentType(contentType);
					}
					catch (ParseException e) {
						throw new IllegalStateException("Error while parsing content type '" + contentType + "'.", e);
					}

					if ("text/plain".equalsIgnoreCase(ct.getBaseType())) {
						textFilename = "message.txt";
					}
					else if ("text/html".equalsIgnoreCase(ct.getBaseType())) {
						textFilename = "message.html";
					}
					else {
						textFilename = "message.other";
					}

					emailFragments.add(new EmailFragment(directory, textFilename, content));
				}


			}
			else if (content instanceof InputStream) {

				final InputStream inputStream = (InputStream) content;
				final ByteArrayOutputStream bis = new ByteArrayOutputStream();

				try {
					IOUtils.copy(inputStream, bis);
				}
				catch (IOException e) {
					throw new IllegalStateException("Error while copying input stream to the ByteArrayOutputStream.", e);
				}

				emailFragments.add(new EmailFragment(directory, filename, bis.toByteArray()));

			}
			else if (content instanceof javax.mail.Message)  {
				handleMessage(directory, (javax.mail.Message) content, emailFragments);
			}
			else if (content instanceof Multipart) {
				final Multipart mp2 = (Multipart) content;
				handleMultipart(directory, mp2, mailMessage, emailFragments);
			}
			else {
				throw new IllegalStateException("Content type not handled: " + content.getClass().getSimpleName());
			}
		}

	}


}
