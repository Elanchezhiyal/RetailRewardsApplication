package com.crm.rewardshub.service.impl;

import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.rewardshub.dto.CustomerRewardsDTO;
import com.crm.rewardshub.dto.MonthlyPointsDTO;
import com.crm.rewardshub.model.Transactions;
import com.crm.rewardshub.repository.TransactionRepository;
import com.crm.rewardshub.service.RewardsService;
import com.crm.rewardshub.utility.RewardsCalculatorUtil;

@Service
public class RewardsServiceImpl implements RewardsService{
	
	@Autowired
	private TransactionRepository transactionRepository;
	

    @Override
    public CustomerRewardsDTO getCustomerRewardsLast3Months(Long customerId) {
        OffsetDateTime end = OffsetDateTime.now();
        OffsetDateTime start = end.minusMonths(3);
        return getCustomerRewards(customerId, start, end);
    }

    @Override
    public List<CustomerRewardsDTO> getAllCustomerRewardsLast3Months() {
        OffsetDateTime end = OffsetDateTime.now();
        OffsetDateTime start = end.minusMonths(3);
        return getAllCustomerRewards(start, end);
    }
    @Override
	public CustomerRewardsDTO getCustomerRewards(Long customerId,
                 OffsetDateTime start,
                 OffsetDateTime end) {
		List<Transactions> txs = transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, start, end);
		
		Map<YearMonth, Long> pointsByMonth = txs.stream()
				.collect(Collectors.groupingBy(
						t -> YearMonth.from(t.getTransactionDate().toLocalDate()),
						Collectors.summingLong(t -> RewardsCalculatorUtil.calculatePoints(t.getAmount()))
						));
		
		List<MonthlyPointsDTO> monthly = pointsByMonth.entrySet().stream()
				 .sorted(Map.Entry.comparingByKey())
				 .map(e -> new MonthlyPointsDTO(e.getKey().getYear(), e.getKey().getMonthValue(), e.getValue()))
				 .collect(Collectors.toList());
		
		long total = monthly.stream().mapToLong(MonthlyPointsDTO::getPoints).sum();
		
		return new CustomerRewardsDTO(customerId, monthly, total);
	 }
		
    @Override
	public List<CustomerRewardsDTO> getAllCustomerRewards(OffsetDateTime start,
	                          OffsetDateTime end) {
		 List<Transactions> txs = transactionRepository.findAllBetween(start, end);
		
		 Set<Long> customerIds = txs.stream()
				 .map(Transactions::getCustomerId)
				 .collect(Collectors.toSet());

		 return customerIds.stream()
				 .map(id -> getCustomerRewards(id, start, end))
				 .collect(Collectors.toList());
	}

}
