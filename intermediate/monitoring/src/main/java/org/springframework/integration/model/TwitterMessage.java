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
package org.springframework.integration.model;

import java.util.Date;

/**
 * Represents some common Twitter related fields.
 */
public class TwitterMessage {

	private Date createdAt;
	private String text;
	private String fromUser;
	private String profileImageUrl;

	/** Default constructor. */
	public TwitterMessage() {
		super();
	}

	/** Constructor to initialize all fields available. */
	public TwitterMessage(Date createdAt, String text, String fromUser,
			String profileImageUrl) {
		super();
		this.createdAt = createdAt;
		this.text = text;
		this.fromUser = fromUser;
		this.profileImageUrl = profileImageUrl;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result
				+ ((fromUser == null) ? 0 : fromUser.hashCode());
		result = prime * result
				+ ((profileImageUrl == null) ? 0 : profileImageUrl.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TwitterMessage other = (TwitterMessage) obj;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (fromUser == null) {
			if (other.fromUser != null)
				return false;
		} else if (!fromUser.equals(other.fromUser))
			return false;
		if (profileImageUrl == null) {
			if (other.profileImageUrl != null)
				return false;
		} else if (!profileImageUrl.equals(other.profileImageUrl))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tweet [createdAt=" + createdAt + ", text=" + text
				+ ", fromUser=" + fromUser + ", profileImageUrl="
				+ profileImageUrl + "]";
	}

}
