package com.crm.rewardshub.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.rewardshub.model.Transactions;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {
	
	List<Transactions> findByCustomerIdAndTransactionDateBetween(
            long customerId,
            OffsetDateTime startDate,
            OffsetDateTime endDate
    );

    List<Transactions> findByTransactionDateBetween(
            OffsetDateTime startDate,
            OffsetDateTime endDate
    );
}
