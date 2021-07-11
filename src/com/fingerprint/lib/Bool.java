package com.fingerprint.lib;

public class Bool {

	boolean[] bool;
	
	public Bool(){
		bool = new boolean[1];
	}
	
	public boolean[] getRaw(){
		return bool;
	}
	
	public boolean getValue(){
		return bool[0];
	}
	
	public void setRaw(boolean []b){
		bool = b;
	}
}
