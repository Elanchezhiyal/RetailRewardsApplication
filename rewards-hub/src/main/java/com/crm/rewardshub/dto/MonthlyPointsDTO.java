package com.crm.rewardshub.dto;

import java.util.List;

public class MonthlyPointsDTO {

	private String yearMonth;
	private Long points;
	private List<TransactionDTO> transactionList;
	
	
	public MonthlyPointsDTO() {

	}


	public String getYearMonth() {
		return yearMonth;
	}


	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}


	public Long getPoints() {
		return points;
	}


	public void setPoints(Long points) {
		this.points = points;
	}


	public List<TransactionDTO> getTransactionList() {
		return transactionList;
	}


	public void setTransactionList(List<TransactionDTO> transactionList) {
		this.transactionList = transactionList;
	}


	public MonthlyPointsDTO(String yearMonth, Long points, List<TransactionDTO> transactionList) {
		this.yearMonth = yearMonth;
		this.points = points;
		this.transactionList = transactionList;
	}
	
	
	
}
