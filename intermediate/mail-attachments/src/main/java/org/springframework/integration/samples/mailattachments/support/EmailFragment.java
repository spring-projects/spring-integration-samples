/*
 * Copyright 2002-2017 the original author or authors.
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

import org.springframework.util.Assert;

/**
 * Represents a part of the original email message. EmailFragments could be either
 * Email messages themselves or also attachments. The sample will use {@link EmailFragment}s
 * to ultimately write the various pieces that constitute an email message out to
 * the file system.
 *
 * @author Gunnar Hillert
 * @since 2.2
 *
 */
public class EmailFragment {

	private Object data;

	private String filename;

	private File directory;

	/**
	 * Constructor.
	 *
	 * @param directory Must not be null
	 * @param filename  Must not be null
	 * @param data  Must not be null
	 */
	public EmailFragment(File directory, String filename, Object data) {
		super();

		Assert.notNull(directory, "'directory' must not be null");
		Assert.hasText(filename, "'filename' must not be empty");
		Assert.notNull(data, "'data' must not be null");

		this.directory = directory;
		this.filename = filename;
		this.data = data;
	}

	/**
	 * The data to save to the file system, e.g. text messages/attachments, binary
	 * file attachments etc.
	 */
	public Object getData() {
		return data;
	}

	/**
	 * The file name to create for the respective {@link EmailFragment}.
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * The directory where to store the {@link #getData()} using the specified
	 * {@link #getFilename()}.
	 *
	 */
	public File getDirectory() {
		return this.directory;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((directory == null) ? 0 : directory.hashCode());
		result = prime * result
				+ ((filename == null) ? 0 : filename.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		EmailFragment other = (EmailFragment) obj;
		if (directory == null) {
			if (other.directory != null) {
				return false;
			}
		}
		else if (!directory.equals(other.directory)) {
			return false;
		}
		if (filename == null) {
			if (other.filename != null) {
				return false;
			}
		}
		else if (!filename.equals(other.filename)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "EmailFragment [filename=" + filename + ", directory="
				+ directory + "]";
	}

}
