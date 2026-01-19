package com.crm.rewardshub.utility;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RewardsCalculatorUtilTest {
	
	@Test
    void testNullAmountReturnsZero() {
        Assertions.assertEquals(0L, RewardsCalculatorUtil.calculatePoints(null));
    }

    @Test
    void testAmountLessThanOrEqual50ReturnsZero() {
        Assertions.assertEquals(0L, RewardsCalculatorUtil.calculatePoints(BigDecimal.valueOf(50)));
        Assertions.assertEquals(0L, RewardsCalculatorUtil.calculatePoints(BigDecimal.valueOf(30)));
    }

    @Test
    void testAmountBetween51And100() {
        Assertions.assertEquals(25L, RewardsCalculatorUtil.calculatePoints(BigDecimal.valueOf(75)));
        Assertions.assertEquals(49L, RewardsCalculatorUtil.calculatePoints(BigDecimal.valueOf(99)));
    }

    @Test
    void testAmountExactly100() {
        Assertions.assertEquals(50L, RewardsCalculatorUtil.calculatePoints(BigDecimal.valueOf(100)));
    }

    @Test
    void testAmountGreaterThan100() {
        Assertions.assertEquals(90L, RewardsCalculatorUtil.calculatePoints(BigDecimal.valueOf(120)));

        Assertions.assertEquals(250L, RewardsCalculatorUtil.calculatePoints(BigDecimal.valueOf(200)));
    }

    @Test
    void testAmountWithCentsTruncated() {
        Assertions.assertEquals(90L, RewardsCalculatorUtil.calculatePoints(BigDecimal.valueOf(120.75)));
    }

}
