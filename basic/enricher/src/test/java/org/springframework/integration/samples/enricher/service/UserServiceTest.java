/*
 * Copyright 2011-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.samples.enricher.service;

import org.junit.jupiter.api.Test;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.enricher.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verify that the Spring Integration Application Context starts successfully
 * with both Java and XML configurations.
 * 
 * @author Glenn Renfro
 */
public class UserServiceTest {

	@Test
	public void testStartupOfSpringIntegrationContextWithJavaConfig() throws Exception {
		AnnotationConfigApplicationContext context = createJavaConfigContext();
		Thread.sleep(2000);
		context.close();
	}

	@Test
	public void testStartupOfSpringIntegrationContextWithXmlConfig() throws Exception {
		final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"/META-INF/spring/integration/spring-integration-context.xml", UserServiceTest.class);
		Thread.sleep(2000);
		context.close();
	}

	@Test
	public void testExecuteFindUserWithJavaConfig() {
		AnnotationConfigApplicationContext context = createJavaConfigContext();

		runFindUserTest(context);
		context.close();
	}

	@Test
	public void testExecuteFindUserWithXmlConfig() {
		final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"/META-INF/spring/integration/spring-integration-context.xml", UserServiceTest.class);

		runFindUserTest(context);
		context.close();
	}

	@Test
	public void testExecuteFindUserByUsernameWithJavaConfig() {
		AnnotationConfigApplicationContext context = createJavaConfigContext();

		runFindUserByUsernameTest(context);
		context.close();
	}

	@Test
	public void testExecuteFindUserByUsernameWithXmlConfig() {
		final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"/META-INF/spring/integration/spring-integration-context.xml", UserServiceTest.class);

		runFindUserByUsernameTest(context);
		context.close();
	}

	private void runFindUserTest(ConfigurableApplicationContext context) {
		final UserService service = context.getBean(UserService.class);

		User user = new User("foo", null, null);
		final User fullUser = service.findUser(user);

		assertThat(fullUser.getUsername()).isEqualTo("foo");
		assertThat(fullUser.getEmail()).isEqualTo("foo@springintegration.org");
		assertThat(fullUser.getPassword()).isEqualTo("secret");
	}

	private void runFindUserByUsernameTest(ConfigurableApplicationContext context) {
		final UserService service = context.getBean(UserService.class);

		User user = new User("foo", null, null);
		final User fullUser = service.findUserByUsername(user);

		assertThat(fullUser.getUsername()).isEqualTo("foo");
		assertThat(fullUser.getEmail()).isEqualTo("foo@springintegration.org");
		assertThat(fullUser.getPassword()).isEqualTo("secret");
	}


	private AnnotationConfigApplicationContext createJavaConfigContext() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("java-config");
		context.scan("org.springframework.integration.samples.enricher.config");
		context.refresh();
		return context;
	}
}
