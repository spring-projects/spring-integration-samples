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

package org.springframework.integration.samples.filecopy;

import java.io.File;

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;

/**
 * Displays the names of the input and output directories.
 *
 * @author Marius Bogoevici
 * @author Mark Fisher
 * @author Gary Russell
 */
public class FileCopyDemoCommon {

	public static void displayDirectories(ApplicationContext context) {
		File inDir = (File) new DirectFieldAccessor(context.getBean(FileReadingMessageSource.class)).getPropertyValue("directory");
		LiteralExpression expression = (LiteralExpression) new DirectFieldAccessor(context.getBean(FileWritingMessageHandler.class)).getPropertyValue("destinationDirectoryExpression");
		File outDir = new File(expression.getValue());
		System.out.println("Input directory is: " + inDir.getAbsolutePath());
		System.out.println("Output directory is: " + outDir.getAbsolutePath());
		System.out.println("===================================================");
	}

}
