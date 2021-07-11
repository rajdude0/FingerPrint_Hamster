package com.fingerprint.lib;

import SecuGen.FDxSDKPro.jni.SGDeviceInfoParam;
import SecuGen.FDxSDKPro.jni.SGFDxErrorCode;
import SecuGen.FDxSDKPro.jni.SGFingerInfo;
import SecuGen.FDxSDKPro.jni.SGFingerPosition;
import SecuGen.FDxSDKPro.jni.SGImpressionType;

public class FingerInfo {

	public int HEIGHT = 0;
	public int WIDTH = 0;
	public int PIXELS = 0;
	private FingerLib fingerLib;

	public FingerInfo(FingerLib fingerLib) {
		this.fingerLib = fingerLib;

	}

	public void gatherDeviceInfo() throws Exception {
		SGDeviceInfoParam deviceInfo = new SGDeviceInfoParam();
		if (fingerLib.fingerLib.GetDeviceInfo(deviceInfo) == SGFDxErrorCode.SGFDX_ERROR_NONE) {
			HEIGHT = deviceInfo.imageHeight;
			WIDTH = deviceInfo.imageWidth;
			PIXELS = HEIGHT * WIDTH;
		} else {
			throw new Exception("Device Not Open");
		}
	}

	public int getMaxTemplateSize() {
		Int templateSize = new Int();
		this.fingerLib.fingerLib.GetMaxTemplateSize(templateSize.getRaw());
		return templateSize.getValue();
	}

	public SGFingerInfo generateSGFingerInfo(int imgQuality) {
		SGFingerInfo fingerInfo = new SGFingerInfo();
		fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
		fingerInfo.ImageQuality = imgQuality;
		fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
		fingerInfo.ViewNumber = 1;
		return fingerInfo;
	}

}
