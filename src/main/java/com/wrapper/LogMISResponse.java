package com.wrapper;

public class LogMISResponse {

	private String ip;
	private String hit;
	private String day;
	/**
	 * @param ip
	 * @param hit
	 * @param day
	 */
	public LogMISResponse(String ip, String hit, String day) {
		this.ip = ip;
		this.hit = hit;
		this.day = day;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the hit
	 */
	public String getHit() {
		return hit;
	}
	/**
	 * @param hit the hit to set
	 */
	public void setHit(String hit) {
		this.hit = hit;
	}
	/**
	 * @return the day
	 */
	public String getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(String day) {
		this.day = day;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EPPMISResponse [ip=" + ip + ", hit=" + hit + ", day=" + day + "]";
	}

}