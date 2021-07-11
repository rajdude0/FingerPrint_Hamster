package com.mongo.conn;


import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class MongoConn {

	private static MongoConn mongoInstance;
	private MongoClient mongoClient;
	private MongoDatabase database;
	private MongoConn() {
		String dbHost = System.getenv("DB_HOST");
		String dbPW = System.getenv("DB_PW");
		String dbUser = System.getenv("DB_USER");
		
		if(dbHost == null || dbPW == null || dbUser == null ){
		   this.mongoClient = new MongoClient();
		} else {
			MongoClientURI uri = new MongoClientURI("mongodb://"+ dbUser + " : "+ dbPW + "@" + dbHost +":27017/"+ this.getWorkingDatabase());
			this.mongoClient = new MongoClient(uri);
		}
	}
	
	public static MongoConn getInstance(){
		if(mongoInstance==null)
			return mongoInstance = new MongoConn();
		return mongoInstance;
	}
	
	public void setWorkingDatabase(String name){
		this.database = this.mongoClient.getDatabase(name);
	}
	
	public String getWorkingDatabase(){
		return this.database.getName();
	}
	
	public MongoCollection<Document> getCollection(String collection){
		return this.database.getCollection(collection);
	}
	
	public void insertPrint(byte[] data){
		Document doc = new Document();
		String base = Base64.getEncoder().encodeToString(data);
		byte[] newdata = Base64.getDecoder().decode(base);
		for(int i=0; i< data.length;i++){
			System.out.print(data[i]);
		}
		System.out.println();
		for(int i=0; i<newdata.length;i++){
			System.out.print(newdata[i]);
		}
		doc.append("data", Base64.getEncoder().encodeToString(data));
		this.database.getCollection("FingerCollection").insertOne(doc);
	}
	
	public ArrayList<String> getAllPrints(){
		ArrayList<String> list =new ArrayList<>();
		Iterator<Document> dbPrints = this.database.getCollection("FingerCollection").find().iterator();
		dbPrints.forEachRemaining(item->{
			
			list.add(item.getString("data"));
		});
		return list;
	}
}
