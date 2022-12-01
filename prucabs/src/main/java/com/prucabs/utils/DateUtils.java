package com.prucabs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.prucabs.exception.BadRequestException;

public class DateUtils {

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	
	  public static void main(String[] args) { String startDateStr =
	  "2022-12-06 14:00:00"; String endDateStr = "2023-12-06 18:00:00"; Date
	  startDate = getDate(startDateStr); Date endDate = getDate(endDateStr);
	  System.out.println(getTimeDifferenceInNumberOfDays(startDateStr,
	  endDateStr)); System.out.println(getTimeDifferenceInNumberOfDays(startDate,
	  endDate)); System.out.println(getTimeDifferenceInNumberOfHours(startDateStr,
	  endDateStr)); System.out.println(getTimeDifferenceInNumberOfHours(startDate,
	  endDate));
	  
	  System.out.println(startDate + " " + endDate); }
	 

	public static long getTimeDifferenceInNumberOfDays(String startDateStr, String endDateStr) {
		try {
			Date startDate = SIMPLE_DATE_FORMAT.parse(startDateStr);
			Date endDate = SIMPLE_DATE_FORMAT.parse(endDateStr);
			long timeDiff = endDate.getTime() - startDate.getTime();
			return TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
		} catch (ParseException e) {
			throw new BadRequestException("Date format is not correct due to: " + e.getMessage());
		}
	}

	public static long getTimeDifferenceInNumberOfDays(Date startDate, Date endDate) {
		long timeDiff = endDate.getTime() - startDate.getTime();
		return TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
	}

	public static long getTimeDifferenceInNumberOfHours(String startDateStr, String endDateStr) {
		try {
			Date startDate = SIMPLE_DATE_FORMAT.parse(startDateStr);
			Date endDate = SIMPLE_DATE_FORMAT.parse(endDateStr);
			long timeDiff = endDate.getTime() - startDate.getTime();
			return TimeUnit.HOURS.convert(timeDiff, TimeUnit.MILLISECONDS);
		} catch (ParseException e) {
			throw new BadRequestException("Date format is not correct due to: " + e.getMessage());
		}
	}

	public static long getTimeDifferenceInNumberOfHours(Date startDate, Date endDate) {
		long timeDiff = endDate.getTime() - startDate.getTime();
		return TimeUnit.HOURS.convert(timeDiff, TimeUnit.MILLISECONDS);
	}
	
	public static long getTimeDifferenceInNumberOfMinutes(Date startDate, Date endDate) {
		long timeDiff = endDate.getTime() - startDate.getTime();
		return TimeUnit.MINUTES.convert(timeDiff, TimeUnit.MILLISECONDS);
	}

	public static Date getDate(String strDate) {
		try {
			return SIMPLE_DATE_FORMAT.parse(strDate);
		} catch (ParseException e) {
			throw new BadRequestException("Date format is not correct due to: " + e.getMessage());
		}
	}
	
	public static String getDateString(Date date) {
		return SIMPLE_DATE_FORMAT.format(date);
	}
}
