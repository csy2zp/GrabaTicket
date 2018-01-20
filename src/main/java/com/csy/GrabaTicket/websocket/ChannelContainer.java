package com.csy.GrabaTicket.websocket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.netty.channel.socket.nio.NioSocketChannel;

@Component
public class ChannelContainer {
	
	private static Map<String,NioSocketChannel> channels = new HashMap<String,NioSocketChannel>();
	
	public void remove(NioSocketChannel n) {
		channels.remove(n.id().toString());
	}
	
	public void bindChannel(NioSocketChannel n) {
		channels.put(n.id().toString(), n);
	}
	
	public long connectSize() {
		return channels.size();
	}
}
