package com.crm.rewardshub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crm.rewardshub.model.Customer;

@Repository
public interface CustomerRewardsRepository extends JpaRepository<Customer,Long>{

	
}
