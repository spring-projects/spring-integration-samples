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

package org.springframework.integration.samples.sftp;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.integration.file.remote.RemoteFileTemplate;
import org.springframework.integration.file.remote.SessionCallback;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * @author Gary Russell
 * @author Artem Bilan
 *
 * @since 4.1
 *
 */
public class SftpTestUtils {

	public static void createTestFiles(RemoteFileTemplate<LsEntry> template, final String... fileNames) {
		if (template != null) {
			final ByteArrayInputStream stream = new ByteArrayInputStream("foo".getBytes());
			template.execute((SessionCallback<LsEntry, Void>) session -> {
				try {
					session.mkdir("si.sftp.sample");
				}
				catch (Exception e) {
					assertThat(e.getMessage(), containsString("failed to create"));
				}
				for (int i = 0; i < fileNames.length; i++) {
					stream.reset();
					session.write(stream, "si.sftp.sample/" + fileNames[i]);
				}
				return null;
			});
		}
	}

	public static void cleanUp(RemoteFileTemplate<LsEntry> template, final String... fileNames) {
		if (template != null) {
			template.execute((SessionCallback<LsEntry, Void>) session -> {
				for (int i = 0; i < fileNames.length; i++) {
					try {
						session.remove("si.sftp.sample/" + fileNames[i]);
					}
					catch (IOException e) {}
				}

				// should be empty
				session.rmdir("si.sftp.sample");
				return null;
			});
		}
	}

	public static boolean fileExists(RemoteFileTemplate<LsEntry> template, final String... fileNames) {
		if (template != null) {
			return template.execute(session -> {
				ChannelSftp channel = (ChannelSftp) session.getClientInstance();
				for (int i = 0; i < fileNames.length; i++) {
					try {
						SftpATTRS stat = channel.stat("si.sftp.sample/" + fileNames[i]);
						if (stat == null) {
							System.out.println("stat returned null for " + fileNames[i]);
							return false;
						}
					}
					catch (SftpException e) {
						System.out.println("Remote file not present: " + e.getMessage() + ": " + fileNames[i]);
						return false;
					}
				}
				return true;
			});
		}
		else {
			return false;
		}
	}

}
