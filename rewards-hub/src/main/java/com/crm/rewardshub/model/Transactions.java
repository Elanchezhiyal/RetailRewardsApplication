package com.crm.rewardshub.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name="transactions",indexes= {
		@Index(name="idx_tx_customer_date", columnList="customer_id, tx_date")
})
public class Transactions {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable=false, name="customer_id")
	private long customerId;
	
	@Column(nullable=false, name="tx_date")
	private OffsetDateTime transactionDate;
	
	@Column(nullable=false, precision=12, scale=2)
	private BigDecimal amount;
	
	@Column(nullable=false)
	private String reference;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	
	public OffsetDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(OffsetDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
	
	
}
