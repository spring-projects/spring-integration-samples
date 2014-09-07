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
package org.springframework.integration.samples.si4demo.springone.c;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.integration.transformer.MessageTransformingHandler;
import org.springframework.integration.transformer.MethodInvokingTransformer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 *
 * @author Gary Russell
 *
 */
public class CNoXML {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx =
				new AnnotationConfigApplicationContext(CConfig.class);
		System.out.println(ctx.getBean(FooService.class).foo("foo"));
		ctx.close();
	}

	@MessagingGateway(defaultRequestChannel="foo")
	public static interface FooService {

		String foo(String request);

	}

	@Configuration
	@EnableIntegration
	@IntegrationComponentScan
	public static class CConfig {

		@Bean
		public MessageChannel foo() {
			return new DirectChannel();
		}

		@Transformer(inputChannel="foo")
		@Bean
		public MessageHandler transform() {
			MessageTransformingHandler transformingHandler = new MessageTransformingHandler(new MethodInvokingTransformer(helpers(), "duplicate"));
			transformingHandler.setOutputChannel(bar());
			return transformingHandler;
		}

		@Bean
		public MessageChannel bar() {
			return new DirectChannel();
		}

		@ServiceActivator(inputChannel="bar")
		@Bean
		public MessageHandler service() {
			return new ServiceActivatingHandler(new MethodInvokingTransformer(helpers(), "upper"));
		}

		@Bean
		public MyHelpers helpers() {
			return new MyHelpers();
		}

	}

	public static class MyHelpers {

		public String duplicate(String foo) {
			return foo + foo;
		}

		public String upper(String foo) {
			return foo.toUpperCase();
		}

	}

}
