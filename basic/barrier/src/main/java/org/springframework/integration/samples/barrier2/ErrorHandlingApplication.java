/*
 * Copyright 2015-2017 the original author or authors.
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

package org.springframework.integration.samples.barrier2;

import java.util.Arrays;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Gary Russell
 * @since 4.2
 */
@SpringBootApplication
@ImportResource("/META-INF/spring/integration/errorhandling-context.xml")
public class ErrorHandlingApplication {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext test = new SpringApplicationBuilder(ErrorHandlingApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
		Gateway gateway = test.getBean(Gateway.class);
		try {
			gateway.process(Arrays.asList(2, 0, 2, 0, 2), "foo");
		}
		catch (Exception e) {
			System.err.println(e.toString());
		}
		test.close();
	}

}
