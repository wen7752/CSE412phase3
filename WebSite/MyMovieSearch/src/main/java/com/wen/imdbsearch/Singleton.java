package com.wen.imdbsearch;

public class Singleton {

	public String username;
	public String pwd;
	public String connURL;
	public String jdbcDriver;
	
	 private static Singleton single_instance = null;
	 
	 private Singleton() {
		
		 	username="postgres";
			pwd="postgres";
			connURL = "jdbc:postgresql://192.168.99.100:5432/imdbsearch";
			jdbcDriver = "org.postgresql.Driver";
			
	}
	 
	 public static Singleton getInstance()
	    {
	        if (single_instance == null)
	            single_instance = new Singleton();
	 
	        return single_instance;
	    }
	 
}
