package com.test.statict;

public class SingletonPatternTester {
	
	//create an object of SingletonPatternTester
	private static SingletonPatternTester instance = new SingletonPatternTester();
	
	//make the constructor private so that this class cannot be
	//instantiated
	private SingletonPatternTester(){};
	
	public static SingletonPatternTester getInstance(){
		return instance;
	}
	
	public void showMessage(){
		System.out.println("This is: " + instance);
	}

}
