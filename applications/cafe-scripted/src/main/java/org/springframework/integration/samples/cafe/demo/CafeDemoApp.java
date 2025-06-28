/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 */

package org.springframework.integration.samples.cafe.demo;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

/**
 * An implementation of the Cafe Demo application to demonstrate Spring Integration's
 * scripting capability. This process expects a command line argument corresponding to the scripting language
 * to use.
 *
 * Provides the 'main' method for running the Cafe Demo application. When an
 * order is placed, the Cafe will send that order to the "orders" channel.
 * The relevant components are defined within the configuration file
 * ("cafeDemo.xml").
 *
 * @author Mark Fisher
 * @author Marius Bogoevici
 * @author Oleg Zhurakousky
 * @author David Turanski
 */
public class CafeDemoApp {

	private static final Log LOGGER = LogFactory.getLog(CafeDemoApp.class);

	public static void main(String[] args) {

		List<String> languages = Arrays.asList("groovy", "ruby", "python");
		if (args.length != 1) {
			usage();
		}
		String lang = args[0];

		if (!StringUtils.hasText(lang)){
			usage();
		}

		lang = lang.toLowerCase();
		if (!languages.contains(lang)){
			usage();
		}

		/*
		 * Create an application context and set the active profile to configure the
		 * corresponding scripting implementation
		 */

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();
		ctx.getEnvironment().setActiveProfiles(lang);
		ctx.setConfigLocation("/META-INF/spring/integration/cafeDemo.xml");
		ctx.refresh();
	}


	private static void usage() {
		LOGGER.error("missing or invalid command line argument [groovy,ruby,python]");
		System.exit(1);
	}
}
