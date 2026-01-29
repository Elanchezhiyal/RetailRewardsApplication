package com.crm.rewardshub.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crm.rewardshub.dto.CustomerRewardsDTO;
import com.crm.rewardshub.dto.MonthlyPointsDTO;
import com.crm.rewardshub.service.RewardsService;

class CustomerRewardsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RewardsService rewardsService;

    @InjectMocks
    private CustomerRewardsController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    private CustomerRewardsDTO mockDto() {
        return new CustomerRewardsDTO(
                1L,
                List.of(new MonthlyPointsDTO(2024, 1, 120)),
                120L
        );
    }

    @Test
    void getCustomerRewards_success() throws Exception {

        when(rewardsService.getCustomerRewards(
                eq(1L), any(OffsetDateTime.class), any(OffsetDateTime.class)))
                .thenReturn(mockDto());

        mockMvc.perform(get("/api/rewards/customers/1")
                        .param("startDate", "2024-01-01T00:00:00Z")
                        .param("endDate", "2024-03-31T23:59:59Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.payload.customerId").value(1))
                .andExpect(jsonPath("$.payload.totalPoints").value(120));
    }

    @Test
    void getAllCustomerRewards_success() throws Exception {

        when(rewardsService.getAllCustomerRewards(any(), any()))
                .thenReturn(List.of(mockDto()));

        mockMvc.perform(get("/api/rewards/customers")
                        .param("startDate", "2024-01-01T00:00:00Z")
                        .param("endDate", "2024-03-31T23:59:59Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload[0].customerId").value(1));
    }

    @Test
    void getCustomerRewardsLast3Months_success() throws Exception {

        when(rewardsService.getCustomerRewardsLast3Months(1L))
                .thenReturn(mockDto());

        mockMvc.perform(get("/api/rewards/customers/1/last3months"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.totalPoints").value(120));
    }

    @Test
    void getAllCustomerRewardsLast3Months_success() throws Exception {

        when(rewardsService.getAllCustomerRewardsLast3Months())
                .thenReturn(List.of(mockDto()));

        mockMvc.perform(get("/api/rewards/customers/last3months"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").isArray());
    }
}
