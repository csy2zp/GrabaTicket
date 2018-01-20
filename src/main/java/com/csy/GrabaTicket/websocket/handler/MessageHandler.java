package com.csy.GrabaTicket.websocket.handler;

import io.netty.channel.Channel;

public interface MessageHandler<T> {
		
	void handle(Channel channel,T t);
	
	int getMessageType();
	
	Class<T> getModelClass();
}
