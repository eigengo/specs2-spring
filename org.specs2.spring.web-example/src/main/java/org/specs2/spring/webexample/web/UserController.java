package org.specs2.spring.webexample.web;

import org.specs2.spring.webexample.domain.User;
import org.specs2.spring.webexample.services.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * @author janm
 */
@Controller
public class UserController {
	private ManagementService managementService;

	@Autowired
	public UserController(ManagementService managementService) {
		this.managementService = managementService;
	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public String view(@PathVariable long id, Model model) {
		model.addAttribute("user", this.managementService.get(User.class, id));
		return "user/edit";
	}

	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute User user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "user/edit";
		}
		this.managementService.save(user);
		return "redirect:/users.html";
	}

}
