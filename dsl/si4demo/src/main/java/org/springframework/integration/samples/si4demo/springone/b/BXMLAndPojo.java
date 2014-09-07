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
package org.springframework.integration.samples.si4demo.springone.b;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;

/**
 *
 * @author Gary Russell
 *
 */
public class BXMLAndPojo {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx =
				new ClassPathXmlApplicationContext("BXMLAndPojo-context.xml");
		System.out.println(ctx.getBean(FooService.class).foo("foo"));
		ctx.close();
	}

	public static interface FooService {

		String foo(String request);

	}

	@MessageEndpoint
	public static class MyComponents {

		@Transformer(inputChannel="foo", outputChannel="bar")
		public String transform(String foo) {
			return foo + foo;
		}

		@ServiceActivator(inputChannel="bar")
		public String service(String foo) {
			return foo.toUpperCase();
		}

	}

}
