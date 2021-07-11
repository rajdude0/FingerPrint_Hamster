package com.fingerprint.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.fingerprint.lib.FingerConn;
import com.mongo.conn.FingerCredentials;
import com.sun.net.httpserver.HttpExchange;

public class FingerRegisterHandler extends FingerConn implements HTTPHandler {

	Map<String, Object> parameters;

	public FingerRegisterHandler() {
		super();
	}

	@Override
	public void handle(HttpExchange he) throws IOException {
		if (!he.getRequestMethod().equals("POST")) {
			sendResponse("Method not allowed!", 405, he);
			return;
		}
		parameters = new HashMap<String, Object>();
		InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
		BufferedReader br = new BufferedReader(isr);
		String query = br.readLine();
		HTTPHandler.parseQuery(query, parameters);
		parameters.forEach((key, value) -> {
			System.out.println(key);
			System.out.println(value);
		});
		if (!parameters.containsKey("userid")) {

			sendResponse("Bad Request", 400, he);
			return;
		}

		if (this.registerFingerPrint()) {
			sendResponse("FingerPrint Registered !", 200, he);
			return;
		}

		sendResponse("Something went wrong", 500, he);

	}

	private boolean registerFingerPrint() {
		try {
			fingerDevice.openDevice();
			fingerDevice.ledOn();
			byte[] temp1 = fingerDevice.getTemplate(fingerDevice.getImageContinous());
			byte[] temp2 = fingerDevice.getTemplate(fingerDevice.getImageContinous());

			if (fingerDevice.match(temp1, temp2)) {
				FingerCredentials credentials = new FingerCredentials();
				credentials.insertFingerData(credentials.new FingerCredential()
						.setUserId((String) parameters.get("userid")).setPrintData(encodeToBase64(temp2)));
				fingerDevice.ledOff();
				fingerDevice.closeDevice();

				return true;

			}
		} catch (Exception e) {
			e.printStackTrace();
			fingerDevice.ledOff();
			return false;
		}
		fingerDevice.ledOff();
		fingerDevice.closeDevice();
		return false;

	}

}
