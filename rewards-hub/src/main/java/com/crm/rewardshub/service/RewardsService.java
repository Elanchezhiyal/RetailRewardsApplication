package com.crm.rewardshub.service;

import java.time.OffsetDateTime;
import java.util.List;

import com.crm.rewardshub.dto.CustomerRewardsDTO;

public interface RewardsService {
	public CustomerRewardsDTO getCustomerRewards(Long customerId, OffsetDateTime start, OffsetDateTime end) ;
	public List<CustomerRewardsDTO> getAllCustomerRewards(OffsetDateTime start, OffsetDateTime end) ;
	CustomerRewardsDTO getCustomerRewardsLast3Months(Long customerId);
	List<CustomerRewardsDTO> getAllCustomerRewardsLast3Months();

}
