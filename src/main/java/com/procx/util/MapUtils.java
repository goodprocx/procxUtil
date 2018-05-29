package com.procx.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapUtils {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("cx", null);
		map.put("c","111");
		map.put("x", null);
//		MapUtils.transformNullforValue(map);
		System.out.println(map);
	}
	
	/**
	 * object只针对属性是String,map类型的 
	 * @param o
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	public static Map<String,Object> transformMapFromObject(Object o) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		Class c = o.getClass();
		Field[] fields =  c.getDeclaredFields();
		for(int i = 0;i<fields.length;i++){
			String key = fields[i].getName();
			if("serialVersionUID".equals(key)){
				continue;
			}
			Method method = c.getMethod("get"+key,new Class[]{});
			Object value = method.invoke(o, new Object[]{});
			map.put(key,value);
		}
		return map;
	}

	public static void transformNullforValue(Map<String,Object> map){
		Iterator<String> it =  map.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			Object value = map.get(key);
			if(value == null){
				map.put(key, "");
			}
		}
		
	}
}
