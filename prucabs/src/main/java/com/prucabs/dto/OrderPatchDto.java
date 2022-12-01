package com.prucabs.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

public class OrderPatchDto {
	
	private String customerName;
	
	@Email
	private String customerEmail;
	
	@Pattern(regexp = "(^$|[0-9]{10})")
	private String customerPhoneNumber;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerPhoneNumber() {
		return customerPhoneNumber;
	}

	public void setCustomerPhoneNumber(String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}
	
	
}
