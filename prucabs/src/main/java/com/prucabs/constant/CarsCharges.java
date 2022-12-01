package com.prucabs.constant;

import java.util.HashMap;
import java.util.Map;

public class CarsCharges {
	private static Map<String, Double> priceMap = new HashMap<>();
	
	static {
		priceMap.put("TOYOTA_CAMRY", 100.0);
		priceMap.put("BMW_650", 500.0);
	}
	
	public static double getPrice(String model) {
		return priceMap.getOrDefault(model, 0.0);
	}
}
