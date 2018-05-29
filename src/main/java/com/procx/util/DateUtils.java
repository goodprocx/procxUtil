package com.procx.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;



final public class DateUtils {
	
	public static final String ShortDayFormatter = "yyyyMMdd";

	public static final String LongDayFormatter = "yyyyMMddHHmmss";

	public static final String MonthFormatter = "yyyyMM";
	
	
	public synchronized static String conversionDateTimeFormat(String source,
			String formatter, String formatter2) {
		DateFormat format = new SimpleDateFormat(formatter);
		DateFormat format2 = new SimpleDateFormat(formatter2);
		String datetime = "";
		try {
			Date date = format.parse(source);
			datetime = format2.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return datetime;
	}
	
	public synchronized static String getDateString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date(System.currentTimeMillis()));
	}
	
	public synchronized static String getCurrentYearString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(new Date(System.currentTimeMillis()));
	}
	
	public synchronized static String getSimpleDateString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date(System.currentTimeMillis()));
	}
	
	public synchronized static String getDateTimeString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(System.currentTimeMillis()));
	}
	
	public synchronized static String getDateString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	public synchronized static String getDateTimeString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	public synchronized static String getDateString(long date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date(date));
	}
	
	public synchronized static String getDateTimeString(long date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(date));
	}
	
	public synchronized static String getDateTimeString(String formater) {
		SimpleDateFormat sdf = new SimpleDateFormat(formater);
		return sdf.format(new Date(System.currentTimeMillis()));
	}
	
	public synchronized static String getDateTimeString(Date date,
			String formater) {
		SimpleDateFormat sdf = new SimpleDateFormat(formater);
		return sdf.format(date);
	}
	
	public synchronized static String getDateTimeString(long date,
			String formater) {
		SimpleDateFormat df = new SimpleDateFormat(formater);
		return df.format(new Date(date));
	}
	
	public synchronized static Date parseStrToDate(String dateStr)
			throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.parse(dateStr);
	}
	
	public synchronized static Date parseStrToDate(String dateStr,
			String formater) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat(formater);
		return df.parse(dateStr);
	}
	
	public synchronized static Date parseStrToDateTime(String dateStr)
			throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.parse(dateStr);
	}
	
	public synchronized static Date parseStrToDateTime(String dateStr,
			String formater) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat(formater);
		return df.parse(dateStr);
	}
	
	public synchronized static Date addDay(Date date, int day) throws Exception {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(GregorianCalendar.DATE, day);
		return cal.getTime();
	}
	
	public synchronized static Date addMonth(Date date, int month)
			throws Exception {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(GregorianCalendar.MONTH, month);
		return cal.getTime();
	}
	
	public synchronized static Date addYear(Date date, int year)
			throws Exception {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(GregorianCalendar.YEAR, year);
		return cal.getTime();
	}
	
	
	
	public static String getTransactionID(String formater,int length){
		String sTime = getDateTimeString(new Date(), formater);
		return sTime+ getRandomString(length);
	}
	
	//获取指定位数的随机字符串(包含小写字母、大写字母、数字,0<length)
	public static String getRandomString(int length) {
	    //随机字符串的随机字符库
	    String KeyString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	    StringBuffer sb = new StringBuffer();
	    int len = KeyString.length();
	    for (int i = 0; i < length; i++) {
	       sb.append(KeyString.charAt((int) Math.round(Math.random() * (len - 1))));
	    }
	    return sb.toString();
	}
}
