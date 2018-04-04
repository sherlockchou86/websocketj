package com.zhzhi.websocketj.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	/**
	 * log for normal info
	 * @param log
	 */
	public static void log(String log) {
		Date day = new Date();    
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); 
		
		System.out.println("[" + df.format(day) + "] [LOG] " + log);
	}
	
	/**
	 * log for error message
	 * @param error
	 */
	public static void error(String error) {
		Date day = new Date();    
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); 
		
		System.err.println("[" + df.format(day) + "] [ERROR] " + error);
	}
	
	/**
	 * log without any other info except itself
	 * @param log
	 */
	public static void rawLog(String log) {
		System.out.println(log);
	}
}
