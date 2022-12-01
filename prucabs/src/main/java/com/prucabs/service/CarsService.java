package com.prucabs.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prucabs.entity.Cars;
import com.prucabs.repository.CustomCarsRepository;

@Service
public class CarsService {
	
	private static final List<String> carsFilterColumn = Arrays.asList("manufacturer", "fuelType", "category");
	
	@Autowired
	private CustomCarsRepository customCarsRepository;
	
	public List<Cars> getAllCars(Map<String, String> queryParamMap) {
		Map<String, String> filterMap = parseFilters(queryParamMap);
		String query = getQueryForCarsFilter(filterMap);
		return customCarsRepository.getCarsByFilter(query);
	}
	
	public String getCarsModelIdentifier(Cars cars) {
		return cars.getManufacturer() + "_" + cars.getModel();
	}
	
	private Map<String, String> parseFilters(Map<String, String> queryParamMap) {
		Map<String, String> filterMap = new HashMap<>();
		String val = queryParamMap.getOrDefault("filters", "");
		if (!val.equals("")) {
			List<String> filterList = Arrays.asList(val.split("_"));
			filterList.stream().forEach(filters -> {
				String[] arr = filters.split(":");
				if (arr.length == 2 && carsFilterColumn.contains(arr[0]))
					filterMap.put(arr[0], arr[1].toUpperCase());
			});
		}
		return filterMap;
	}
	
	private String getQueryForCarsFilter(Map<String, String> filterMap) {
		String query = "SELECT c FROM Cars c WHERE c.id != 0 %s";
		StringBuilder sb = new StringBuilder("");
		if (!filterMap.isEmpty()) {
			filterMap.forEach((k,v) -> sb.append(" AND ").append(k).append(" like ").append("'%").append(v).append("%'"));
		}
		return String.format(query, sb.toString());
	}

}
