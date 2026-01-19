package com.crm.rewardshub.service;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.crm.rewardshub.dto.CustomerRewardsDTO;
import com.crm.rewardshub.model.Transactions;
import com.crm.rewardshub.repository.TransactionRepository;
import com.crm.rewardshub.service.impl.RewardsServiceImpl;
import com.crm.rewardshub.utility.RewardsCalculatorUtil;
import static org.mockito.ArgumentMatchers.any;


public class RewardsServiceImplTest {

	@Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardsServiceImpl rewardsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCustomerRewards() {
    	Long customerId = 1L;
        OffsetDateTime start = OffsetDateTime.parse("2025-01-01T00:00:00Z");
        OffsetDateTime end   = OffsetDateTime.parse("2025-03-31T23:59:59Z");

        // ✅ Properly initialized transactions with non-null date and BigDecimal amount
        Transactions t1 = new Transactions();
        t1.setCustomerId(customerId);
        t1.setTransactionDate(start.plusDays(5));
        t1.setAmount(new BigDecimal("120.00"));

        Transactions t2 = new Transactions();
        t2.setCustomerId(customerId);
        t2.setTransactionDate(start.plusDays(40));
        t2.setAmount(new BigDecimal("75.00"));

        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, start, end))
                .thenReturn(List.of(t1, t2));

        CustomerRewardsDTO result = rewardsService.getCustomerRewards(customerId, start, end);

        long expectedPoints = RewardsCalculatorUtil.calculatePoints(new BigDecimal("120.00"))
                             + RewardsCalculatorUtil.calculatePoints(new BigDecimal("75.00"));


        Assertions.assertEquals(customerId, result.getCustomerId());
        Assertions.assertEquals(expectedPoints, result.getTotalPoints());
        Assertions.assertEquals(2, result.getMonthlyPoints().size());
    }

    @Test
    void testGetAllCustomerRewards() {
    	 OffsetDateTime start = OffsetDateTime.parse("2025-01-01T00:00:00Z");
         OffsetDateTime end   = OffsetDateTime.parse("2025-03-31T23:59:59Z");

         // ✅ Transactions for two customers
         Transactions t1 = new Transactions();
         t1.setCustomerId(1L);
         t1.setTransactionDate(start.plusDays(10));
         t1.setAmount(new BigDecimal("120.00"));

         Transactions t2 = new Transactions();
         t2.setCustomerId(2L);
         t2.setTransactionDate(start.plusDays(15));
         t2.setAmount(new BigDecimal("200.00"));

         when(transactionRepository.findAllBetween(start, end))
                 .thenReturn(List.of(t1, t2));

         when(transactionRepository.findByCustomerIdAndTransactionDateBetween(1L, start, end))
                 .thenReturn(List.of(t1));
         when(transactionRepository.findByCustomerIdAndTransactionDateBetween(2L, start, end))
                 .thenReturn(List.of(t2));

         List<CustomerRewardsDTO> results = rewardsService.getAllCustomerRewards(start, end);

        Assertions.assertEquals(2, results.size());
        Assertions.assertEquals(1L, results.get(0).getCustomerId());
        Assertions.assertEquals(2L, results.get(1).getCustomerId());
    }
    
    	
    @Test
    void testGetCustomerRewardsLast3Months() {
        Long customerId = 1L;

        Transactions t1 = new Transactions();
        t1.setCustomerId(customerId);
        t1.setTransactionDate(OffsetDateTime.now().minusDays(10));
        t1.setAmount(new BigDecimal("120.00"));

        // ✅ Use eq for customerId, any for dates
        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(
                eq(customerId), any(OffsetDateTime.class), any(OffsetDateTime.class)))
            .thenReturn(List.of(t1));

        CustomerRewardsDTO result = rewardsService.getCustomerRewardsLast3Months(customerId);

        long expectedPoints = RewardsCalculatorUtil.calculatePoints(new BigDecimal("120.00"));

        Assertions.assertEquals(customerId, result.getCustomerId());
        Assertions.assertEquals(expectedPoints, result.getTotalPoints());
        Assertions.assertEquals(1, result.getMonthlyPoints().size());
        Assertions.assertEquals(expectedPoints, result.getMonthlyPoints().get(0).getPoints());
    }

    @Test
    void testGetAllCustomerRewardsLast3Months() {
        Transactions t1 = new Transactions();
        t1.setCustomerId(1L);
        t1.setTransactionDate(OffsetDateTime.now().minusDays(5));
        t1.setAmount(new BigDecimal("120.00"));

        Transactions t2 = new Transactions();
        t2.setCustomerId(2L);
        t2.setTransactionDate(OffsetDateTime.now().minusDays(15));
        t2.setAmount(new BigDecimal("200.00"));

        // ✅ Both args are matchers
        when(transactionRepository.findAllBetween(any(OffsetDateTime.class), any(OffsetDateTime.class)))
            .thenReturn(List.of(t1, t2));

        // ✅ eq for customerId, any for dates
        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(
                eq(1L), any(OffsetDateTime.class), any(OffsetDateTime.class)))
            .thenReturn(List.of(t1));

        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(
                eq(2L), any(OffsetDateTime.class), any(OffsetDateTime.class)))
            .thenReturn(List.of(t2));

        List<CustomerRewardsDTO> results = rewardsService.getAllCustomerRewardsLast3Months();

        Assertions.assertEquals(2, results.size());
        Assertions.assertEquals(1L, results.get(0).getCustomerId());
        Assertions.assertEquals(2L, results.get(1).getCustomerId());
    }

}