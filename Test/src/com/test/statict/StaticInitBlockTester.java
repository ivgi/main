package com.test.statict;

import java.util.ArrayList;

/**
 * The purpose is to test the static initialize block and its usage.
 * Is a field in the static block available through the lifecycle of the application?
 * Is it one for all classes?
 * What happens when a new instance of a class is created?
 * @author ivan ivanov
 *
 */
public class StaticInitBlockTester {
	
	static String someString = "ivan";
	static {
		ArrayList<String> strings = new ArrayList<String>();
		strings.add(someString);
		System.out.println(strings.get(0));
		
	}

}
