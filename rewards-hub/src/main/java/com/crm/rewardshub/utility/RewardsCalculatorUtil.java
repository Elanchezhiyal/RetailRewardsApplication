package com.crm.rewardshub.utility;

import java.math.BigDecimal;

import com.crm.rewardshub.exception.InvalidRequestException;

public class RewardsCalculatorUtil {

	public static long calculatePoints(BigDecimal amount) {

        if (amount == null) {
            throw new InvalidRequestException("Transaction amount cannot be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidRequestException("Transaction amount cannot be negative");
        }

        long dollars = amount.longValue(); // decimals intentionally ignored
        long points = 0;

        if (dollars > 50) {
            points += Math.min(dollars, 100) - 50;
        }
        if (dollars > 100) {
            points += (dollars - 100) * 2;
        }
        return points;
    }
}
