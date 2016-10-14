/*
 * Copyright 2002-2016 the original author or authors.
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

package org.springframework.integration.samples.filesplit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.net.ftp.FTPFile;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.test.mail.TestMailServer;
import org.springframework.integration.test.mail.TestMailServer.SmtpServer;
import org.springframework.integration.test.util.TestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ApplicationTests {

	private static final SmtpServer smtpServer = TestMailServer.smtp(0);

	@Autowired
	private Session<FTPFile> session;

	@BeforeClass
	public static void setup() {
		// Configure the boot property to send email to the test email server.
		System.setProperty("spring.mail.port", Integer.toString(smtpServer.getPort()));
		new File("/tmp/in/foo.txt").delete();
		new File("/tmp/in/foo.txt.success").delete();
		new File("/tmp/in/foo.txt.failed").delete();
	}

	@Before
	public void beforeTest() {
		smtpServer.getMessages().clear();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSuccess() throws Exception {
		reset(this.session);
		String message = runTest(false);
		assertThat(message).contains("File successfully split and transferred");
		assertThat(message).contains(TestUtils.applySystemFileSeparator("/tmp/in/foo.txt"));
	}

	@Test
	public void testFailure() throws Exception {
		willThrow(new RuntimeException("fail test exception"))
			.given(this.session).write(any(InputStream.class), eq("foo/002.txt.writing"));
		String message = runTest(true);
		assertThat(message).contains("File split and transfer failed");
		assertThat(message).contains("fail test exception");
		assertThat(message).contains(TestUtils.applySystemFileSeparator("/tmp/out/002.txt"));
	}

	/*
	 * Create a test file containing one row per account.
	 * Verify the three files appear, with the correct contents.
	 * Verify the input file was renamed based on success/failure.
	 * Verify the email was sent.
	 */
	private String runTest(boolean fail) throws Exception {
		File in = new File("/tmp/in/", "foo");
		FileOutputStream fos = new FileOutputStream(in);
		fos.write("*002,foo,bar\n*006,baz,qux\n*009,fiz,buz\n".getBytes());
		fos.close();
		in.renameTo(new File("/tmp/in/", "foo.txt"));
		File out = new File("/tmp/out/002.txt");
		int n = 0;
		while(n++ < 100 && (!out.exists() || out.length() < 12)) {
			Thread.sleep(100);
		}
		assertThat(out.exists()).isTrue();
		BufferedReader br = new BufferedReader(new FileReader(out));
		assertThat(br.readLine()).isEqualTo("*002,foo,bar");
		br.close();
		out.delete();
		out = new File("/tmp/out/006.txt");
		assertThat(out.exists()).isTrue();
		br = new BufferedReader(new FileReader(out));
		assertThat(br.readLine()).isEqualTo("*006,baz,qux");
		br.close();
		out.delete();
		out = new File("/tmp/out/009.txt");
		assertThat(out.exists()).isTrue();
		br = new BufferedReader(new FileReader(out));
		assertThat(br.readLine()).isEqualTo("*009,fiz,buz");
		br.close();
		out.delete();
		if (!fail) {
			in = new File("/tmp/in/", "foo.txt.success");
		}
		else {
			in = new File("/tmp/in/", "foo.txt.failed");
		}
		n = 0;
		while(n++ < 100 && !in.exists()) {
			Thread.sleep(100);
		}
		assertThat(in.exists()).isTrue();
		in.delete();
		// verify FTP
		verify(this.session).write(any(InputStream.class), eq("foo/002.txt.writing"));
		if (!fail) {
			verify(this.session).write(any(InputStream.class), eq("foo/006.txt.writing"));
			verify(this.session).write(any(InputStream.class), eq("foo/009.txt.writing"));
			verify(this.session).rename("foo/002.txt.writing", "foo/002.txt");
			verify(this.session).rename("foo/006.txt.writing", "foo/006.txt");
			verify(this.session).rename("foo/009.txt.writing", "foo/009.txt");
		}

		String message = verifyMail();
		assertThat(message).contains("From: foo@bar");
		assertThat(message).contains("To: bar@baz");
		return message;
	}

	public String verifyMail() throws Exception {
		List<String> messages = smtpServer.getMessages();
		int n = 0;
		while (n++ < 100 && messages.size() < 1) {
			Thread.sleep(100);
		}
		assertThat(messages).hasSize(1);
		return messages.get(0);
	}

	/**
	 * Overrides the ftp session factories with mocks.
	 *
	 */
	@Configuration
	@Import(Application.class)
	public static class Config {

		@Bean
		public SessionFactory<FTPFile> ftp1() throws IOException {
			return mockSf();
		}

		@Bean
		public SessionFactory<FTPFile> ftp2() throws IOException {
			return mockSf();
		}

		@Bean
		public SessionFactory<FTPFile> ftp3() throws IOException {
			return mockSf();
		}

		private SessionFactory<FTPFile> mockSf() throws IOException {
			@SuppressWarnings("unchecked")
			SessionFactory<FTPFile> mocksf = mock(SessionFactory.class);
			given(mocksf.getSession()).willReturn(mockSession());
			return mocksf;
		}

		@SuppressWarnings("unchecked")
		@Bean
		public Session<FTPFile> mockSession() throws IOException {
			return mock(Session.class);
		}

	}

}
