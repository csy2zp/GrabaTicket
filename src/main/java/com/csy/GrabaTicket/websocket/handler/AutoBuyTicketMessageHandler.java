package com.csy.GrabaTicket.websocket.handler;

import org.springframework.beans.factory.annotation.Autowired;

import com.csy.GrabaTicket.service.impl.AutoBuyTicketService;
import com.csy.GrabaTicket.websocket.MessageTypeEnum;
import com.csy.GrabaTicket.websocket.request.AutoBuyTicketMessageRequest;

import io.netty.channel.Channel;

public class AutoBuyTicketMessageHandler implements MessageHandler<AutoBuyTicketMessageRequest> {
	
	@Autowired
	private AutoBuyTicketService autoBuyTicketService;
	
	@Override
	public void handle(Channel channel, AutoBuyTicketMessageRequest message) {
		autoBuyTicketService.start(channel,message);
	}

	@Override
	public int getMessageType() {
		return MessageTypeEnum.AUTI_BUY_TICKET.getIndex();
	}

	@Override
	public Class<AutoBuyTicketMessageRequest> getModelClass() {
		return AutoBuyTicketMessageRequest.class;
	}

}
