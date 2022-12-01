package com.prucabs.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TRANSACTION_DETAIL")
public class TransactionDetail {
	@Id
	@Column(name = "TRANSACTION_ID")
	private String transactionId;

	@Column(name = "ORDER_ID")
	private String orderId;

	@Column(name = "CURRENCY")
	private String currency;

	@Column(name = "AMOUNT")
	private Double amount;

	@Column(name = "TRANSACTION_DATE")
	private Date transactionDate;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "PAYMENT_MODE")
	private String paymentMode;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
