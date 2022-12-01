package com.prucabs.service;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prucabs.entity.Cars;
import com.prucabs.repository.CustomCarsRepository;

@ExtendWith(MockitoExtension.class)
public class CarsServiceTest {

	@Mock
	private CustomCarsRepository customCarsRepository;
	
	@InjectMocks
	private CarsService carsService;
	
	@Test
	public void getCarsModelIdentifierTest() {
		Cars cars = new Cars();
		cars.setManufacturer("TOYOTA");
		cars.setModel("CAMRY");
		
		String actualResult = carsService.getCarsModelIdentifier(cars);
		Assertions.assertEquals("TOYOTA_CAMRY", actualResult);
		
		cars.setManufacturer("BMW");
		cars.setModel("650");
		actualResult = carsService.getCarsModelIdentifier(cars);
		Assertions.assertNotEquals("BMW_6501", actualResult);
	}
	
	@Test
	public void getAllCarsTest() {
		Cars c1 = new Cars();
		c1.setCarId(1L);
		c1.setManufacturer("TOYOTA");
		c1.setModel("CAMARY");
		
		Cars c2 = new Cars();
		c2.setCarId(2L);
		c2.setManufacturer("BMW");
		c2.setModel("650");
		
		List<Cars> carsList = new ArrayList<>();
		carsList.add(c1);
		carsList.add(c2);
		
		when(customCarsRepository.getCarsByFilter(Mockito.anyString())).thenReturn(carsList);
		
		Map<String, String> map = new HashMap<>();
		map.put("manufacturer", "TOYOTA");
		List<Cars> actualResult = carsService.getAllCars(map);
		
		Assertions.assertEquals(2, carsList.size());
		Assertions.assertEquals("TOYOTA", actualResult.get(0).getManufacturer());
	}
}
