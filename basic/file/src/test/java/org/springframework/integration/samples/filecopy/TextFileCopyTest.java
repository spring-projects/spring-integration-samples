/*
 * Copyright 2002-2010 the original author or authors.
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

package org.springframework.integration.samples.filecopy;

import org.junit.jupiter.api.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrating the file copy scenario using text-based source and target.
 * See the 'fileCopyDemo-text.xml' configuration file for details.
 * 
 * @author Mark Fisher
 * @author Marius Bogoevici
 */
public class TextFileCopyTest {

	@Test
	public void testTextBasedCopy() throws Exception{
		ApplicationContext context =
				new ClassPathXmlApplicationContext("/META-INF/spring/integration/fileCopyDemo-text.xml",
						TextFileCopyTest.class);
		FileCopyDemoCommon.displayDirectories(context);
		Thread.sleep(5000);
	}
	
}

