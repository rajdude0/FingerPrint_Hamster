package com.fingerprint.api;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;


public class RootHandler implements HTTPHandler {

	@Override
	public void handle(HttpExchange he) throws IOException {
		// TODO Auto-generated method stub
		 String response = "<h1>Server start success "+
                 "if you see this message</h1>" + "<h1>Port: " + 9909 + "</h1>";
                 he.sendResponseHeaders(200, response.length());
                 OutputStream os = he.getResponseBody();
                 os.write(response.getBytes());
                 os.close();
	}

	

}
