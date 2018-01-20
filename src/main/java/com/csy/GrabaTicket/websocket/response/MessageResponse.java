package com.csy.GrabaTicket.websocket.response;

public class MessageResponse<T> {
	
	private int mqid;
	
	private int messageType;
	
	private T data;

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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	
}
