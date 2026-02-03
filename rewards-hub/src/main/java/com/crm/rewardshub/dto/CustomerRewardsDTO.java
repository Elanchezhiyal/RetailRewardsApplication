package com.crm.rewardshub.dto;

import java.util.List;

public class CustomerRewardsDTO {

	private Long customerId;
    private List<MonthlyPointsDTO> monthlyPoints;
    private Long totalPoints;
    private String customerName;

    public CustomerRewardsDTO(Long customerId,
                              List<MonthlyPointsDTO> monthlyPoints,
                              Long totalPoints) {
        this.customerId = customerId;
        this.monthlyPoints = monthlyPoints;
        this.totalPoints = totalPoints;
    }

	public CustomerRewardsDTO() {}
	
	public CustomerRewardsDTO(Long customerId, String customerName,
	                            List<MonthlyPointsDTO> monthlyPoints, Long totalPoints) {
	    this.customerId = customerId;
	    this.customerName = customerName;
	    this.monthlyPoints = monthlyPoints;
	    this.totalPoints = totalPoints;
	  }

	

	public CustomerRewardsDTO(Long customerId, Long totalPoints) {
		this.customerId = customerId;
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

	public Long getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(Long totalPoints) {
		this.totalPoints = totalPoints;
	}
	
	


}
