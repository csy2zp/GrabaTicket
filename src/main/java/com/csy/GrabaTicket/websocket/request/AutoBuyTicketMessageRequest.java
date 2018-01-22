package com.csy.GrabaTicket.websocket.request;

public class AutoBuyTicketMessageRequest extends BaseMessageRequest {
	
	/**
	 * 开车日期
	 * */
	private String trainDate;
	/**
	 * 开车时间段（比如8-12）
	 * */
	private PeriodTime[] periodTime;	
	/**
	 * 购票信息
	 * */
	private String passengerTicketStr;
	/**
	 * 购票人信息
	 * */
	private String oldPassengerStr;
	/**
	 * 出发城市
	 * */
	private String fromCity;
	/**
	 * 目标城市
	 * */
	private String toCity;
	/**
	 * 座位类型
	 * */
	private String[] ticketType;
	
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
	
	public PeriodTime[] getPeriodTime() {
		return periodTime;
	}
	public void setPeriodTime(PeriodTime[] periodTime) {
		this.periodTime = periodTime;
	}

	public String[] getTicketType() {
		return ticketType;
	}
	public void setTicketType(String[] ticketType) {
		this.ticketType = ticketType;
	}

	public static class PeriodTime{
		private int startTime;
		private int endTime;
		
		public int getStartTime() {
			return startTime;
		}
		public void setStartTime(int startTime) {
			this.startTime = startTime;
		}
		public int getEndTime() {
			return endTime;
		}
		public void setEndTime(int endTime) {
			this.endTime = endTime;
		}
	}
}
