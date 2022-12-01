package com.prucabs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prucabs.entity.TransactionDetail;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionDetail, String>{

}
