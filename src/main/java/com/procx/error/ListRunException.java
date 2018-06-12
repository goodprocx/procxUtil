package com.procx.error;

import java.util.ArrayList;
import java.util.Iterator;

public class ListRunException {

	public static void main(String[] args) {
		
		ArrayList<String> list = new ArrayList();

		list.add("a");
		list.add("b");
		list.add("c");
		
		//由于list自身方法add,remove成功了都会对list的修改次数都有个记录，list迭代器内部类有修改次数的变量，next方法都会进行比较，不相等就会抛出
		for (Iterator<String> it = list.iterator(); it.hasNext();) { 
            String val = it.next();
            if (val.equals("c")) {
                list.add(val); //报错
//              list.remove(val);   //报错    
            }
        }
		
		//正确用法
		for(Iterator<String> it = list.iterator();it.hasNext();){
			 String s = it.next();
			 if(s.equals("c")){
				 it.remove();
			 }
		}
	}

}
