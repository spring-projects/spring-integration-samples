/*
 * Copyright 2002-present the original author or authors.
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

package org.springframework.integration.samples.ftp.support;

import java.util.Arrays;

import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.AnonymousAuthentication;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.AbstractUserManager;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;

/**
 *
 * @author Gunnar Hillert
 *
 */
public class TestUserManager extends AbstractUserManager {

	private final BaseUser testUser;

	private final BaseUser anonUser;

	private static final String TEST_USERNAME = "demo";

	private static final String TEST_PASSWORD = "demo";

	public TestUserManager(String homeDirectory) {
		super("admin", new ClearTextPasswordEncryptor());

		this.testUser = new BaseUser();
		this.testUser.setAuthorities(Arrays.asList(new Authority[] {new ConcurrentLoginPermission(1, 1), new WritePermission()}));
		this.testUser.setEnabled(true);
		this.testUser.setHomeDirectory(homeDirectory);
		this.testUser.setMaxIdleTime(10000);
		this.testUser.setName(TEST_USERNAME);
		this.testUser.setPassword(TEST_PASSWORD);

		this.anonUser = new BaseUser(this.testUser);
		this.anonUser.setName("anonymous");
	}

	public User getUserByName(String username) throws FtpException {
		if (TEST_USERNAME.equals(username)) {
			return this.testUser;
		}
		else if (this.anonUser.getName().equals(username)) {
			return this.anonUser;
		}

		return null;
	}

	public String[] getAllUserNames() throws FtpException {
		return new String[] {TEST_USERNAME, this.anonUser.getName()};
	}

	public void delete(String username) throws FtpException {
		throw new UnsupportedOperationException("Deleting of FTP Users is not supported.");
	}

	public void save(User user) throws FtpException {
		throw new UnsupportedOperationException("Saving of FTP Users is not supported.");
	}

	public boolean doesExist(String username) throws FtpException {
		return TEST_USERNAME.equals(username) || this.anonUser.getName().equals(username);
	}

	public User authenticate(Authentication authentication) throws AuthenticationFailedException {
		if (UsernamePasswordAuthentication.class.isAssignableFrom(authentication.getClass())) {
			UsernamePasswordAuthentication upAuth = (UsernamePasswordAuthentication) authentication;

			if (TEST_USERNAME.equals(upAuth.getUsername()) && TEST_PASSWORD.equals(upAuth.getPassword())) {
				return this.testUser;
			}

			if (this.anonUser.getName().equals(upAuth.getUsername())) {
				return this.anonUser;
			}
		}
		else if (AnonymousAuthentication.class.isAssignableFrom(authentication.getClass())) {
			return this.anonUser;
		}

		return null;
	}
}
