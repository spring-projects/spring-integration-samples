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

package org.springframework.integration.samples.cafe.xml;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 *
 * @author Gunnar Hillert
 *
 */
public final class CafeDemoAppUtilities {

	/** spring profile for running locally */
	static final String DEV = "dev";

	/** spring profile for running in cloud foundry */
	static final String CLOUD = "cloud";

	private CafeDemoAppUtilities() {
	}

	/**
	 *
	 * @param path path to the file
	 * @param targetClass the class who's classloader we will use to load the context file
	 * @param profile a profile name
	 * @return the spring context
	 */
	public static AbstractApplicationContext loadProfileContext(String path, Class<?> targetClass, String profile) {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.getEnvironment().setActiveProfiles(profile);
		ctx.setClassLoader(targetClass.getClassLoader());
		ctx.load(path);
		ctx.refresh();
		return ctx;
	}

}
