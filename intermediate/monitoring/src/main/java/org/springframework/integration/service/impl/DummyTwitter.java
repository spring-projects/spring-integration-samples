/*
 * Copyright 2002-2016 the original author or authors.
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
package org.springframework.integration.service.impl;

import java.util.Date;

import org.springframework.social.twitter.api.Tweet;

/**
 * @author Gary Russell
 * @since 2.2
 *
 */
public class DummyTwitter {

	private long id;


	@SuppressWarnings("deprecation")
	public Tweet getTweet() {
		Tweet tweet = new Tweet(++this.id, "" + this.id,
				"Spring Integration is the coolest Enterprise Integration project",
				new Date(),
				"SomeUser",
				"https://pbs.twimg.com/profile_images/378800000502646541/992d3596458fca87741b8e93e7df0860_normal.png",
				0L,
				0L,
				null,
				null);
		return tweet;

	}
}
