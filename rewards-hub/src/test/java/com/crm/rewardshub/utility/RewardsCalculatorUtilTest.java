package com.crm.rewardshub.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.crm.rewardshub.exception.InvalidRequestException;

public class RewardsCalculatorUtilTest {
	
	 @Test
	    void calculatePoints_amountBelow50_returnsZero() {
	        long points = RewardsCalculatorUtil.calculatePoints(BigDecimal.valueOf(40));
	        assertEquals(0, points);
	    }

	    @Test
	    void calculatePoints_amountExactly50_returnsZero() {
	        long points = RewardsCalculatorUtil.calculatePoints(BigDecimal.valueOf(50));
	        assertEquals(0, points);
	    }

	    @Test
	    void calculatePoints_amountBetween50And100() {
	        long points = RewardsCalculatorUtil.calculatePoints(BigDecimal.valueOf(80));
	        // 80 - 50 = 30
	        assertEquals(30, points);
	    }

	    @Test
	    void calculatePoints_amountExactly100() {
	        long points = RewardsCalculatorUtil.calculatePoints(BigDecimal.valueOf(100));
	        // (100 - 50) = 50
	        assertEquals(50, points);
	    }

	    @Test
	    void calculatePoints_amountAbove100() {
	        long points = RewardsCalculatorUtil.calculatePoints(BigDecimal.valueOf(120));
	        // (100 - 50) + (20 * 2) = 50 + 40 = 90
	        assertEquals(90, points);
	    }

	    @Test
	    void calculatePoints_decimalAmount_decimalsIgnored() {
	        long points = RewardsCalculatorUtil.calculatePoints(BigDecimal.valueOf(120.75));
	        // decimals ignored → treated as 120
	        assertEquals(90, points);
	    }

	    @Test
	    void calculatePoints_nullAmount_throwsException() {
	        InvalidRequestException ex = assertThrows(
	                InvalidRequestException.class,
	                () -> RewardsCalculatorUtil.calculatePoints(null)
	        );
	        assertEquals("Transaction amount cannot be null", ex.getMessage());
	    }

	    @Test
	    void calculatePoints_negativeAmount_throwsException() {
	        InvalidRequestException ex = assertThrows(
	                InvalidRequestException.class,
	                () -> RewardsCalculatorUtil.calculatePoints(BigDecimal.valueOf(-10))
	        );
	        assertEquals("Transaction amount cannot be negative", ex.getMessage());
	    }
}
