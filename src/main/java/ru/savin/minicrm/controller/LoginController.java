package ru.savin.minicrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/showMyLoginPage")
	public String showMyLoginPage() {
		
		return "user/login";
		
	}
	
	@GetMapping("/access-denied")
	public String showAccessDenied() {
		
		return "/user/access-denied";
		
	}
	
}