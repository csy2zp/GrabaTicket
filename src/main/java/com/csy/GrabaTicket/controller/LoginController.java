package com.csy.GrabaTicket.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.csy.GrabaTicket.service.ILoginService;

@Controller
public class LoginController {
	
	@Autowired
	private ILoginService loginService;
	
	@GetMapping("/login")
	public String getLogin(Model model) {
		String imgPath = loginService.init();
		model.addAttribute("image", imgPath);
		return "login";
	}
	
	@PostMapping("/login")
	public String login(HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String code = request.getParameter("code");
		
		if(loginService.checkImg(code)) {
			if(loginService.login(username, password)) {
				return "redirect:index";
			}
		}
		return "redirect:login";
	}
	
	@GetMapping("/loginOut")
	public String getLoginOut() {
		loginService.loginOut();
		return "redirect:login";
	}
}
