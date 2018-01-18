package com.csy.GrabaTicket.service;

import java.util.List;
import java.util.Map;

import com.csy.GrabaTicket.model.ConfirmOrderModel;
import com.csy.GrabaTicket.model.PreOrderModel;

public interface IBuyTicketService {
	
	/**
	 * 预下单
	 * */
	Map<String,String> preOrder(PreOrderModel preOrderModel);
	
	/**
	 * 确认下单
	 * */
	Map<String,String> confirmOrder(ConfirmOrderModel confirmOrderModel);
	
	/**
	 * 校验图片
	 * */
	boolean checkImage(String imgLocation,String repeatSubmitToken);

	List<String> listUser();
	
	String getImage();
}
