package com.csy.GrabaTicket.websocket.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csy.GrabaTicket.service.impl.AutoBuyTicketService;
import com.csy.GrabaTicket.websocket.MessageTypeEnum;
import com.csy.GrabaTicket.websocket.request.BaseMessageRequest;

import io.netty.channel.Channel;

@Service
public class StopMessageHandler implements MessageHandler<BaseMessageRequest> {
	
	@Autowired
	private AutoBuyTicketService autoBuyTicketService;
	
	@Override
	public void handle(Channel channel, BaseMessageRequest baseMessageRequest) {
		autoBuyTicketService.stopThreadByName(channel.id().toString());
	}

	@Override
	public int getMessageType() {
		return MessageTypeEnum.STOP.getIndex();
	}

	@Override
	public Class<BaseMessageRequest> getModelClass() {
		return BaseMessageRequest.class;
	}

}
