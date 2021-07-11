package com.fingerprint.api;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class FingerServer {
	
	private int PORT = 9909;
	private HttpServer instanceServer;
	
	public FingerServer(){
		try {
			this.instanceServer = HttpServer.create(new InetSocketAddress(PORT),0);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Port Already Used!!");
		}
		this.addRoutes("/",new RootHandler());
		this.addRoutes("/register", new FingerRegisterHandler());
		this.addRoutes("/verify", new FingerVerifyHandler());
		
	}
	
	public void startServer(){
		this.instanceServer.start();
	}
	
	public void addRoutes(String routeName, HTTPHandler route){
		instanceServer.createContext(routeName, route);
	}

}
