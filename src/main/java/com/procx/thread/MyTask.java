package com.procx.thread;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MyTask extends TimerTask {

	@Override
	public void run() {
		 System.out.println("任务执行了，时间为："+new Date());

	}

	public static void main(String[] args) {
		System.out.println("系统当前时间："+new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 10);
        Date date = calendar.getTime();
        MyTask task = new MyTask();
        Timer timer = new Timer(true);//守护线程
        timer.schedule(task, date);

	}

}
