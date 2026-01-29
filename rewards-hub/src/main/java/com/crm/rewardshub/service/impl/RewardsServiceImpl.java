package com.crm.rewardshub.service.impl;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.crm.rewardshub.dto.CustomerRewardsDTO;
import com.crm.rewardshub.dto.MonthlyPointsDTO;
import com.crm.rewardshub.exception.CustomerNotFoundException;
import com.crm.rewardshub.exception.InvalidRequestException;
import com.crm.rewardshub.model.Transactions;
import com.crm.rewardshub.repository.CustomerRepository;
import com.crm.rewardshub.repository.TransactionRepository;
import com.crm.rewardshub.service.RewardsService;
import com.crm.rewardshub.utility.RewardsCalculatorUtil;

@Service
public class RewardsServiceImpl implements RewardsService{
    private static final ZoneId APP_ZONE = ZoneId.of("UTC");

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    public RewardsServiceImpl(TransactionRepository transactionRepository,
                              CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }

    // -------------------- CUSTOM RANGE – ONE CUSTOMER --------------------
    @Override
    public CustomerRewardsDTO getCustomerRewards(
            long customerId,
            OffsetDateTime startDate,
            OffsetDateTime endDate) {

        validateCustomer(customerId);
        validateDates(startDate, endDate);

        List<Transactions> transactions =
                transactionRepository.findByCustomerIdAndTransactionDateBetween(
                        customerId, startDate, endDate);

        return buildCustomerRewards(customerId, transactions);
    }

    // -------------------- CUSTOM RANGE – ALL CUSTOMERS --------------------
    @Override
    public List<CustomerRewardsDTO> getAllCustomerRewards(
            OffsetDateTime startDate,
            OffsetDateTime endDate) {

        validateDates(startDate, endDate);

        return transactionRepository
                .findByTransactionDateBetween(startDate, endDate)
                .stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Transactions::getCustomerId))
                .entrySet()
                .stream()
                .map(e -> buildCustomerRewards(e.getKey(), e.getValue()))
                .toList();
    }

    // -------------------- LAST 3 MONTHS – ONE CUSTOMER --------------------
    @Override
    public CustomerRewardsDTO getCustomerRewardsLast3Months(long customerId) {

        validateCustomer(customerId);

        OffsetDateTime endDate = OffsetDateTime.now(APP_ZONE);
        OffsetDateTime startDate = endDate.minusMonths(3);

        return getCustomerRewards(customerId, startDate, endDate);
    }

    // -------------------- LAST 3 MONTHS – ALL CUSTOMERS --------------------
    @Override
    public List<CustomerRewardsDTO> getAllCustomerRewardsLast3Months() {

        OffsetDateTime endDate = OffsetDateTime.now(APP_ZONE);
        OffsetDateTime startDate = endDate.minusMonths(3);

        return getAllCustomerRewards(startDate, endDate);
    }

    // -------------------- BUILD RESPONSE --------------------
    private CustomerRewardsDTO buildCustomerRewards(
            long customerId,
            List<Transactions> transactions) {

        Map<String, Long> monthlyPointsMap =
                transactions.stream()
                        .map(tx -> Map.entry(
                                tx.getTransactionDate().getYear() + "-" +
                                tx.getTransactionDate().getMonthValue(),
                                RewardsCalculatorUtil.calculatePoints(tx.getAmount())))
                        .collect(java.util.stream.Collectors.groupingBy(
                                Map.Entry::getKey,
                                java.util.stream.Collectors.summingLong(Map.Entry::getValue)));

        List<MonthlyPointsDTO> monthlyPoints =
                monthlyPointsMap.entrySet().stream()
                        .map(e -> {
                            String[] parts = e.getKey().split("-");
                            return new MonthlyPointsDTO(
                                    Integer.parseInt(parts[0]),
                                    Integer.parseInt(parts[1]),
                                    e.getValue());
                        })
                        .sorted((a, b) ->
                                a.getYear() != b.getYear()
                                        ? a.getYear() - b.getYear()
                                        : a.getMonth() - b.getMonth())
                        .toList();

        long totalPoints =
                monthlyPoints.stream()
                        .mapToLong(MonthlyPointsDTO::getPoints)
                        .sum();

        return new CustomerRewardsDTO(customerId, monthlyPoints, totalPoints);
    }

    // -------------------- VALIDATIONS --------------------
    private void validateCustomer(long customerId) {
        if (customerId <= 0) {
            throw new InvalidRequestException("Invalid customerId");
        }
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(
                    "Customer not found for id: " + customerId);
        }
    }

    private void validateDates(OffsetDateTime startDate, OffsetDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new InvalidRequestException("startDate and endDate are required");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidRequestException("Invalid date range");
        }
    }
}
