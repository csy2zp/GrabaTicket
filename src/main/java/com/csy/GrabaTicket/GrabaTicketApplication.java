package com.csy.GrabaTicket;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.csy.GrabaTicket.websocket.WebSocketConfig;
import com.csy.GrabaTicket.websocket.WebSocketConfiguration;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;

@SpringBootApplication
@ComponentScan("com.csy.GrabaTicket.*")
public class GrabaTicketApplication {
	
	@Autowired
	private ServerBootstrap bootStart;
	@Autowired
	private WebSocketConfig config;
	
	private Channel serverChannel;
	
	@PostConstruct
	public void start() throws Exception {
		serverChannel = bootStart.bind(config.getPort()).sync().channel();
	}

	@PreDestroy
	public void stop() throws Exception {
	    serverChannel.closeFuture().sync();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(new Object[] {GrabaTicketApplication.class,
											GrabaTicketConfig.class,
											WebSocketConfiguration.class}, args);
	}
}
