package com.crm.rewardshub.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.crm.rewardshub.model.Transactions;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions,Long>{

	List<Transactions> findByCustomerIdAndTransactionDateBetween(long customerId, OffsetDateTime start
			,OffsetDateTime  end);
	
	@Query("SELECT t FROM Transactions t WHERE t.transactionDate BETWEEN :startDate AND :endDate")
	List<Transactions> findAllBetween(OffsetDateTime  start,OffsetDateTime  end);
}
