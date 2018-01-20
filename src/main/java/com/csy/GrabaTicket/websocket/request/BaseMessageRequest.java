package com.csy.GrabaTicket.websocket.request;

public class BaseMessageRequest {

	private int mqid;

	private int messageType;

	public int getMqid() {
		return mqid;
	}

	public void setMqid(int mqid) {
		this.mqid = mqid;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
}
