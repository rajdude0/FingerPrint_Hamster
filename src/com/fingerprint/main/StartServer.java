package com.fingerprint.main;

import com.fingerprint.api.FingerServer;
import com.mongo.conn.MongoConn;

public class StartServer {

	public static void main(String[] args){
		MongoConn.getInstance();
		FingerServer server = new FingerServer();
		server.startServer();
		
	}
}
