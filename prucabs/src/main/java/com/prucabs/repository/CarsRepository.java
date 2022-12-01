package com.prucabs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prucabs.entity.Cars;

@Repository
public interface CarsRepository extends JpaRepository<Cars, Long>{

}
