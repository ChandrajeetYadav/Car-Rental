package com.prucabs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CARS_INVENTORY")
public class CarsInventory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CI_ID")
	private Long id;
	
	@Column(name = "CARS_MODEL_IDENTIFIER", unique = true)
	private String carsModelIdentifier;
	
	@Column(name = "STOCK")
	private Integer stock;
	
	@Column(name = "PRICE")
	private Double price;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCarsModelIdentifier() {
		return carsModelIdentifier;
	}

	public void setCarsModelIdentifier(String carsModelIdentifier) {
		this.carsModelIdentifier = carsModelIdentifier;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	
}
