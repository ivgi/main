package com.test.statict;

public class SingletonPatternDemo{

	public static void main(String[] args) {

		// get a reference to the static instance
		SingletonPatternTester firstLevelInstance = SingletonPatternTester
				.getInstance();
		firstLevelInstance.showMessage();

		// get a reference to the static instance of the static instance
		SingletonPatternTester secondLevelInstance = firstLevelInstance.getInstance();
		secondLevelInstance.showMessage();

		// They are the same object, because they are static fields of type
		// SingletonPatternTester
	}

}
