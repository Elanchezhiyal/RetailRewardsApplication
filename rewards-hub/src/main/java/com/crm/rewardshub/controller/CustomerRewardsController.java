package com.crm.rewardshub.controller;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crm.rewardshub.api.ResponseJson;
import com.crm.rewardshub.dto.CustomerRewardsDTO;
import com.crm.rewardshub.service.impl.RewardsServiceImpl;

@RestController
@RequestMapping("/api/rewards")
public class CustomerRewardsController {

	private final RewardsServiceImpl rewardsService;

	public CustomerRewardsController(RewardsServiceImpl rewardsService) {
		this.rewardsService = rewardsService;
	}

	@GetMapping("/customers/{customerId}")
	public ResponseEntity<ResponseJson<CustomerRewardsDTO>> getCustomerRewards(@PathVariable long customerId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate) {

		CustomerRewardsDTO response = rewardsService.getCustomerRewards(customerId, startDate, endDate);

		return ResponseEntity.ok(ResponseJson.success(response));
	}

	/**
	 * Custom date range – all customers Example:
	 * /api/rewards/customers?startDate=2024-01-01T00:00:00Z&endDate=2024-03-31T23:59:59Z
	 */
	@GetMapping("/customers")
	public ResponseEntity<ResponseJson<List<CustomerRewardsDTO>>> getAllCustomerRewards(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate) {

		List<CustomerRewardsDTO> response = rewardsService.getAllCustomerRewards(startDate, endDate);

		return ResponseEntity.ok(ResponseJson.success(response));
	}

	/**
	 * Last 3 months – single customer Example: /api/rewards/customers/1/last3months
	 */
	@GetMapping("/customers/{customerId}/last3months")
	public ResponseEntity<ResponseJson<CustomerRewardsDTO>> getCustomerRewardsLast3Months(
			@PathVariable long customerId) {

		CustomerRewardsDTO response = rewardsService.getCustomerRewardsLast3Months(customerId);

		return ResponseEntity.ok(ResponseJson.success(response));
	}

	/**
	 * Last 3 months – all customers Example: /api/rewards/customers/last3months
	 */
	@GetMapping("/customers/last3months")
	public ResponseEntity<ResponseJson<List<CustomerRewardsDTO>>> getAllCustomerRewardsLast3Months() {

		List<CustomerRewardsDTO> response = rewardsService.getAllCustomerRewardsLast3Months();

		return ResponseEntity.ok(ResponseJson.success(response));
	}
}
