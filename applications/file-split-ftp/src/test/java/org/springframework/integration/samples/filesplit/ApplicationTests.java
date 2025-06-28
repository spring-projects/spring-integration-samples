/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 */

package org.springframework.integration.samples.filesplit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.integration.test.util.TestUtils;
import org.springframework.test.annotation.DirtiesContext;

import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringIntegrationTest(noAutoStartup = "fileInboundChannelAdapter")
class ApplicationTests {

	private static GreenMail mailServer;

	@Autowired
	private Session<FTPFile> session;

	@Autowired
	private SourcePollingChannelAdapter fileInboundChannelAdapter;

	@BeforeAll
	static void setup() {
		ServerSetup smtp = ServerSetupTest.SMTP.dynamicPort();
		smtp.setServerStartupTimeout(10000);
		mailServer = new GreenMail(smtp);
		mailServer.setUser("bar@bar@baz", "user", "pw");
		mailServer.start();
		// Configure the boot property to send email to the test email server.
		System.setProperty("spring.mail.port", Integer.toString(mailServer.getSmtp().getPort()));
	}

	@AfterAll
	static void tearDown() {
		mailServer.stop();
	}

	@BeforeEach
	void beforeTest() throws FolderException, IOException {
		mailServer.purgeEmailFromAllMailboxes();
		cleanup();
		this.fileInboundChannelAdapter.start();
	}

	@AfterEach
	void cleanup() throws IOException {
		File inDir = new File("/tmp/in");
		if (inDir.exists()) {
			FileUtils.cleanDirectory(inDir);
		}

		File outDir = new File("/tmp/out");
		if (outDir.exists()) {
			FileUtils.cleanDirectory(outDir);
		}
	}

	@Test
	void testSuccess() throws Exception {
		assertThat(runTest(false).getSubject()).isEqualTo("File successfully split and transferred");
		assertThat(verifyMail().getSubject()).isEqualTo("File successfully split and transferred"); // Assertion added here
	}

	@Test
	void testFailure() throws Exception {
		willThrow(new RuntimeException("fail test exception"))
				.given(this.session).write(any(InputStream.class), eq("foo/002.txt.writing"));
		MimeMessage message = runTest(true);
		assertThat(message.getSubject()).isEqualTo("File split and transfer failed");
		assertThat(message.getContent()).asString().contains("fail test exception");
		assertThat(message.getContent()).asString().contains(TestUtils.applySystemFileSeparator("/tmp/out/002.txt"));
	}

	/*
	 * Create a test file containing one row per account.
	 * Verify the three files appear, with the correct contents.
	 * Verify the input file was renamed based on success/failure.
	 * Verify the email was sent.
	 */
	 MimeMessage runTest(boolean fail) throws Exception {
		File in = new File("/tmp/in/", "foo");
		FileOutputStream fos = new FileOutputStream(in);
		fos.write("*002,foo,bar\n*006,baz,qux\n*009,fiz,buz\n".getBytes());
		fos.close();
		in.renameTo(new File("/tmp/in/", "foo.txt"));
		File out = new File("/tmp/out/002.txt");
		int n = 0;
		while (n++ < 100 && (!out.exists() || out.length() < 12)) {
			Thread.sleep(100);
		}
		assertThat(out.exists()).isTrue();
		BufferedReader br = new BufferedReader(new FileReader(out));
		assertThat(br.readLine()).isEqualTo("*002,foo,bar");
		br.close();
		out = new File("/tmp/out/006.txt");
		n = 0;
		while (n++ < 100 && (!out.exists() || out.length() < 12)) {
			Thread.sleep(100);
		}
		assertThat(out.exists()).isTrue();
		br = new BufferedReader(new FileReader(out));
		assertThat(br.readLine()).isEqualTo("*006,baz,qux");
		br.close();
		out = new File("/tmp/out/009.txt");
		n = 0;
		while (n++ < 100 && (!out.exists() || out.length() < 12)) {
			Thread.sleep(100);
		}
		assertThat(out.exists()).isTrue();
		br = new BufferedReader(new FileReader(out));
		assertThat(br.readLine()).isEqualTo("*009,fiz,buz");
		br.close();
		if (!fail) {
			in = new File("/tmp/in/", "foo.txt.success");
		}
		else {
			in = new File("/tmp/in/", "foo.txt.failed");
		}
		n = 0;
		while (n++ < 100 && !in.exists()) {
			Thread.sleep(100);
		}
		assertThat(in.exists()).isTrue();
		// verify FTP
		verify(this.session).write(any(InputStream.class), eq("foo/002.txt.writing"));
		if (!fail) {
			verify(this.session).write(any(InputStream.class), eq("foo/006.txt.writing"));
			verify(this.session).write(any(InputStream.class), eq("foo/009.txt.writing"));
			verify(this.session).rename("foo/002.txt.writing", "foo/002.txt");
			verify(this.session).rename("foo/006.txt.writing", "foo/006.txt");
			verify(this.session).rename("foo/009.txt.writing", "foo/009.txt");
		}

		assertThat(verifyMail().getFrom()).containsOnly(new InternetAddress("foo@bar"));
		assertThat(verifyMail().getRecipients(MimeMessage.RecipientType.TO)).containsOnly(new InternetAddress("bar@baz"));
		return verifyMail();
	}

	 MimeMessage verifyMail() {
		mailServer.waitForIncomingEmail(10000, 1);
		MimeMessage[] mail = mailServer.getReceivedMessagesForDomain("baz");
		assertThat(mail).hasSize(1);
		return mail[0];
	}

	/**
	 * Overrides the ftp session factories with mocks.
	 *
	 */
	@Configuration
	@Import(Application.class)
	 static class Config {

		@Bean
		SessionFactory<FTPFile> ftp1() {
			return mockSf();
		}

		@Bean
		SessionFactory<FTPFile> ftp2() {
			return mockSf();
		}

		@Bean
		SessionFactory<FTPFile> ftp3() {
			return mockSf();
		}

		 SessionFactory<FTPFile> mockSf() {
			@SuppressWarnings("unchecked")
			SessionFactory<FTPFile> mocksf = mock(SessionFactory.class);
			given(mocksf.getSession()).willReturn(mockSession());
			return mocksf;
		}

		@Bean
		@SuppressWarnings("unchecked")
		Session<FTPFile> mockSession() {
			return mock(Session.class);
		}

	}

}
