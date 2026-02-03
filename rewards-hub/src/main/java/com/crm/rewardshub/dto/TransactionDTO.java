package com.crm.rewardshub.dto;

import java.math.BigDecimal;

public class TransactionDTO {
	
	private Long id;
	private String date;
	private BigDecimal amount;
	private Long pointsEarned;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Long getPointsEarned() {
		return pointsEarned;
	}
	public void setPointsEarned(Long pointsEarned) {
		this.pointsEarned = pointsEarned;
	}
	public TransactionDTO(Long id, String date, BigDecimal amount, Long pointsEarned) {
		this.id = id;
		this.date = date;
		this.amount = amount;
		this.pointsEarned = pointsEarned;
	}
	
	

}
