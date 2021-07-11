package com.fingerprint.lib;



import SecuGen.FDxSDKPro.jni.JSGFPLib;
import SecuGen.FDxSDKPro.jni.SGFDxDeviceName;
import SecuGen.FDxSDKPro.jni.SGFDxErrorCode;
import SecuGen.FDxSDKPro.jni.SGFDxSecurityLevel;

public class FingerLib {

	long NOEROR = SGFDxErrorCode.SGFDX_ERROR_NONE;
	
	long AUTODETECT = SGFDxDeviceName.SG_DEV_AUTO;
	long HU20 = SGFDxDeviceName.SG_DEV_FDU05;
	long usedMethod;
	private boolean autoDetectModel = true;
	private long timeOut = 10*1000;
	private long imageQuality = 80;

	private static FingerLib instance;
	private FingerInfo fingerInfo;
	JSGFPLib fingerLib;
	private long SecurityLevel = SGFDxSecurityLevel.SL_NORMAL;

	private FingerLib() {
		fingerLib = new JSGFPLib();
		usedMethod = autoDetectModel ? AUTODETECT : HU20;
		fingerInfo = new FingerInfo(this);
		this.init();
	}

	public static FingerLib getInstance() {
		if (instance == null)
			return instance = new FingerLib();
		return instance;
	}
	
	public void setTimeOut(long timeout){
		this.timeOut = timeout;
	}

	public void requiredFingerQuality(long quality){
		if(quality < 0 || quality > 100 ){
			System.out.println("Quality should be in range of 0 to 100");
			return;
		}
		this.imageQuality = quality;
	}
	
	public boolean init() {

		long error = this.fingerLib.Init(usedMethod);

		if (error == NOEROR) {
			System.out.println("Device Initialized!");
			return true;
		}
		return false;

	}

	public void ledOn() {
		this.fingerLib.SetLedOn(true);
	}

	public void ledOff() {
		this.fingerLib.SetLedOn(false);
	}

	public void openDevice() {
		long error = this.fingerLib.OpenDevice(usedMethod);
		if (error == NOEROR) {
			try {
				fingerInfo.gatherDeviceInfo();
			} catch (Exception ie) {
				ie.printStackTrace();
			}
		}
	}

	public void stop() {
		this.fingerLib.Close();
	}

	public void closeDevice() {
		this.fingerLib.CloseDevice();
	}

	public byte[] getImage() throws Exception {
		byte[] data = new byte[fingerInfo.PIXELS];
		long error = this.fingerLib.GetImage(data);
		if (error != NOEROR) {
			
			throw new Exception("Device not opened!!");
		}
		return data;
	}
	
	public byte[] getImageContinous() throws Exception{
		byte[] data = new byte[fingerInfo.PIXELS];
		long error = this.fingerLib.GetImageEx(data, this.timeOut, 0L, this.imageQuality);
		if(error != NOEROR){
			System.out.println(error);
			throw new Exception("Device not opened!!");
		}
		return data;
	}
	

	public int getImageQuality(byte[] data) {
		Int imageQuality = new Int();
		this.fingerLib.GetImageQuality(this.fingerInfo.WIDTH, this.fingerInfo.HEIGHT, data, imageQuality.getRaw());
		return imageQuality.getValue();
	}

	public byte[] getTemplate(byte[] data) {
		byte[] template = new byte[this.fingerInfo.getMaxTemplateSize()];
		int imageQuality = this.getImageQuality(data);
		this.fingerLib.CreateTemplate(this.fingerInfo.generateSGFingerInfo(imageQuality), data, template);
		return template;
	}

	public void setSecurityLeve(int value) {

		switch (value) {
		case 4:
			this.SecurityLevel = SGFDxSecurityLevel.SL_NORMAL;
			break;
		case 3:
			this.SecurityLevel = SGFDxSecurityLevel.SL_HIGH;
			break;
		case 2:
			this.SecurityLevel = SGFDxSecurityLevel.SL_HIGHER;
			break;
		case 1:
			this.SecurityLevel = SGFDxSecurityLevel.SL_HIGHEST;
			break;
		}
	}

	public boolean match(byte[] template1, byte[] template2) {
		Bool matched = new Bool();
		this.fingerLib.MatchTemplate(template1, template2, this.SecurityLevel, matched.getRaw());
		return matched.getValue();
	}

}
