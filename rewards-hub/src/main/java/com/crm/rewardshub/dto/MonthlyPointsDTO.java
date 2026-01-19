package com.crm.rewardshub.dto;

public class MonthlyPointsDTO {

	private int year;
	private int month;
	private long points;
	
	
	public MonthlyPointsDTO() {

	}
	public MonthlyPointsDTO(int year, int month, long points) {

		this.year = year;
		this.month = month;
		this.points = points;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public long getPoints() {
		return points;
	}
	public void setPoints(long points) {
		this.points = points;
	}
	
	
}
