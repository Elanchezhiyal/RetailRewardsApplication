package com.crm.rewardshub.service;

import java.time.OffsetDateTime;
import java.util.List;

import com.crm.rewardshub.dto.CustomerRewardsDTO;

public interface RewardsService {

	CustomerRewardsDTO getCustomerRewards(long customerId, OffsetDateTime start, OffsetDateTime end);

	List<CustomerRewardsDTO> getAllCustomerRewards(OffsetDateTime start, OffsetDateTime end);

	CustomerRewardsDTO getCustomerRewardsLast3Months(long customerId);

	List<CustomerRewardsDTO> getAllCustomerRewardsLast3Months();

}
