package com.crm.rewardshub.controller;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crm.rewardshub.dto.CustomerRewardsDTO;
import com.crm.rewardshub.service.RewardsService;

@RestController
@RequestMapping("/api/rewards")
public class CustomerRewardsController {
	
	@Autowired
	private RewardsService rewardsService;

	@GetMapping("/customers/{customerId}")
	public CustomerRewardsDTO getCustomerRewards(  
		@PathVariable Long customerId,
	    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) OffsetDateTime startDate,
	    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) OffsetDateTime endDate) {
	    return rewardsService.getCustomerRewards(customerId, startDate, endDate);
	  }

	  @GetMapping("/customers")
	  public List<CustomerRewardsDTO> getAllCustomerRewards(
	      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) OffsetDateTime startDate,
	      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) OffsetDateTime endDate) {
	    return rewardsService.getAllCustomerRewards(startDate, endDate);
	  }

	  @GetMapping("/customers/{id}/last3months")
	    public CustomerRewardsDTO getCustomerRewardsLast3Months(@PathVariable Long id) {
	        return rewardsService.getCustomerRewardsLast3Months(id);
	    }

	    @GetMapping("/customers/last3months")
	    public List<CustomerRewardsDTO> getAllCustomerRewardsLast3Months() {
	        return rewardsService.getAllCustomerRewardsLast3Months();
	    }

}
