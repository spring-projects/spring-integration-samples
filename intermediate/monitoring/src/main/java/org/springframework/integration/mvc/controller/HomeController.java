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
package org.springframework.integration.mvc.controller;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.model.TwitterMessage;
import org.springframework.integration.service.TwitterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Log logger = LogFactory.getLog(HomeController.class);

	@Autowired
	private TwitterService twitterService;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value="/")
	public String home(Model model, @RequestParam(required=false) String startTwitter,
									@RequestParam(required=false) String stopTwitter,
									@RequestParam(required=false) String shutdown) {

		if (startTwitter != null) {
			twitterService.startTwitterAdapter();
			return "redirect:/";
		}

		if (stopTwitter != null) {
			twitterService.stopTwitterAdapter();
			return "redirect:/";
		}

		if (shutdown != null) {
			twitterService.shutdown();
			return "redirect:/";
		}

		final Collection<TwitterMessage> twitterMessages = twitterService.getTwitterMessages();

		logger.info("Retrieved " + twitterMessages.size() + " Twitter messages.");

		model.addAttribute("twitterMessages", twitterMessages);

		return "home";
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value="/ajax")
	public String ajaxCall(Model model) {

		final Collection<TwitterMessage> twitterMessages = twitterService.getTwitterMessages();

		logger.info("Retrieved " + twitterMessages.size() + " Twitter messages.");
		model.addAttribute("twitterMessages", twitterMessages);

		return "twitterMessages";

	}
}

