package com.csy.GrabaTicket.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csy.GrabaTicket.model.ConfirmOrderModel;
import com.csy.GrabaTicket.model.PreOrderModel;
import com.csy.GrabaTicket.service.IBuyTicketService;

@SuppressWarnings("rawtypes")
@Controller
public class BuyTicketController {
	
	@Autowired
	private IBuyTicketService buyTicket;
	
	@GetMapping("/index")
	public String getindex(Model model) {
		model.addAttribute("users", buyTicket.listUser());
		return "index";
	}
	
	@PostMapping("/preOrder")
	@ResponseBody
	public Map PreOrder(@ModelAttribute PreOrderModel preOrderModel) {
		return buyTicket.preOrder(preOrderModel);
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/confirmOrder")
	@ResponseBody
	public Map confirmOrder(@ModelAttribute ConfirmOrderModel confirmOrderModel) {
		Map<String,String> data = new HashMap();
		if(buyTicket.checkImage(confirmOrderModel.getImageLocation(), confirmOrderModel.getRepeatSubmitToken())) {
			return buyTicket.confirmOrder(confirmOrderModel);
		} else{			
			data.put("messageStatus", "fail");
			data.put("message", "图片验证不通过");
			data.put("image", buyTicket.getImage());
		};
		return data;
	}
	
	@GetMapping("/auto_buy_ticket")
	public String autoBuyTicket(Model model) {
		model.addAttribute("users", buyTicket.listUser());
		return "autoBuyTicket";
	}
}
