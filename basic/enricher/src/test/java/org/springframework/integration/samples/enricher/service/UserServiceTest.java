/*
 * Copyright 2002-2010 the original author or authors.
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

package org.springframework.integration.samples.enricher.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.samples.enricher.domain.User;

/**
 * Verify that the Spring Integration Application Context starts successfully.
 */
public class UserServiceTest {

	@Test
	public void testStartupOfSpringInegrationContext() throws Exception {
		final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"/META-INF/spring/integration/spring-integration-context.xml", UserServiceTest.class);
		Thread.sleep(2000);
		context.close();
	}

	@Test
	public void testExecuteFindUser() {

		final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"/META-INF/spring/integration/spring-integration-context.xml", UserServiceTest.class);

		final UserService service = context.getBean(UserService.class);

		User user = new User("foo", null, null);
		final User fullUser = service.findUser(user);

		assertEquals("foo", fullUser.getUsername());
		assertEquals("foo@springintegration.org", fullUser.getEmail());
		assertEquals("secret", fullUser.getPassword());
		context.close();

	}

	@Test
	public void testExecuteFindUserByUsername() {

		final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"/META-INF/spring/integration/spring-integration-context.xml", UserServiceTest.class);

		final UserService service = context.getBean(UserService.class);

		User user = new User("foo", null, null);
		final User fullUser = service.findUserByUsername(user);

		assertEquals("foo", fullUser.getUsername());
		assertEquals("foo@springintegration.org", fullUser.getEmail());
		assertEquals("secret", fullUser.getPassword());
		context.close();

	}

}
