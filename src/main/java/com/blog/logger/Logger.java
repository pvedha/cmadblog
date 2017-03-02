package com.blog.logger;

public class Logger {
	
	private Logger(){
		//do nothing for sonarqube
	}
	public static void log(String message){
		System.out.println(message);
	}

}
