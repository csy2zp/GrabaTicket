package com.csy.GrabaTicket.websocket;

public enum MessageTypeEnum {
	
	BIND_CHANNEL_MESSAGE(1,"绑定通道消息");
	
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
