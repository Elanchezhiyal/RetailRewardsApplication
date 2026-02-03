package com.crm.rewardshub.service.impl;

import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.crm.rewardshub.dto.CustomerRewardsDTO;
import com.crm.rewardshub.dto.MonthlyPointsDTO;
import com.crm.rewardshub.dto.TransactionDTO;
import com.crm.rewardshub.exception.CustomerNotFoundException;
import com.crm.rewardshub.model.Customer;
import com.crm.rewardshub.model.Transactions;
import com.crm.rewardshub.repository.CustomerRepository;
import com.crm.rewardshub.repository.TransactionRepository;
import com.crm.rewardshub.service.RewardsService;
import com.crm.rewardshub.utility.RewardsCalculatorUtil;

@Service
public class RewardsServiceImpl implements RewardsService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    private static final DateTimeFormatter YEAR_MONTH_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM");

    public RewardsServiceImpl(TransactionRepository transactionRepository,
                              CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }

    // ================= SINGLE CUSTOMER (CUSTOM RANGE) =================

    @Override
    public CustomerRewardsDTO getCustomerRewards(long customerId,
                                                 OffsetDateTime startDate,
                                                 OffsetDateTime endDate) {

        validateDates(startDate, endDate);
        Customer customer = validateCustomer(customerId);

        List<Transactions> transactions =
                transactionRepository.findByCustomerIdAndTransactionDateBetween(
                        customerId, startDate, endDate);

        return buildCustomerRewards(customer, transactions);
    }

    // ================= ALL CUSTOMERS (CUSTOM RANGE) =================

    @Override
    public List<CustomerRewardsDTO> getAllCustomerRewards(OffsetDateTime startDate,
                                                          OffsetDateTime endDate) {

        validateDates(startDate, endDate);

        List<Transactions> transactions =
                transactionRepository.findByTransactionDateBetween(startDate, endDate);

        return buildAllCustomersRewards(transactions);
    }

    // ================= SINGLE CUSTOMER (LAST 3 MONTHS) =================

    @Override
    public CustomerRewardsDTO getCustomerRewardsLast3Months(long customerId) {

        OffsetDateTime endDate = OffsetDateTime.now();
        OffsetDateTime startDate = endDate.minusMonths(3);

        Customer customer = validateCustomer(customerId);

        List<Transactions> transactions =
                transactionRepository.findByCustomerIdAndTransactionDateBetween(
                        customerId, startDate, endDate);

        return buildCustomerRewards(customer, transactions);
    }

    // ================= ALL CUSTOMERS (LAST 3 MONTHS) =================

    @Override
    public List<CustomerRewardsDTO> getAllCustomerRewardsLast3Months() {

        OffsetDateTime endDate = OffsetDateTime.now();
        OffsetDateTime startDate = endDate.minusMonths(3);

        List<Transactions> transactions =
                transactionRepository.findByTransactionDateBetween(startDate, endDate);

        return buildAllCustomersRewards(transactions);
    }

    // ================= HELPER METHODS =================

    private Customer validateCustomer(long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("CustomerId not found: " + customerId));
    }

    private void validateDates(OffsetDateTime startDate, OffsetDateTime endDate) {
        Assert.notNull(startDate, "Start date must not be null");
        Assert.notNull(endDate, "End date must not be null");

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }

    // ================= CORE BUILDERS =================

    private CustomerRewardsDTO buildCustomerRewards(Customer customer,
                                                    List<Transactions> transactions) {

        Map<YearMonth, List<Transactions>> groupedByMonth =
                transactions.stream()
                        .collect(Collectors.groupingBy(
                                tx -> YearMonth.from(tx.getTransactionDate())
                        ));

        List<MonthlyPointsDTO> monthlyPoints = new ArrayList<>();
        long totalPoints = 0;

        for (Map.Entry<YearMonth, List<Transactions>> entry : groupedByMonth.entrySet()) {

            List<TransactionDTO> transactionDTOs =
                    entry.getValue().stream()
                            .map(tx -> {
                                long points =
                                        RewardsCalculatorUtil.calculatePoints(tx.getAmount());
                                return new TransactionDTO(
                                        tx.getId(),
                                        tx.getTransactionDate().format(YEAR_MONTH_FORMATTER),
                                        tx.getAmount(),
                                        points
                                );
                            })
                            .toList();

            long monthlyTotal = transactionDTOs.stream()
                    .mapToLong(TransactionDTO::getPointsEarned)
                    .sum();

            totalPoints += monthlyTotal;

            monthlyPoints.add(new MonthlyPointsDTO(
                    entry.getKey().toString(),
                    monthlyTotal,
                    transactionDTOs
            ));
        }

        monthlyPoints.sort(Comparator.comparing(MonthlyPointsDTO::getYearMonth));

        return new CustomerRewardsDTO(
                customer.getId(),
                customer.getName(),
                monthlyPoints,
                totalPoints
        );
    }

    private List<CustomerRewardsDTO> buildAllCustomersRewards(
            List<Transactions> transactions) {

        Map<Long, List<Transactions>> groupedByCustomer =
                transactions.stream()
                        .collect(Collectors.groupingBy(Transactions::getCustomerId));

        Map<Long, Customer> customerMap =
                customerRepository.findAllById(groupedByCustomer.keySet())
                        .stream()
                        .collect(Collectors.toMap(Customer::getId, c -> c));

        return groupedByCustomer.entrySet().stream()
                .map(entry -> {
                    Customer customer = customerMap.get(entry.getKey());
                    if (customer == null) {
                        return null;
                    }
                    return buildCustomerRewards(customer, entry.getValue());
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
