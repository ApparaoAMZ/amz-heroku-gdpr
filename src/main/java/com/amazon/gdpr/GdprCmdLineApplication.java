package com.amazon.gdpr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.amazon.gdpr.service.InitService;
import com.amazon.gdpr.util.GlobalConstants;

/****************************************************************************************
 * This is the Spring Boot Servlet Initializer class for GDPR Application web
 * This will be initiated during application deployment in Heroku
 ****************************************************************************************/
@SpringBootApplication
public class GdprCmdLineApplication {// implements CommandLineRunner {
	
	private static String CURRENT_CLASS		 		= GlobalConstants.CLS_GDPRCMDLINEAPPLICATION;
	
	@Autowired
	InitService initService;	
	
	/**
	 * This is the class initiated during command line runtime
	 * @param args
	 */
	public static void main(String[] args) {
		String CURRENT_METHOD = "main";		
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+":: Inside method");
		
		SpringApplication.run(GdprCmdLineApplication.class, args);
	}
	
	public void run(){
		String CURRENT_METHOD = "run";		
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+":: Inside method");
		
		String status = initService.initService("Test Run");
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: status "+status);
	}
}