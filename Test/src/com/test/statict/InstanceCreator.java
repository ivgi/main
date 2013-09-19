package com.test.statict;

public class InstanceCreator {

	public static void main(String[] args) {

		/*
		 * we create three instances Notice that we can not access the ArrayList
		 * in which is in the static block
		 */

		System.out.println(StaticInitBlockTester.someString);
		
		/* uncommet this makes no difference as the classloader has already
		 loaded the class */
		
		// StaticInitBlockTester bl = new StaticInitBlockTester();
		//StaticInitBlockTester bl1 = new StaticInitBlockTester();
		// StaticInitBlockTester bl2 = new StaticInitBlockTester();

	}

	/*
	 * The instance is initialized only once; When the class is loaded from the
	 * class loader the static{} block is executed. Same as static variable.
	 * The trick is that the
	 */

}
