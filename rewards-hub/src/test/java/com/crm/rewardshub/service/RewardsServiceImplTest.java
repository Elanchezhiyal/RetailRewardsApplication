package com.crm.rewardshub.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crm.rewardshub.dto.CustomerRewardsDTO;
import com.crm.rewardshub.exception.CustomerNotFoundException;
import com.crm.rewardshub.model.Customer;
import com.crm.rewardshub.model.Transactions;
import com.crm.rewardshub.repository.CustomerRepository;
import com.crm.rewardshub.repository.TransactionRepository;
import com.crm.rewardshub.service.impl.RewardsServiceImpl;

@ExtendWith(MockitoExtension.class)
class RewardsServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RewardsServiceImpl rewardsService;

    private Customer customer;
    private Transactions tx1;
    private Transactions tx2;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        OffsetDateTime now = OffsetDateTime.now();

        tx1 = new Transactions();
        tx1.setId(100L);
        tx1.setCustomerId(1L);
        tx1.setAmount(BigDecimal.valueOf(120));
        tx1.setTransactionDate(now.minusMonths(1));

        tx2 = new Transactions();
        tx2.setId(101L);
        tx2.setCustomerId(1L);
        tx2.setAmount(BigDecimal.valueOf(90));
        tx2.setTransactionDate(now.minusMonths(1));
    }

    // ================= SINGLE CUSTOMER (CUSTOM RANGE) =================

    @Test
    void getCustomerRewards_success() {

        OffsetDateTime start = OffsetDateTime.now().minusMonths(2);
        OffsetDateTime end = OffsetDateTime.now();

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(transactionRepository
                .findByCustomerIdAndTransactionDateBetween(1L, start, end))
                .thenReturn(List.of(tx1, tx2));

        CustomerRewardsDTO result =
                rewardsService.getCustomerRewards(1L, start, end);

        assertNotNull(result);
        assertEquals(1L, result.getCustomerId());
        assertEquals("John Doe", result.getCustomerName());
        assertEquals(1, result.getMonthlyPoints().size());
        assertTrue(result.getTotalPoints() > 0);
    }

    // ================= CUSTOMER NOT FOUND =================

    @Test
    void getCustomerRewards_customerNotFound() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () ->
                rewardsService.getCustomerRewards(
                        1L,
                        OffsetDateTime.now().minusMonths(1),
                        OffsetDateTime.now()
                ));
    }

    // ================= DATE VALIDATION =================

    @Test
    void getCustomerRewards_invalidDateRange() {

        OffsetDateTime start = OffsetDateTime.now();
        OffsetDateTime end = start.minusDays(1);

        assertThrows(IllegalArgumentException.class, () ->
                rewardsService.getCustomerRewards(1L, start, end));
    }

    // ================= SINGLE CUSTOMER (LAST 3 MONTHS) =================

    @Test
    void getCustomerRewardsLast3Months_success() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(transactionRepository
                .findByCustomerIdAndTransactionDateBetween(anyLong(), any(), any()))
                .thenReturn(List.of(tx1));

        CustomerRewardsDTO result =
                rewardsService.getCustomerRewardsLast3Months(1L);

        assertEquals("John Doe", result.getCustomerName());
        assertEquals(1, result.getMonthlyPoints().size());
    }

    // ================= ALL CUSTOMERS (CUSTOM RANGE) =================

    @Test
    void getAllCustomerRewards_success() {

        OffsetDateTime start = OffsetDateTime.now().minusMonths(2);
        OffsetDateTime end = OffsetDateTime.now();

        when(transactionRepository.findByTransactionDateBetween(start, end))
                .thenReturn(List.of(tx1, tx2));

        when(customerRepository.findAllById(anySet()))
                .thenReturn(List.of(customer));

        List<CustomerRewardsDTO> results =
                rewardsService.getAllCustomerRewards(start, end);

        assertEquals(1, results.size());
        assertEquals("John Doe", results.get(0).getCustomerName());
        assertTrue(results.get(0).getTotalPoints() > 0);
    }

    // ================= ALL CUSTOMERS (LAST 3 MONTHS) =================

    @Test
    void getAllCustomerRewardsLast3Months_success() {

        when(transactionRepository
                .findByTransactionDateBetween(any(), any()))
                .thenReturn(List.of(tx1));

        when(customerRepository.findAllById(anySet()))
                .thenReturn(List.of(customer));

        List<CustomerRewardsDTO> results =
                rewardsService.getAllCustomerRewardsLast3Months();

        assertEquals(1, results.size());
        assertEquals("John Doe", results.get(0).getCustomerName());
    }
}
