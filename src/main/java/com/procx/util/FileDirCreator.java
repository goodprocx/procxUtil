package com.procx.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class FileDirCreator {
	public static final String fseparator = "/" ;
	public static final String cseparator ="-" ;
	private static final String fileType = ".pdf" ;
	public static final String dotPoint = "." ;
	private static final int randNumLen = 4 ; 
	
	
	
	
	public static String getYear(){
		Calendar cl = Calendar.getInstance();
		return String.valueOf(cl.get(Calendar.YEAR));
	}
	
	public static String getDay(){
		Calendar cl = Calendar.getInstance();
		StringBuilder sb = new StringBuilder(240) ;
		return sb.append("23").append( cseparator ).append( cl.get(Calendar.MONTH)+1 ).append( cseparator ).append( cl.get(Calendar.DAY_OF_MONTH)).toString();
	}
	
	public static String getMonth(){
		Calendar cl = Calendar.getInstance();
		return String.valueOf(cl.get(Calendar.MONTH)+1);
	}
	
	public static String getStaffCode(Map context){
		String staffCode =context.get("STAFF_ID")+"";
		return staffCode;
	}
	
	
}
