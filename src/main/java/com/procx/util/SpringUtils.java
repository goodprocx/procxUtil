package com.procx.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 */
public class SpringUtils {

	private static ApplicationContext applicationContext = null;

	
	public static void init() {
		if (applicationContext == null) {
			applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		}
	}
	
	
	public static Object getResourceDAO(String beanName) {
		return  applicationContext.getBean("beanName");
	}

}
