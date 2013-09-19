package com.test;


/**
 * 
 * @author ivaniv
 *
 *  Class for testing log4j vie slf4j functionality
 */

public class Test {
	
	Test(){
		int a = 6;
		int b = 0;
		System.out.println("test started!");
		int c;
		
		try{
			c = a/b;
			System.out.println("After exception occured");
		}catch(ArithmeticException e){
			System.out.println("oops devision by zero occured ");
			throw e;//important; delete to see the difference
		}finally{
			System.out.println("Executing finally");
		}
		System.out.println("Continue in the main block ...");
	}

	
	public static void main(String args[]){
		new Test();
		System.out.println("in main method");
	}

}
