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
package org.springframework.integration.samples.loanbroker.loanshark.web;

import javax.validation.Valid;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.samples.loanbroker.loanshark.domain.LoanShark;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/loansharks")
@Controller
public class SharkController {

	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid LoanShark loanShark, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("loanShark", loanShark);
			return "loansharks/create";
		}
		loanShark.persist();
		return "redirect:/loansharks/" + loanShark.getId();
	}

	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("loanShark", new LoanShark());
		return "loansharks/create";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, Model model) {
		model.addAttribute("loanshark", LoanShark.findLoanShark(id));
		model.addAttribute("itemId", id);
		return "loansharks/show";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			model.addAttribute("loansharks", LoanShark.findLoanSharkEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
			float nrOfPages = (float) LoanShark.countLoanSharks() / sizeNo;
			model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		} else {
			model.addAttribute("loansharks", LoanShark.findAllLoanSharks());
		}
		return "loansharks/list";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@Valid LoanShark loanShark, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("loanShark", loanShark);
			return "loansharks/update";
		}
		loanShark.merge();
		return "redirect:/loansharks/" + loanShark.getId();
	}

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("loanShark", LoanShark.findLoanShark(id));
		return "loansharks/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
		LoanShark.findLoanShark(id).remove();
		model.addAttribute("page", (page == null) ? "1" : page.toString());
		model.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/loansharks?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
	}

	@RequestMapping(params = { "find=ByName", "form" }, method = RequestMethod.GET)
	public String findLoanSharksByNameForm(Model model) {
		return "loansharks/findLoanSharksByName";
	}

	@RequestMapping(params = "find=ByName", method = RequestMethod.GET)
	public String findLoanSharksByName(@RequestParam("name") String name, Model model) {
		model.addAttribute("loansharks", LoanShark.findLoanSharksByName(name).getResultList());
		return "loansharks/list";
	}

	Converter<LoanShark, String> getLoanSharkConverter() {
		return new Converter<LoanShark, String>() {
			public String convert(LoanShark loanShark) {
				return new StringBuilder().append(loanShark.getName()).append(" ").append(loanShark.getCounter()).append(" ").append(loanShark.getAverageRate()).toString();
			}
		};
	}

	@InitBinder
	void registerConverters(WebDataBinder binder) {
		if (binder.getConversionService() instanceof GenericConversionService) {
			GenericConversionService conversionService = (GenericConversionService) binder.getConversionService();
			conversionService.addConverter(getLoanSharkConverter());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public String showJson(@PathVariable("id") Long id) {
		return LoanShark.findLoanShark(id).toJson();
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		LoanShark.fromJsonToLoanShark(json).persist();
		return new ResponseEntity<String>("LoanShark created", HttpStatus.CREATED);
	}

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public String listJson() {
		return LoanShark.toJsonArray(LoanShark.findAllLoanSharks());
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
		for (LoanShark loanshark: LoanShark.fromJsonArrayToLoanSharks(json)) {
			loanshark.persist();
		}
		return new ResponseEntity<String>("LoanShark created", HttpStatus.CREATED);
	}
}
