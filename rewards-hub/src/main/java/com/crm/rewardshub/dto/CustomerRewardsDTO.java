package com.crm.rewardshub.dto;

import java.util.List;

public class CustomerRewardsDTO {

	private Long customerId;
	private String customerName;
	private List<MonthlyPointsDTO> monthlyPoints;
	private long totalPoints;
	

	public CustomerRewardsDTO() {}
	
	public CustomerRewardsDTO(Long customerId, String customerName,
	                            List<MonthlyPointsDTO> monthlyPoints, long totalPoints) {
	    this.customerId = customerId;
	    this.customerName = customerName;
	    this.monthlyPoints = monthlyPoints;
	    this.totalPoints = totalPoints;
	  }

	

	public CustomerRewardsDTO(Long customerId, List<MonthlyPointsDTO> monthlyPoints, long totalPoints) {
		super();
		this.customerId = customerId;
		this.monthlyPoints = monthlyPoints;
		this.totalPoints = totalPoints;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public List<MonthlyPointsDTO> getMonthlyPoints() {
		return monthlyPoints;
	}

	public void setMonthlyPoints(List<MonthlyPointsDTO> monthlyPoints) {
		this.monthlyPoints = monthlyPoints;
	}

	public long getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(long totalPoints) {
		this.totalPoints = totalPoints;
	}
	
	


}
