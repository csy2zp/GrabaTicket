package com.csy.GrabaTicket.model;

public class PreOrderModel {
	
	/**
	 * 火车编码
	 * */
	private String stationTrainCode;
	/**
	 * 出发城市
	 * */
	private String fromCity;
	/**
	 * 目标城市
	 * */
	private String toCity;
	/**
	 * 查询开车日期（格式YYYY-MM-DD）
	 * */
	private String trainDate;
	
	/**
	 * 购票信息
	 * */
	private String passengerTicketStr;
	/**
	 * 购票人信息
	 * */
	private String oldPassengerStr;
	
	public String getStationTrainCode() {
		return stationTrainCode;
	}

	public void setStationTrainCode(String stationTrainCode) {
		this.stationTrainCode = stationTrainCode;
	}

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public String getToCity() {
		return toCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity;
	}

	public String getTrainDate() {
		return trainDate;
	}

	public void setTrainDate(String trainDate) {
		this.trainDate = trainDate;
	}

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
}
