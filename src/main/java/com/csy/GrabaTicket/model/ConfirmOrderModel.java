package com.csy.GrabaTicket.model;

public class ConfirmOrderModel {
	
	/**
	 * 购票信息
	 * */
	private String passengerTicketStr;
	/**
	 * 购票人信息
	 * */
	private String oldPassengerStr;
	
	private String trainLocation;
	
	private String repeatSubmitToken;
	
	private String keyCheckIsChange;
	
	private String leftTicketStr;
	
	private String imageLocation;
	
	public String getPassengerTicketStr() {
		return passengerTicketStr;
	}

	public void setPassengerTicketStr(String passengerTicketStr) {
		this.passengerTicketStr = passengerTicketStr;
	}

	public String getOldPassengerStr() {
		return oldPassengerStr;
	}

	public void setOldPassengerStr(String oldPassengerStr) {
		this.oldPassengerStr = oldPassengerStr;
	}

	public String getTrainLocation() {
		return trainLocation;
	}

	public void setTrainLocation(String trainLocation) {
		this.trainLocation = trainLocation;
	}

	public String getRepeatSubmitToken() {
		return repeatSubmitToken;
	}

	public void setRepeatSubmitToken(String repeatSubmitToken) {
		this.repeatSubmitToken = repeatSubmitToken;
	}

	public String getKeyCheckIsChange() {
		return keyCheckIsChange;
	}

	public void setKeyCheckIsChange(String keyCheckIsChange) {
		this.keyCheckIsChange = keyCheckIsChange;
	}

	public String getLeftTicketStr() {
		return leftTicketStr;
	}

	public void setLeftTicketStr(String leftTicketStr) {
		this.leftTicketStr = leftTicketStr;
	}

	public String getImageLocation() {
		return imageLocation;
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}
}
