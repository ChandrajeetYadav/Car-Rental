package com.prucabs.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.prucabs.entity.CarsOrders;

@Repository
public interface CarsOrdersRepository extends JpaRepository<CarsOrders, String>{
	
	@Query("SELECT count(c) FROM Cars c LEFT JOIN CarsOrders co ON c.carId = co.carId WHERE co.carId = :carId AND co.status = :status AND ((co.startDate >= :startDate AND co.startDate < :endDate) OR (co.endDate >= :startDate AND co.endDate < :endDate) OR (co.startDate <= :startDate AND co.endDate >= :endDate))")
	public int getExistingCarBookings(Long carId, String status, Date startDate, Date endDate);
}
