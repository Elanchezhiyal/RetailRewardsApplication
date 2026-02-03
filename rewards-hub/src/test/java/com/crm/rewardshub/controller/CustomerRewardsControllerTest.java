package com.crm.rewardshub.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.crm.rewardshub.api.ResponseJson;
import com.crm.rewardshub.dto.CustomerRewardsDTO;
import com.crm.rewardshub.dto.MonthlyPointsDTO;
import com.crm.rewardshub.service.impl.RewardsServiceImpl;

@ExtendWith(MockitoExtension.class)
class CustomerRewardsControllerTest {

    @Mock
    private RewardsServiceImpl rewardsService;

    @InjectMocks
    private CustomerRewardsController controller;

    // ---------------- getCustomerRewards ----------------

    @Test
    void getCustomerRewards_success() {

        long customerId = 1L;
        OffsetDateTime startDate = OffsetDateTime.parse("2024-01-01T00:00:00Z");
        OffsetDateTime endDate = OffsetDateTime.parse("2024-03-31T23:59:59Z");

        CustomerRewardsDTO dto = buildCustomerRewardsDTO(customerId);

        when(rewardsService.getCustomerRewards(customerId, startDate, endDate))
                .thenReturn(dto);

        ResponseEntity<ResponseJson<CustomerRewardsDTO>> response =
                controller.getCustomerRewards(customerId, startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerId, response.getBody().getPayload().getCustomerId());

        verify(rewardsService).getCustomerRewards(customerId, startDate, endDate);
    }

    // ---------------- getAllCustomerRewards ----------------

    @Test
    void getAllCustomerRewards_success() {

        OffsetDateTime startDate = OffsetDateTime.parse("2024-01-01T00:00:00Z");
        OffsetDateTime endDate = OffsetDateTime.parse("2024-03-31T23:59:59Z");

        when(rewardsService.getAllCustomerRewards(startDate, endDate))
                .thenReturn(List.of(
                        buildCustomerRewardsDTO(1L),
                        buildCustomerRewardsDTO(2L)
                ));

        ResponseEntity<ResponseJson<List<CustomerRewardsDTO>>> response =
                controller.getAllCustomerRewards(startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(rewardsService).getAllCustomerRewards(startDate, endDate);
    }

    // ---------------- getCustomerRewardsLast3Months ----------------

    @Test
    void getCustomerRewardsLast3Months_success() {

        when(rewardsService.getCustomerRewardsLast3Months(1L))
                .thenReturn(buildCustomerRewardsDTO(1L));

        ResponseEntity<ResponseJson<CustomerRewardsDTO>> response =
                controller.getCustomerRewardsLast3Months(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getPayload().getCustomerId());

        verify(rewardsService).getCustomerRewardsLast3Months(1L);
    }

    // ---------------- getAllCustomerRewardsLast3Months ----------------

    @Test
    void getAllCustomerRewardsLast3Months_success() {

        when(rewardsService.getAllCustomerRewardsLast3Months())
                .thenReturn(List.of(
                        buildCustomerRewardsDTO(1L),
                        buildCustomerRewardsDTO(2L)
                ));

        ResponseEntity<ResponseJson<List<CustomerRewardsDTO>>> response =
                controller.getAllCustomerRewardsLast3Months();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getPayload().size());

        verify(rewardsService).getAllCustomerRewardsLast3Months();
    }

    // ===================== TEST DATA =====================

    private CustomerRewardsDTO buildCustomerRewardsDTO(Long customerId) {

        MonthlyPointsDTO monthlyPoints = new MonthlyPointsDTO(
                "2024-01",
                100L,
                List.of()
        );

        return new CustomerRewardsDTO(
                customerId,
                List.of(monthlyPoints),
                100L
        );
    }
}
