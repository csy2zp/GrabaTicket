package com.csy.GrabaTicket.websocket;

public enum MessageTypeEnum {
	
	AUTI_BUY_TICKET(1,"自动买票消息"),
	STOP(2,"停止自动买票"),
	PING(3,"ping");
	
	private int index;
	
	private String name;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	MessageTypeEnum(int index,String name) {
		this.index = index;
		this.name = name;
	}
}
