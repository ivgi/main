package com.test;

//implemented in InterfaceImpl class
interface InterfaceWithStaticClass {
	A a = new A();
	class A{
		A(){
			System.out.println("Hello from interface");
		}
	}

}
