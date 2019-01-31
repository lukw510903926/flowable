package com.flowable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlowableApplication {

	private static Logger logger = LoggerFactory.getLogger(FlowableApplication.class);
	
	public static void main(String[] args){
		
		SpringApplication.run(FlowableApplication.class, args);
		logger.info("flowable application start successfully-------");
	}

}
