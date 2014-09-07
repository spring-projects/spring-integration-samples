/*
 * Copyright 2014 the original author or authors.
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
package org.springframework.integration.samples.si4demo.springone.f;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.samples.si4demo.springone.GMailProperties;

/**
 *
 * @author Gary Russell
 *
 */
@Configuration
@EnableConfigurationProperties(GMailProperties.class)
@EnableAutoConfiguration
@IntegrationComponentScan
public class FMail {

	@Autowired
	GMailProperties gmail;

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx =
				SpringApplication.run(FMail.class);
		System.out.println(ctx.getBean(FooService.class).foo("foo"));
		ctx.close();
	}

	@MessagingGateway(defaultRequestChannel="foo.input")
	public static interface FooService {

		String foo(String request);

	}

	@Bean
	IntegrationFlow foo() {
		return f -> f
			.transform("payload + payload")
			.handle(String.class, (p, h) -> p.toUpperCase())
			//RLR
			;
	}

}
