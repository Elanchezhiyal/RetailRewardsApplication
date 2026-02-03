package com.crm.rewardshub.utility;

import java.math.BigDecimal;

import com.crm.rewardshub.exception.InvalidRequestException;

public class RewardsCalculatorUtil {

	public static Long calculatePoints(BigDecimal amount) {

        if (amount == null) {
            throw new InvalidRequestException("Transaction amount cannot be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidRequestException("Transaction amount cannot be negative");
        }

        Long dollars = amount.longValue(); // decimals intentionally ignored
        Long points = 0L;
        if (dollars > 100) {
            points += (dollars - 100) * 2;
        }
        else if (dollars > 50) {
            points += Math.min(dollars, 100) - 50;
        }
        
        return points;
    }
}
