package com.mongo.conn;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

abstract public class Collection {

	private MongoConn mongoConn;
	protected MongoCollection<Document> collection;
	public Collection(){
		this.mongoConn = MongoConn.getInstance();
		this.mongoConn.setWorkingDatabase(System.getenv("DB_COLL_NAME"));
		this.collection = mongoConn.getCollection(this.getCollectionName());
	}
	
	public abstract String getCollectionName();
	
	
}
