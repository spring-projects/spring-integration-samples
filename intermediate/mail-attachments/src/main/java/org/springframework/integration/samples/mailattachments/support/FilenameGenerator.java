/*
 * Copyright 2002-2012 the original author or authors.
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
package org.springframework.integration.samples.mailattachments.support;

import java.io.File;

import org.springframework.integration.Message;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.util.Assert;

/**
 * This custom implementation of the {@FileNameGenerator} is necessary to work
 * around Jira issue: https://jira.springsource.org/browse/INT-805
 *
 * @author Gunnar Hillert
 * @since 2.2
 *
 */
public class FilenameGenerator implements FileNameGenerator {

	private String baseDirectory = "";

	public String generateFileName(Message<?> message) {

		File directory = (File) message.getHeaders().get("directory");

		new File(new File(baseDirectory), directory.getPath()).mkdirs();

		return directory.getPath() + "/" + message.getHeaders().get(FileHeaders.FILENAME);

	}

	public void setBaseDirectory(String baseDirectory) {
		Assert.notNull(baseDirectory);
		this.baseDirectory = baseDirectory;
	}

}
