package com.mongo.conn;

import com.mongo.conn.FingerCredentials;

public class Demo {

	public static void main(String args[]){
		FingerCredentials credentials = new FingerCredentials();
		
		credentials.insertFingerData(credentials.new FingerCredential().setPrintData("ldjflajd").setUserId("1"));
		credentials.insertFingerData(credentials.new FingerCredential().setPrintData("ldjflajd").setUserId("2"));
		credentials.updateFingerData(credentials.new FingerCredential().setPrintData("asdfasdf").setUserId("1"));
		System.out.println(credentials.getFingerData(credentials.new FingerCredential().setUserId("1")).toJSON());
		//credentials.deleteFingerData(credentials.new FingerCredential().setUserId("1"));
		credentials.getAllFingerData().forEach(item->{
			System.out.println(item.toJSON());
		});
	}
}
