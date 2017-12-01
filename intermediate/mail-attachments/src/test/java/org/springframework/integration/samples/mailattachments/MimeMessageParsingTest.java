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

package org.springframework.integration.samples.mailattachments;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.integration.samples.mailattachments.support.EmailFragment;
import org.springframework.integration.samples.mailattachments.support.EmailParserUtils;
import org.springframework.util.SocketUtils;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * Test to verify the correct parsing of Email Messages.
 *
 * @author Gunnar Hillert
 * @author Gary Russell
 * @author Artem Bilan
 *
 * @since 2.2
 */
public class MimeMessageParsingTest {

	private static final Log LOGGER = LogFactory.getLog(MimeMessageParsingTest.class);

	private Wiser wiser;

	private int wiserPort;

	@Before
	public void startWiser() {

		this.wiserPort = SocketUtils.findAvailableTcpPort(2500);

		wiser = new Wiser();
		wiser.setPort(this.wiserPort);
		wiser.start();
		LOGGER.info("Wiser was started.");

	}

	/**
	 * This test will create a Mime Message that contains an Attachment, send it
	 * to an SMTP Server (Using Wiser) and retrieve and process the Mime Message.
	 *
	 * This test verifies that the parsing of the retrieved Mime Message is
	 * successful and that the correct number of {@link EmailFragment}s is created.
	 */
	@Test
	public void testProcessingOfEmailAttachments() {

		final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setPort(this.wiserPort);

		final MimeMessage message = mailSender.createMimeMessage();
		final String pictureName = "picture.png";

		final ByteArrayResource byteArrayResource = getFileData(pictureName);

		try {

			final MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom("testfrom@springintegration.org");
			helper.setTo("testto@springintegration.org");
			helper.setSubject("Parsing of Attachments");
			helper.setText("Spring Integration Rocks!");

			helper.addAttachment(pictureName, byteArrayResource, "image/png");

		}
		catch (MessagingException e) {
			throw new MailParseException(e);
		}

		mailSender.send(message);

		final List<WiserMessage> wiserMessages = wiser.getMessages();

		Assert.assertTrue(wiserMessages.size() == 1);

		boolean foundTextMessage = false;
		boolean foundPicture = false;

		for (WiserMessage wiserMessage : wiserMessages) {

				final List<EmailFragment> emailFragments = new ArrayList<EmailFragment>();

				try {
					final MimeMessage mailMessage = wiserMessage.getMimeMessage();
					EmailParserUtils.handleMessage(null, mailMessage, emailFragments);
				}
				catch (MessagingException e) {
					throw new IllegalStateException("Error while retrieving Mime Message.");
				}

				Assert.assertTrue(emailFragments.size() == 2);

				for (EmailFragment emailFragment : emailFragments) {
					if ("picture.png".equals(emailFragment.getFilename())) {
						foundPicture = true;
					}

					if ("message.txt".equals(emailFragment.getFilename())) {
						foundTextMessage = true;
					}
				}

				Assert.assertTrue(foundPicture);
				Assert.assertTrue(foundTextMessage);

		}
	}

	/**
	 * This test will create a Mime Message that in return contains another
	 * mime message. The nested mime message contains an attachment.
	 *
	 * The root message consist of both HTML and Text message.
	 *
	 */
	@Test
	public void testProcessingOfNestedEmailAttachments() {

		final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setPort(this.wiserPort);

		final MimeMessage rootMessage = mailSender.createMimeMessage();

		try {

			final MimeMessageHelper messageHelper = new MimeMessageHelper(rootMessage, true);

			messageHelper.setFrom("testfrom@springintegration.org");
			messageHelper.setTo("testto@springintegration.org");
			messageHelper.setSubject("Parsing of Attachments");
			messageHelper.setText("Spring Integration Rocks!", "Spring Integration <b>Rocks</b>!");

			final String pictureName = "picture.png";

			final ByteArrayResource byteArrayResource = getFileData(pictureName);

			messageHelper.addInline("picture12345", byteArrayResource, "image/png");

		}
		catch (MessagingException e) {
			throw new MailParseException(e);
		}

		mailSender.send(rootMessage);

		final List<WiserMessage> wiserMessages = wiser.getMessages();

		Assert.assertTrue(wiserMessages.size() == 1);

		boolean foundTextMessage = false;
		boolean foundPicture = false;
		boolean foundHtmlMessage = false;

		for (WiserMessage wiserMessage : wiserMessages) {

			List<EmailFragment> emailFragments = new ArrayList<EmailFragment>();

			try {

				final MimeMessage mailMessage = wiserMessage.getMimeMessage();
				EmailParserUtils.handleMessage(null, mailMessage, emailFragments);

			} catch (MessagingException e) {
				throw new IllegalStateException("Error while retrieving Mime Message.");
			}

			Assert.assertTrue(emailFragments.size() == 3);

			for (EmailFragment emailFragment : emailFragments) {
				if ("<picture12345>".equals(emailFragment.getFilename())) {
					foundPicture = true;
				}

				if ("message.txt".equals(emailFragment.getFilename())) {
					foundTextMessage = true;
				}

				if ("message.html".equals(emailFragment.getFilename())) {
					foundHtmlMessage = true;
				}
			}

			Assert.assertTrue(foundPicture);
			Assert.assertTrue(foundTextMessage);
			Assert.assertTrue(foundHtmlMessage);

		}
	}

	private ByteArrayResource getFileData(String filename) {

		final InputStream attachmentInputStream = MimeMessageParsingTest.class.getResourceAsStream(filename);

		Assert.assertNotNull("Resource not found: " + filename, attachmentInputStream);

		ByteArrayResource byteArrayResource = null;

		try {
			byteArrayResource = new ByteArrayResource(IOUtils.toByteArray(attachmentInputStream));
			attachmentInputStream.close();
		}
		catch (IOException e1) {
			Assert.fail();
		}

		return byteArrayResource;

	}

	@After
	public void stopWiser() {
		wiser.stop();
		LOGGER.info("Wiser stopped.");
	}

}
