package org.specs2.spring.webexample.web;

import org.specs2.spring.webexample.domain.Message;
import org.specs2.spring.webexample.services.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author janm
 */
@Controller
public class IndexController {
	private ManagementService managementService;

	@Autowired
	public IndexController(ManagementService managementService) {
		this.managementService = managementService;
	}

	@RequestMapping(value = "/home/{name}", method = RequestMethod.GET)
	public String home(@PathVariable String name, Model model) {
		model.addAttribute("message", name);
		Message message = new Message();
		message.setSourceText(name);
		message.setProcessedText(name.toUpperCase());
		this.managementService.save(message);
		return "home";
	}

}
