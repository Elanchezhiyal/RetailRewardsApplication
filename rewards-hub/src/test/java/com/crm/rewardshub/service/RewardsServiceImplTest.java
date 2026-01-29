package com.crm.rewardshub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.crm.rewardshub.dto.CustomerRewardsDTO;
import com.crm.rewardshub.exception.CustomerNotFoundException;
import com.crm.rewardshub.exception.InvalidRequestException;
import com.crm.rewardshub.model.Transactions;
import com.crm.rewardshub.repository.CustomerRepository;
import com.crm.rewardshub.repository.TransactionRepository;
import com.crm.rewardshub.service.impl.RewardsServiceImpl;

class RewardsServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;
    
    @InjectMocks
    private RewardsServiceImpl rewardsService;

    private OffsetDateTime start;
    private OffsetDateTime end;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        start = OffsetDateTime.now().minusMonths(3);
        end = OffsetDateTime.now();
    }

    @Test
    void getCustomerRewards_success() {

        when(customerRepository.existsById(1L)).thenReturn(true);

        Transactions tx = new Transactions();
        tx.setCustomerId(1L);
        tx.setAmount(BigDecimal.valueOf(120));
        tx.setTransactionDate(OffsetDateTime.now());

        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(1L, start, end))
                .thenReturn(List.of(tx));

        CustomerRewardsDTO result =
                rewardsService.getCustomerRewards(1L, start, end);

        assertEquals(90, result.getTotalPoints());
    }

    @Test
    void getCustomerRewards_invalidCustomer() {

        when(customerRepository.existsById(99L)).thenReturn(false);

        assertThrows(CustomerNotFoundException.class, () ->
                rewardsService.getCustomerRewards(99L, start, end));
    }

    @Test
    void getCustomerRewards_negativeAmount() {

        when(customerRepository.existsById(1L)).thenReturn(true);

        Transactions tx = new Transactions();
        tx.setCustomerId(1L);
        tx.setAmount(BigDecimal.valueOf(-50));
        tx.setTransactionDate(OffsetDateTime.now());

        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(1L, start, end))
                .thenReturn(List.of(tx));

        assertThrows(InvalidRequestException.class, () ->
                rewardsService.getCustomerRewards(1L, start, end));
    }

    @Test
    void getCustomerRewards_invalidDateRange() {

        when(customerRepository.existsById(1L)).thenReturn(true);

        assertThrows(InvalidRequestException.class, () ->
                rewardsService.getCustomerRewards(
                        1L,
                        OffsetDateTime.now(),
                        OffsetDateTime.now().minusDays(1)));
    }
}
