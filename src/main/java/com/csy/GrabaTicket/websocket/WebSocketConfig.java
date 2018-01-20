package com.csy.GrabaTicket.websocket;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(ignoreUnknownFields = false,prefix="websocket")
public class WebSocketConfig {
	private String path;
	
	private int port;
	
	private boolean ssl;
	
	private int bossCount;
	
	private int workerCount;
	
	public int getBossCount() {
		return bossCount;
	}

	public void setBossCount(int bossCount) {
		this.bossCount = bossCount;
	}

	public int getWorkerCount() {
		return workerCount;
	}

	public void setWorkerCount(int workerCount) {
		this.workerCount = workerCount;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isSsl() {
		return ssl;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}
	
}
