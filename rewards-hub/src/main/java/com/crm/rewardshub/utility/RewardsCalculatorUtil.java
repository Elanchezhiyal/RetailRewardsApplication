package com.crm.rewardshub.utility;

import java.math.BigDecimal;

public class RewardsCalculatorUtil {

	 public static long calculatePoints(BigDecimal amount) {
		    if (amount == null) return 0L;
		    long dollars = amount.longValue(); // truncate cents per typical rule
		    long points = 0L;

		    if (dollars > 100) {
		      points += 2L * (dollars - 100);
		      points += 50L; 
		    } else if (dollars > 50) {
		      points += (dollars - 50);
		    }
		    return points;
		  }

}
