package com.fingerprint.main;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.imageio.ImageIO;

import com.fingerprint.lib.FingerLib;
import com.fingerprint.lib.Int;
import com.mongo.conn.MongoConn;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import SecuGen.FDxSDKPro.jni.JSGFPLib;
import SecuGen.FDxSDKPro.jni.SGDeviceInfoParam;
import SecuGen.FDxSDKPro.jni.SGDeviceList;
import SecuGen.FDxSDKPro.jni.SGFDxDeviceName;
import SecuGen.FDxSDKPro.jni.SGFDxErrorCode;
import SecuGen.FDxSDKPro.jni.SGFDxSecurityLevel;
import SecuGen.FDxSDKPro.jni.SGFingerInfo;
import SecuGen.FDxSDKPro.jni.SGFingerPosition;
import SecuGen.FDxSDKPro.jni.SGImpressionType;


public class FingerMain {

	/*
	public static void main(String args[]){
		JSGFPLib obj = new JSGFPLib();
		long error = obj.Init( SGFDxDeviceName.SG_DEV_AUTO);
		long error2 = obj.OpenDevice(SGFDxDeviceName.SG_DEV_AUTO);
		
		obj.SetLedOn(true);
		int imageHeight = 0;
		int imageWidth = 0;
		SGDeviceInfoParam deviceInfo = new SGDeviceInfoParam();
		if(obj.GetDeviceInfo(deviceInfo)== SGFDxErrorCode.SGFDX_ERROR_NONE){
			imageHeight = deviceInfo.imageHeight;
			imageWidth = deviceInfo.imageWidth;
			System.out.println(imageHeight +" ,"+ imageWidth);
		}
		byte[] data = new byte[imageHeight * imageWidth];
		if (obj.GetImage(data) == SGFDxErrorCode.SGFDX_ERROR_NONE) {
			System.out.println("Capturing file..");
			int[] mg_qlty = new int[1]; // this should be initialized with 1 count;
			long imgError = obj.GetImageQuality(imageWidth, imageHeight, data, mg_qlty);
			System.out.println(imgError);
			if(imgError == SGFDxErrorCode.SGFDX_ERROR_NONE){
				System.out.println(mg_qlty[0]);
				//int[] templateSize = new int[1];
				Int templateSize = new Int();
				obj.GetMaxTemplateSize(templateSize.getRaw());
				byte[] templatebuffer = new byte[templateSize.getValue()];
				SGFingerInfo fingerInfo = new SGFingerInfo();
				fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
				fingerInfo.ImageQuality = mg_qlty[0];
				fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
				fingerInfo.ViewNumber = 1;
				if(obj.CreateTemplate(fingerInfo, data, templatebuffer)== SGFDxErrorCode.SGFDX_ERROR_NONE){
					System.out.println("Template Created");
				}
				byte[] data1 = new byte[imageHeight * imageWidth];
				obj.GetImage(data1);
				byte[] templatebuffer1 = new byte[templateSize.getValue()];
				obj.CreateTemplate(fingerInfo, data, templatebuffer1);
				boolean[] matched = new boolean[1];
				obj.MatchTemplate(templatebuffer, templatebuffer1, SGFDxSecurityLevel.SL_NORMAL, matched);
				if(matched[0]){
					System.out.println("Finger Print Matched");
				}
			}
			
			File f = new File("D:\\Output.jpg");
			if(!f.exists()){
				try {
					f.createNewFile();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			try {
				ImageIO.getImageReaders(new ByteArrayInputStream(data)).forEachRemaining(item->{
					try {
						System.out.println(item.getFormatName());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});;
				
				//BufferedImage image = ImageIO.read();
				//if(image == null){
					System.out.println("image is null");
				//}
				//ImageIO.write(image, "jpg", f);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try{
		Thread.sleep(10000);
		obj.SetLedOn(false);
		}catch(Exception ie){
			ie.printStackTrace();
		}
	}
	*/
	
	public static void main(String args[]) throws Exception{
		MongoConn conn = MongoConn.getInstance();
		FingerLib lib = FingerLib.getInstance();
		lib.openDevice();
		lib.ledOn();
		byte[] data1 = lib.getImageContinous();
		System.out.println(lib.getImageQuality(data1));
		byte[] temp1 = lib.getTemplate(data1);
		byte[] data  = lib.getImageContinous();
		System.out.println(lib.getImageQuality(data));
		byte[] temp2 = lib.getTemplate(data);
		System.out.println("Storing data...");
		conn.insertPrint(temp2);
		System.out.println(lib.match(temp1, temp2));
		
		Thread.sleep(10000);
		byte[] temp3 = lib.getTemplate(lib.getImageContinous());
		System.out.println("Verifying the data");
		Decoder decoder = Base64.getDecoder();
		conn.getAllPrints().forEach(item->{
			System.out.println("Decoding");
			byte[] temp = decoder.decode(item);
			System.out.println(temp.length==temp2.length);
			if(lib.match(temp3, temp)){
				System.out.println("Data Matched");
				return;
			}
			
		});
		System.out.println("Not match found!");
		
	}
	
	
}
