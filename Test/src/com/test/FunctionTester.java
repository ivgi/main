package com.test;

public class FunctionTester {
	
	
	public void changeName(String name){
		name = new String("dragan");
	}
	
	public static void main(String[] args) {
		FunctionTester tester = new FunctionTester();
		String name = "ivan";
		tester.changeName(name);
		System.out.println(name);
	}

}
