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

package org.springframework.integration.samples.filesplit;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.net.ftp.FTPFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.file.splitter.FileSplitter;
import org.springframework.integration.ftp.dsl.Ftp;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.http.config.EnableIntegrationGraphController;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;

@SpringBootApplication
@EnableIntegrationGraphController(allowedOrigins = "http://localhost:8082")
public class Application {

	private static final String EMAIL_SUCCESS_SUFFIX = "emailSuccessSuffix";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private MailProperties mailProperties;

	/**
	 * Poll for files, add an error channel, split into lines route the start/end markers
	 * to {@link #markers()} and the lines to {@link #lines}.
	 *
	 * @return the flow.
	 */
	@Bean
	public IntegrationFlow fromFile() {
		return IntegrationFlows.from(
				Files.inboundAdapter(new File("/tmp/in"))
						.preventDuplicates(false)
						.patternFilter("*.txt"), e -> e.poller(Pollers.fixedDelay(5000)
						.errorChannel("tfrErrors.input"))
						.id("fileInboundChannelAdapter"))
				.handle(Files.splitter(true, true))
				.<Object, Class<?>>route(Object::getClass, m -> m
						.channelMapping(FileSplitter.FileMarker.class, "markers.input")
						.channelMapping(String.class, "lines.input"))
				.get();
	}

	/**
	 * Process lines; append (no flush) to the appropriate file.
	 *
	 * @return the flow.
	 */
	@Bean
	public IntegrationFlow lines(FileWritingMessageHandler fileOut) {
		return f -> f.handle(fileOut);
	}

	@Bean
	public FileWritingMessageHandler fileOut() {
		return Files.outboundAdapter("'/tmp/out'")
				.appendNewLine(true)
				.fileNameExpression("payload.substring(1, 4) + '.txt'")
				.get();
	}

	/**
	 * Process file markers; ignore START, when END, flush the files, ftp them and
	 * send an email.
	 *
	 * @return the flow.
	 */
	@Bean
	public IntegrationFlow markers() {
		return f -> f.<FileSplitter.FileMarker>filter(m -> m.getMark().equals(FileSplitter.FileMarker.Mark.END),
				e -> e.id("markerFilter"))
				.publishSubscribeChannel(s -> s

						// first trigger file flushes
						.subscribe(sf -> sf.transform("'/tmp/out/.*\\.txt'", e -> e.id("toTriggerPattern"))
								.trigger("fileOut", e -> e.id("flusher")))

						// send the first file
						.subscribe(sf -> sf.<FileSplitter.FileMarker, File>transform(p -> new File("/tmp/out/002.txt"))
								.enrichHeaders(h -> h.header(FileHeaders.FILENAME, "002.txt", true))
								.handle(Ftp.outboundAdapter(ftp1()).remoteDirectory("foo"), e -> e.id("ftp002")))

						// send the second file
						.subscribe(sf -> sf.<FileSplitter.FileMarker, File>transform(p -> new File("/tmp/out/006.txt"))
								.enrichHeaders(h -> h.header(FileHeaders.FILENAME, "006.txt", true))
								.handle(Ftp.outboundAdapter(ftp2()).remoteDirectory("foo"), e -> e.id("ftp006")))

						// send the third file
						.subscribe(sf -> sf.<FileSplitter.FileMarker, File>transform(p -> new File("/tmp/out/009.txt"))
								.enrichHeaders(h -> h.header(FileHeaders.FILENAME, "009.txt", true))
								.handle(Ftp.outboundAdapter(ftp3()).remoteDirectory("foo"), e -> e.id("ftp009")))

						// send an email
						.subscribe(sf -> sf.transform(FileSplitter.FileMarker::getFilePath)
								.enrichHeaders(Mail.headers()
										.subject("File successfully split and transferred")
										.from("foo@bar")
										.toFunction(m -> new String[] { "bar@baz" }))
								.enrichHeaders(h -> h.header(EMAIL_SUCCESS_SUFFIX, ".success"))
								.channel("toMail.input")));
	}

	@Bean
	public SessionFactory<FTPFile> ftp1() {
		DefaultFtpSessionFactory ftp = new DefaultFtpSessionFactory();
		ftp.setHost("host3");
		ftp.setUsername("user");
		ftp.setPassword("ftp");
		return ftp;
	}

	@Bean
	public SessionFactory<FTPFile> ftp2() {
		DefaultFtpSessionFactory ftp = new DefaultFtpSessionFactory();
		ftp.setHost("host3");
		ftp.setUsername("user");
		ftp.setPassword("ftp");
		return ftp;
	}

	@Bean
	public SessionFactory<FTPFile> ftp3() {
		DefaultFtpSessionFactory ftp = new DefaultFtpSessionFactory();
		ftp.setHost("host3");
		ftp.setUsername("user");
		ftp.setPassword("ftp");
		return ftp;
	}

	/**
	 * Error flow - email failure
	 *
	 * @return the flow.
	 */
	@Bean
	public IntegrationFlow tfrErrors() {
		return f -> f
				.enrichHeaders(Mail.headers()
						.subject("File split and transfer failed")
						.from("foo@bar")
						.toFunction(m -> new String[] { "bar@baz" }))
				.enrichHeaders(h -> h.header(EMAIL_SUCCESS_SUFFIX, ".failed")
						.headerExpression(FileHeaders.ORIGINAL_FILE, "payload.failedMessage.headers['"
								+ FileHeaders.ORIGINAL_FILE + "']"))
				.<MessagingException, String>transform(p ->
						p.getFailedMessage().getPayload().toString() + "\n" + getStackTraceAsString(p))
				.channel("toMail.input");
	}

	@Bean
	public IntegrationFlow toMail() {
		return f -> f
				.handle(Mail.outboundAdapter(this.mailProperties.getHost())
//						.javaMailProperties(b -> b.put("mail.debug", "true"))
								.port(this.mailProperties.getPort())
								.credentials(this.mailProperties.getUsername(), this.mailProperties.getPassword()),
						e -> e.id("mailOut").advice(afterMailAdvice()));
	}

	/**
	 * Rename the input file after success/failure.
	 *
	 * @return the flow.
	 */
	@Bean
	public MethodInterceptor afterMailAdvice() {
		return invocation -> {
			Message<?> message = (Message<?>) invocation.getArguments()[0];
			MessageHeaders headers = message.getHeaders();
			File originalFile = headers.get(FileHeaders.ORIGINAL_FILE, File.class);
			try {
				invocation.proceed();
				originalFile.renameTo(
						new File(originalFile.getAbsolutePath() + headers.get(EMAIL_SUCCESS_SUFFIX)));
			}
			catch (Exception e) {
				originalFile.renameTo(
						new File(originalFile.getAbsolutePath() + headers.get(EMAIL_SUCCESS_SUFFIX) + ".email.failed"));
			}
			return null;
		};
	}

	private String getStackTraceAsString(Throwable cause) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter, true);
		cause.printStackTrace(printWriter);
		return stringWriter.getBuffer().toString();
	}

}
