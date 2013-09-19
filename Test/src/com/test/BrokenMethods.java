package com.test;

public class BrokenMethods {
	
	
	 private Object statusThree;
	private Object statusFour;
	private Object statusFive;
	private Object status;
	 Object statusTwo;
	public String colorCheck(Integer ticket) {
		
		  String color="green";
		  switch (ticket) {
		  case 1:
			
			if (statusTwo.equals("Reserved")) {
		    color= "red";
		    return color;
		   } else if (statusTwo.equals("Owned")) {
		    color= "blue";
		    return color;
		   } else {
		    color= "green";
		    return color;
		   }
		  case 2:
		   if (statusThree.equals("Reserved")) {
		    color= "red";
		    return color;
		   } else if (statusThree.equals("Owned")) {
		    color= "blue";
		    return color;
		   } else {
		    color= "green";
		    return color;
		   }
		  case 3:
		   if (statusFour.equals("Reserved")) {
		    color= "red";
		    return color;
		   } else if (statusFour.equals("Owned")) {
		    color= "blue";
		    return color;
		   } else {
		    color= "green";
		    return color;
		   }
		  case 4:
		   if (statusFive.equals("Reserved")) {
		    color= "red";
		    return color;
		   } else if (statusFive.equals("Owned")) {
		    color= "blue";
		    return color;
		   } else {
		    color= "green";
		    return color;
		   }
		  case 0:
		   if (status.equals("Reserved")) {
		    color= "red";
		    return color;
		   } else if (status.equals("Owned")) {
		    color= "blue";
		    return color;
		   } else {
		    color= "green";
		    return color;
		   }
		  default:
		   break;
		  }
		return "Error";
		 }

}
