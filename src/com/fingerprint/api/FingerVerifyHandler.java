package com.fingerprint.api;

import java.io.IOException;



import org.json.JSONException;
import org.json.JSONObject;

import com.fingerprint.lib.FingerConn;
import com.mongo.conn.FingerCredentials;
import com.sun.net.httpserver.HttpExchange;

public class FingerVerifyHandler extends FingerConn implements HTTPHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		// TODO Auto-generated method stub
		if (!exchange.getRequestMethod().equals("POST")) {
			sendResponse("Method not allowed!", 405, exchange);
			return;
		}
		System.out.println("Request to verify");
		
		try{
		JSONObject data = verifyFingerPrint();
		if(data!=null){
			sendResponse(data.toString(), 200, exchange);
			return;
		} else {
			
			sendResponse("FingerPrint not Found!", 404, exchange);
			
		}
		}
		catch(Exception e){
		 e.printStackTrace();
		sendResponse("Something went wrong!", 500, exchange);
		}
		
	}

	private JSONObject verifyFingerPrint() throws Exception {
		JSONObject obj = new JSONObject();
		FingerCredentials credentials = new FingerCredentials();
		fingerDevice.openDevice();
		fingerDevice.ledOn();
		try {
			byte[] deviceData = fingerDevice.getTemplate(fingerDevice.getImageContinous());
			
			
			credentials.getAllFingerData().stream()
					.filter(item -> fingerDevice.match(deviceData, decodeFromBase64(item.getPrintData())))
					.limit(1)
					.forEach(item -> {
						try {
							obj.put("userid", item.getUserdid());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
			if(obj.has("userid")){
				return obj;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			fingerDevice.closeDevice();
			fingerDevice.ledOff();
			throw e;
		}

		return null;
	}

}
