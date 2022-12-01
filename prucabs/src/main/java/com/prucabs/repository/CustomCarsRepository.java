package com.prucabs.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.prucabs.entity.Cars;

@Repository
public class CustomCarsRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	public List<Cars> getCarsByFilter(String query) {
		return entityManager.createQuery(query).getResultList();
	}
}
