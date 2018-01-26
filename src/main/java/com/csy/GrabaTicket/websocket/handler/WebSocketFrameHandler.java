
package com.csy.GrabaTicket.websocket.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.csy.GrabaTicket.service.impl.AutoBuyTicketService;
import com.csy.GrabaTicket.websocket.ChannelContainer;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

@Component
@SuppressWarnings({"rawtypes","unchecked"})
@Sharable
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
	
	private final static Log log = LogFactory.getLog(WebSocketFrameHandler.class);
	private Map<Integer,MessageHandler> handlers = new HashMap<Integer,MessageHandler>();
	
	@Autowired
	private ChannelContainer channels;
	@Autowired
	private AutoBuyTicketService autoBuyTicketService;
	
	@Autowired
	public void setHandler(List<MessageHandler> messageHandlers) {
		for(MessageHandler m : messageHandlers) {
			handlers.put(m.getMessageType(), m);
		}
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		if (frame instanceof TextWebSocketFrame) {
			String msg = ((TextWebSocketFrame) frame).text();
			
			log.info("-------receive readed message----address:" + ctx.channel().remoteAddress()+"----"+msg);
			
			Map<String,Object> data = mapper.readValue(msg,HashMap.class);
			MessageHandler handler = handlers.get(data.get("messageType"));
			if(handler != null) {
				handler.handle(ctx.channel(), mapper.readValue(msg,handler.getModelClass()));
			}
		} else {
			String message = "unsupported frame type: " + frame.getClass().getName();
			throw new UnsupportedOperationException(message);
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		channels.bindChannel((NioSocketChannel)ctx.channel());
		log.info("user online --------------address:"+ctx.channel().remoteAddress());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		channels.remove((NioSocketChannel)ctx.channel());
		log.info("----------current connect size " + channels.connectSize());
	}
}
