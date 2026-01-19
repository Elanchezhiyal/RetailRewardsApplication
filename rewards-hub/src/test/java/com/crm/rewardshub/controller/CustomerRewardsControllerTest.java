package com.crm.rewardshub.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.crm.rewardshub.dto.CustomerRewardsDTO;
import com.crm.rewardshub.dto.MonthlyPointsDTO;
import com.crm.rewardshub.service.RewardsService;

@WebMvcTest(CustomerRewardsController.class)
class CustomerRewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean   // new annotation in Spring Boot 3.3+
    private RewardsService rewardsService;

    @Test
    void testGetCustomerRewards() throws Exception {
        Long customerId = 1L;
        OffsetDateTime start = OffsetDateTime.parse("2025-01-01T00:00:00Z");
        OffsetDateTime end   = OffsetDateTime.parse("2025-03-31T23:59:59Z");

        CustomerRewardsDTO dto = new CustomerRewardsDTO(
                customerId,
                List.of(
                        new MonthlyPointsDTO(2025, 1, 115),
                        new MonthlyPointsDTO(2025, 2, 250),
                        new MonthlyPointsDTO(2025, 3, 110)
                ),
                475
        );

        when(rewardsService.getCustomerRewards(customerId, start, end)).thenReturn(dto);

        mockMvc.perform(get("/api/rewards/customers/{id}", customerId)
                        .param("startDate", "2025-01-01T00:00:00Z")
                        .param("endDate", "2025-03-31T23:59:59Z")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.totalPoints").value(475))
                .andExpect(jsonPath("$.monthlyPoints[0].points").value(115));
    }

    @Test
    void testGetAllCustomerRewards() throws Exception {
        OffsetDateTime start = OffsetDateTime.parse("2025-01-01T00:00:00Z");
        OffsetDateTime end   = OffsetDateTime.parse("2025-03-31T23:59:59Z");

        List<CustomerRewardsDTO> all = List.of(
                new CustomerRewardsDTO(1L,
                        List.of(new MonthlyPointsDTO(2025, 1, 115)),
                        115),
                new CustomerRewardsDTO(2L,
                        List.of(new MonthlyPointsDTO(2025, 1, 1)),
                        1)
        );

        when(rewardsService.getAllCustomerRewards(start, end)).thenReturn(all);

        mockMvc.perform(get("/api/rewards/customers")
                        .param("startDate", "2025-01-01T00:00:00Z")
                        .param("endDate", "2025-03-31T23:59:59Z")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[1].customerId").value(2));
    }
    
    @Test
    void testGetCustomerRewardsLast3Months() throws Exception {
        Long customerId = 1L;

        CustomerRewardsDTO dto = new CustomerRewardsDTO(
                customerId,
                List.of(
                        new MonthlyPointsDTO(2025, 11, 90),
                        new MonthlyPointsDTO(2025, 12, 25),
                        new MonthlyPointsDTO(2026, 1, 50)
                ),
                165
        );

        when(rewardsService.getCustomerRewardsLast3Months(customerId)).thenReturn(dto);

        mockMvc.perform(get("/api/rewards/customers/{id}/last3months", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.totalPoints").value(165))
                .andExpect(jsonPath("$.monthlyPoints[0].points").value(90));
    }

    @Test
    void testGetAllCustomerRewardsLast3Months() throws Exception {
        List<CustomerRewardsDTO> all = List.of(
                new CustomerRewardsDTO(1L,
                        List.of(new MonthlyPointsDTO(2025, 11, 90)),
                        90),
                new CustomerRewardsDTO(2L,
                        List.of(new MonthlyPointsDTO(2025, 12, 250)),
                        250)
        );

        when(rewardsService.getAllCustomerRewardsLast3Months()).thenReturn(all);

        mockMvc.perform(get("/api/rewards/customers/last3months")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[1].customerId").value(2));
    }

}
