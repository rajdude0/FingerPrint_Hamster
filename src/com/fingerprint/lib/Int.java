package com.fingerprint.lib;

public class Int {

	
	int[] integer;
	
	public Int(){
		integer = new int[1];
	}
	
	public int[] getRaw(){
		return integer;
	}
	
	public int getValue(){
		return integer[0];
	}
	
	public void setRaw(int []a){
		integer = a;
	}
	
}
