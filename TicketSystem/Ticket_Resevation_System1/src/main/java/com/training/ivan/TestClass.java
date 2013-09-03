package com.training.ivan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestClass {
	
	final Logger logger = LoggerFactory.getLogger(TestClass.class);
	
	TestClass(){
		logger.info("Writing message");
		logger.warn("Writing message");
		logger.debug("Writing message");
		logger.error("Writing message");
	}
	
	public static void main(String args[]){
		
		TestClass test = new TestClass();
				
	}

}
