/*
 * Copyright 2015-present the original author or authors.
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

package org.springframework.integration.samples.zip;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.Expression;
import org.springframework.integration.file.inbound.FileReadingMessageSource;
import org.springframework.integration.file.outbound.FileWritingMessageHandler;

/**
 * Displays the names of the input and output directories.
 *
 * @author Gunnar Hillert
 *
 * @since 6.4
 *
 */
public final class SpringIntegrationUtils {

	private static final Log LOGGER = LogFactory.getLog(SpringIntegrationUtils.class);

	private SpringIntegrationUtils() { }

	/**
	 * Helper Method to dynamically determine and display input and output
	 * directories as defined in the Spring Integration context.
	 *
	 * @param context Spring Application Context
	 */
	public static void displayDirectories(final ApplicationContext context) {

		Map<String, FileReadingMessageSource> fileReadingMessageSources = context.getBeansOfType(FileReadingMessageSource.class);

		List<String> inputDirectories = new ArrayList<>();

		for (FileReadingMessageSource source : fileReadingMessageSources.values()) {
			final File inDir = (File) new DirectFieldAccessor(source).getPropertyValue("directoryExpression.value");
			inputDirectories.add(inDir.getAbsolutePath());
		}


		Map<String, FileWritingMessageHandler> fileWritingMessageHandlers = context.getBeansOfType(FileWritingMessageHandler.class);

		List<String> outputDirectories = new ArrayList<>();

		for (final FileWritingMessageHandler messageHandler : fileWritingMessageHandlers.values()) {
			final Expression outDir = (Expression) new DirectFieldAccessor(messageHandler).getPropertyValue("destinationDirectoryExpression");
			outputDirectories.add(outDir.getExpressionString());
		}

		final StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("\n=========================================================");
		stringBuilder.append("\n");

		for (final String inputDirectory : inputDirectories) {
			stringBuilder.append("\n    Input directory is: '" + inputDirectory + "'");
		}

		for (final String outputDirectory : outputDirectories) {
			stringBuilder.append("\n    Output directory is: '" + outputDirectory + "'");
		}

		stringBuilder.append("\n\n=========================================================");

		LOGGER.info(stringBuilder.toString());

	}

}
