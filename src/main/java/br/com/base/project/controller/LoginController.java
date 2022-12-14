package br.com.base.project.controller;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

	@RequestMapping("/login")
	public String login(@AuthenticationPrincipal User user) {
		if (user != null) {
			return "redirect:/";
		}
		return "login.html";
	}
	
	@RequestMapping("/")
	public String index() {
		return "redirect:/login";
	}
	
	@RequestMapping("/logout")
	public String logout() {
		return "login.html";
	}
}
