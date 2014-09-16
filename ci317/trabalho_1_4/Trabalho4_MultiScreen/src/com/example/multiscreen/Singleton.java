package com.example.multiscreen;

public class Singleton {
	
	 private static Singleton mInstance = null;
	 
	 private Integer value;
	 private Integer value2;
	 private Integer counter;
	 private Integer aux;
	  
	 private Singleton()
	 {
		 
		 value = 2;
		 value2 = 1;
		 counter = 1;
		 aux = 0;
	 }
	  
	 public static Singleton getInstance()
	 {
		 if(mInstance == null)
		 {
			 mInstance = new Singleton();
		 }
		 return mInstance;
	 }
	  
	 public String getNextLucasNumber()
	 {
		 if (counter == 1){
			 counter++;
			 return this.value.toString();
		 }
		 else if (counter == 2){
			 counter++;
			 return this.value2.toString();
		 }
		 else{
			 aux = value2;
			 value2 = value +value2;
			 value = aux;
			 counter++;
			 return this.value2.toString();
		 }
	 }
	  
	 
}
