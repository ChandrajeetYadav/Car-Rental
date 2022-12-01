package com.prucabs.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prucabs.entity.Cars;
import com.prucabs.service.CarsService;

@RestController
@RequestMapping("/cars")
public class CarsController {
	
	@Autowired
	private CarsService carsService;

	@GetMapping
	public List<Cars> getCars(@RequestParam Map<String, String> queryParamsMap) {	
		System.out.println(queryParamsMap);
		return carsService.getAllCars(queryParamsMap);
	}
}
