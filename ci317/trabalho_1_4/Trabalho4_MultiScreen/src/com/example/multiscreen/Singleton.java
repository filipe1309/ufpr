package com.example.multiscreen;

public class Singleton {
	
	 private static Singleton mInstance = null;
	 
	 private Integer value;
	 private Integer value2;
	 private Integer contador;
	 private Integer aux;
	  
	 private Singleton()
	 {
		 
		 value = 2;
		 value2 = 1;
		 contador = 1;
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
		 if (contador == 1){
			 contador++;
			 return this.value.toString();
		 }
		 else if (contador == 2){
			 contador++;
			 return this.value2.toString();
		 }
		 else{
			 aux = value2;
			 value2 = value +value2;
			 value = aux;
			 contador++;
			 return this.value2.toString();
		 }
	 }
	  
	 
}
