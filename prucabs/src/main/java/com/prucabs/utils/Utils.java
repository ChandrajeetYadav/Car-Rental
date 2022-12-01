package com.prucabs.utils;

public class Utils {
	public static boolean isEmpty(String str) {
		return str == null || str.isEmpty() || str.equalsIgnoreCase("null");
	}
}
