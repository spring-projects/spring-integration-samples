/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 */

package org.springframework.integration.samples.mailattachments.support;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.Assert;

import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.internet.ContentType;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.ParseException;

/**
 * Utility Class for parsing mail messages.
 *
 * @author Gunnar Hillert
 * @author Gary Russell
 * @author Artem Bilan
 *
 * @since 2.2
 *
 */
public final class EmailParserUtils {

	private static final Log LOGGER = LogFactory.getLog(EmailParserUtils.class);

	/** Prevent instantiation. */
	private EmailParserUtils() {
		throw new AssertionError();
	}

	/**
	 * Parses a mail message. The respective message can either be the root message
	 * or another message that is attached to another message.
	 *
	 * If the mail message is an instance of {@link String}, then a {@link EmailFragment}
	 * is being created using the email message's subject line as the file name,
	 * which will contain the mail message's content.
	 *
	 * If the mail message is an instance of {@link Multipart} then we delegate
	 * to {@link #handleMultipart(File, Multipart, Message, List)}.
	 *
	 * @param directory The directory for storing the message. If null this is the root message.
	 * @param mailMessage The mail message to be parsed. Must not be null.
	 * @param emailFragments Must not be null.
	 */
	public static void handleMessage(final File directory,
			final jakarta.mail.Message mailMessage,
			final List<EmailFragment> emailFragments) {

		Assert.notNull(mailMessage, "The mail message to be parsed must not be null.");
		Assert.notNull(emailFragments, "The collection of email fragments must not be null.");

		try {
			Object content = mailMessage.getContent();
			String subject = mailMessage.getSubject();
			File directoryToUse = (directory == null) ? new File(subject) : new File(directory, subject);

			handleContent(content, directoryToUse, mailMessage, emailFragments);

		} catch (IOException | MessagingException e) {
			throw new IllegalStateException("Error while retrieving the email contents.", e);
		}
	}

	private static void handleContent(Object content, File directoryToUse, jakarta.mail.Message mailMessage, List<EmailFragment> emailFragments) {
		if (content instanceof String) {
			emailFragments.add(new EmailFragment(new File(directoryToUse.getName()), "message.txt", content));
		} else if (content instanceof Multipart) {
			handleMultipart(directoryToUse, (Multipart) content, mailMessage, emailFragments);
		} else {
			throw new IllegalStateException("This content type is not handled - " + content.getClass().getSimpleName());
		}
	}

	private static void handleBodyPart(File directory, BodyPart bp, jakarta.mail.Message mailMessage, List<EmailFragment> emailFragments) {
		try {
			final String contentType = bp.getContentType();
			String filename = bp.getFileName();
			final String disposition = bp.getDisposition();

			if (filename == null && bp instanceof MimeBodyPart) {
				filename = ((MimeBodyPart) bp).getContentID();
			}

			logBodyPartInfo(contentType, filename, disposition);

			if (Part.ATTACHMENT.equalsIgnoreCase(disposition)) {
				handleAttachment(directory, filename, contentType, bp, emailFragments);
			}
			else {
				handleNonAttachment(directory, contentType, bp, emailFragments);
			}
		} catch (MessagingException e) {
			throw new IllegalStateException("Unable to retrieve body part meta data.", e);
		} catch (IOException e) {
			throw new IllegalStateException("Error while retrieving the email contents.", e);
		}
	}

	private static void handleAttachment(File directory, String filename, String contentType, BodyPart bp, List<EmailFragment> emailFragments)
			throws MessagingException, IOException {
		LOGGER.info("Handling attachment '{}', type: '{}'", filename, contentType);

		Object content = bp.getContent();
		if (content instanceof String) {
			emailFragments.add(new EmailFragment(directory, filename, content));
		}
		else if (content instanceof InputStream) {
			createEmailFragmentFromInputStream(directory, filename, (InputStream) content, emailFragments);
		}
	}

	private static void handleNonAttachment(File directory, String contentType, BodyPart bp, List<EmailFragment> emailFragments)
			throws MessagingException, IOException {
		Object content = bp.getContent();
		if (content instanceof String) {
			createEmailFragmentFromString(directory, contentType, content, emailFragments);
		}
		else if (content instanceof InputStream) {
			String filename = "message.data";
			createEmailFragmentFromInputStream(directory, filename, (InputStream) content, emailFragments);
		}
	}

	private static void createEmailFragmentFromInputStream(File directory, String filename, InputStream inputStream, List<EmailFragment> emailFragments) throws IOException {
		final ByteArrayOutputStream bis = new ByteArrayOutputStream();

		try {
			IOUtils.copy(inputStream, bis);
		}
		catch (IOException e) {
			throw new IllegalStateException("Error while copying input stream to the ByteArrayOutputStream.", e);
		}

		emailFragments.add(new EmailFragment(directory, filename, bis.toByteArray()));
	}

	private static void createEmailFragmentFromString(File directory, String contentType, Object content, List<EmailFragment> emailFragments) throws ParseException {
		final String textFilename;
		final ContentType ct = new ContentType(contentType);

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

	private static void logBodyPartInfo(String contentType, String filename, String disposition) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BodyPart - Content Type: '{}', filename: '{}', disposition: '{}'",
					contentType, filename, disposition);
		}
	}

	/**
	 * Parses any {@link Multipart} instances that contain text or Html attachments,
	 * {@link InputStream} instances, additional instances of {@link Multipart}
	 * or other attached instances of {@link jakarta.mail.Message}.
	 *
	 * Will create the respective {@link EmailFragment}s representing those attachments.
	 *
	 * Instances of {@link jakarta.mail.Message} are delegated to
	 * {@link #handleMessage(File, jakarta.mail.Message, List)}. Further instances
	 * of {@link Multipart} are delegated to
	 * {@link #handleMultipart(File, Multipart, jakarta.mail.Message, List)}.
	 *
	 * @param directory Must not be null
	 * @param multipart Must not be null
	 * @param mailMessage Must not be null
	 * @param emailFragments Must not be null
	 */
	public static void handleMultipart(File directory, Multipart multipart, jakarta.mail.Message mailMessage, List<EmailFragment> emailFragments) {

		Assert.notNull(directory, "The directory must not be null.");
		Assert.notNull(multipart, "The multipart object to be parsed must not be null.");
		Assert.notNull(mailMessage, "The mail message to be parsed must not be null.");
		Assert.notNull(emailFragments, "The collection of email fragments must not be null.");

		try {
			int count = multipart.getCount();

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Number of enclosed BodyPart objects: {}.", count);
			}

			for (int i = 0; i < count; i++) {
				BodyPart bp = multipart.getBodyPart(i);
				processBodyPart(directory, bp, mailMessage, emailFragments);
			}
		} catch (MessagingException | IOException e) {
			throw new IllegalStateException("Error while retrieving the email contents.", e);
		}
	}

	private static void processBodyPart(File directory, BodyPart bp, jakarta.mail.Message mailMessage, List<EmailFragment> emailFragments) throws MessagingException, IOException {
		Object content = bp.getContent();

		if (content instanceof String || content instanceof InputStream) {
			handleBodyPart(directory, bp, mailMessage, emailFragments);
		}
		else if (content instanceof jakarta.mail.Message message) {
			handleMessage(directory, message, emailFragments);
		}
		else if (content instanceof Multipart) {
			handleMultipart(directory, (Multipart) content, mailMessage, emailFragments);
		}
		else {
			throw new IllegalStateException("Content type not handled: " + content.getClass().getSimpleName());
		}
	}

}
