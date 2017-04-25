/*
 * Copyright 2014-2017 the original author or authors.
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

package org.springframework.integration.samples.si4demo.springone.g;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mail.MailHeaders;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.integration.samples.si4demo.springone.GMailProperties;

/**
 *
 * @author Gary Russell
 *
 */
@Configuration
@EnableConfigurationProperties(GMailProperties.class)

@EnableAutoConfiguration
public class GIMAP {

	@Autowired
	GMailProperties gmail;

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx =
				new SpringApplicationBuilder(GIMAP.class)
						.web(WebApplicationType.NONE)
						.run(args);
		System.out.println("Hit Enter to terminate");
		System.in.read();
		ctx.close();
	}

	@Bean
	IntegrationFlow imapIdle() {
		return IntegrationFlows.from(Mail.imapIdleAdapter(
							"imaps://"
							+ gmail.getUser().replaceAll("@", "%40")
							+ ":"
							+ gmail.getPassword()
							+ "@imap.gmail.com:993/INBOX")
						.id("imapIn")
						.autoStartup(true)
						.javaMailProperties(p ->
							 p.put("mail.debug", "false")))
				.enrichHeaders(s -> s.headerExpressions(h -> h
						.put(MailHeaders.SUBJECT, "payload.subject")
						.put(MailHeaders.FROM, "payload.from[0].toString()")))
				.transform("payload.content")
				.handle(System.out::println)
				.get();
	}

}
