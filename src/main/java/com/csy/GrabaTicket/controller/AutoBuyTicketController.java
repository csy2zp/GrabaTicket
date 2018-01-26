package com.csy.GrabaTicket.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csy.GrabaTicket.model.AutoBuyTicketModel;
import com.csy.GrabaTicket.service.IBuyTicketService;
import com.csy.GrabaTicket.service.impl.AutoBuyTicketService;

@Controller
public class AutoBuyTicketController {
	
	@Autowired
	private IBuyTicketService buyTicket;
	@Autowired
	private AutoBuyTicketService autoBuyTicket;
	
	@GetMapping("/auto_buy_ticket")
	public String autoBuyTicket(Model model) {
		model.addAttribute("users", buyTicket.listUser());
		return "autoBuyTicket";
	}
	
	@PostMapping("/auto_buy_ticket")
	@ResponseBody
	public Map<String,String> autoBuyTicket(@RequestBody AutoBuyTicketModel model) {
		return autoBuyTicket.start(model);
	}
}
