package com.csy.GrabaTicket.websocket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

@Configuration
public class SSLContextConfiguration {
	
	private final static Log log = LogFactory.getLog(SSLContextConfiguration.class);
	
	@Autowired
	private WebSocketConfig config;
	
	@Bean
	public SslContext getSslContext() {
		SelfSignedCertificate ssc = null;
		SslContext sslCtx = null;
		if(config.isSsl()) {
			try {
				ssc = new SelfSignedCertificate();
				sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}
		}
        return sslCtx;
	}
	
}
