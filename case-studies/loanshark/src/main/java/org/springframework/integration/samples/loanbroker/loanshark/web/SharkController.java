package org.springframework.integration.samples.loanbroker.loanshark.web;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.integration.samples.loanbroker.loanshark.domain.LoanShark;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@RooWebScaffold(path = "loansharks", formBackingObject = LoanShark.class)
@RequestMapping("/loansharks")
@Controller
public class SharkController {
}
