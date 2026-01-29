package com.crm.rewardshub.exception;

public class CustomerNotFoundException extends RuntimeException {
	public CustomerNotFoundException(String msg) { 
		super(msg); 
	}
}