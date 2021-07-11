package com.mongo.conn;

import java.util.ArrayList;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.model.UpdateOptions;

public class FingerCredentials extends Collection {

	public FingerCredentials() {
		super();
	}

	@Override
	public String getCollectionName() {

		return this.getClass().getSimpleName();
	}

	public void insertFingerData(FingerCredential finger) {
		Document doc = new Document().append(FingerCredential.USERID, finger.userid).append(FingerCredential.PRINTDATA,
				finger.printData);
		this.collection.insertOne(doc);
	}

	public boolean updateFingerData(FingerCredential finger) {

		UpdateOptions options = new UpdateOptions();
		options.upsert(true);
		Document findDoc = new Document().append(FingerCredential.USERID, finger.userid);
		Document updateDoc = new Document().append(FingerCredential.PRINTDATA, finger.printData);
		return this.collection.updateMany(findDoc, new Document("$set", updateDoc), options).getModifiedCount() > 0;

	}

	public boolean deleteFingerData(FingerCredential finger) {

		Document delDoc = new Document().append(FingerCredential.USERID, finger.userid);
		return this.collection.deleteMany(delDoc).getDeletedCount() > 0;
	}

	public FingerCredential getFingerData(FingerCredential finger) {

		Document doc = new Document().append(FingerCredential.USERID, finger.getUserdid());
		FingerCredential fingerDb = new FingerCredential();
		this.collection.find(doc).iterator().forEachRemaining(item -> {
			fingerDb.setPrintData(item.getString(FingerCredential.PRINTDATA))
					.setUserId(item.getString(FingerCredential.USERID));
			return;
		});

		return fingerDb;
	}

	public ArrayList<FingerCredential> getAllFingerData() {
		ArrayList<FingerCredential> list = new ArrayList<>();
		this.collection.find().iterator().forEachRemaining(item -> {
			list.add(this.new FingerCredential().setPrintData(item.getString(FingerCredential.PRINTDATA))
					.setUserId(item.getString(FingerCredential.USERID)));
		});
		return list;
	}

	public class FingerCredential {

		static final String USERID = "userid";
		static final String PRINTDATA = "printData";

		private String userid;
		private String printData;

		public String getUserdid() {
			return userid;
		}

		public FingerCredential setUserId(String usedid) {
			this.userid = usedid;
			return this;
		}

		public String getPrintData() {
			return printData;
		}

		public FingerCredential setPrintData(String printData) {
			this.printData = printData;
			return this;
		}

		public JSONObject toJSON() {
			try {
				return new JSONObject().put(USERID, this.userid).put(PRINTDATA, this.printData);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new JSONObject();
		}

	}

}
