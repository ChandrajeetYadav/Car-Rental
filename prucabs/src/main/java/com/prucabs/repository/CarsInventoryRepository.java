package com.prucabs.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.prucabs.entity.CarsInventory;

@Repository
public interface CarsInventoryRepository extends JpaRepository<CarsInventory, Long>{
	
	@Transactional
	@Modifying
	@Query("UPDATE CarsInventory ci  set ci.stock = ci.stock - 1 where ci.carsModelIdentifier = :carsModelIdentifier")
	public int updateStockInventory(String carsModelIdentifier);
}
