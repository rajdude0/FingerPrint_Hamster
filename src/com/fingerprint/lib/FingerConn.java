package com.fingerprint.lib;

import java.util.Base64;


public class FingerConn {

	protected FingerLib fingerDevice;
	
	protected FingerConn(){
		this.fingerDevice = FingerLib.getInstance();
		
	}
	
	public String encodeToBase64(byte[] data){
	
		return Base64.getEncoder().encodeToString(data);
	}
	
	public byte[] decodeFromBase64(String data){
		
		return Base64.getDecoder().decode(data);
	}
}
