package com.csy.GrabaTicket.websocket.request;

public class BindChannelMessageRequest extends BaseMessageRequest{
	
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
