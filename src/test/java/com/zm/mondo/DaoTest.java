package com.zm.mondo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.zm.model.chat.Chat;
import com.zm.model.chat.Message;
//import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

public class DaoTest {

//  @Test
  public void testQuery() {
	Query query = new Query();
	query.addCriteria(Criteria.where("id").is("1-2").and("messages.userId").is("1").and("messages.read").exists(true));
	int num = (int) getMongoTemplate().count(query, Chat.class);
	System.out.println(num);
	Assert.notNull(num);
  }

//  @Test
  public void testUpdate() {
	String chatId = "1-2";
	String userId = "1";
	Query query = new Query();
	query.addCriteria(Criteria.where("id").is(chatId).and("messages.userId").is(userId).and("messages.read").is(true));
	long count = getMongoTemplate().count(query, Chat.class);
	System.out.println(count);
//	Update update = new Update().set("messages.$.read", false).set("messages.$.content", "content1");
	Update update = Update.update("messages.$.read", false).set("messages.$.content", "content1");
	WriteResult writeResult = this.getMongoTemplate().updateMulti(query, update, Chat.class);
	System.out.print(writeResult);
  }

  //  @Test
  public void testUpdateByDriver() {
	MongoClient client = null;
	try {
	  client = new MongoClient("127.0.0.1", 27017);
	  DBCollection dbCollection = client.getDB("chatdb").getCollection("demo");
	  dbCollection.drop();
	  dbCollection = client.getDB("chatdb").getCollection("demo");
	  BasicDBObject dbObject = new BasicDBObject();
	  dbObject.append("id", 583739819102582565L).append("kids", Arrays.asList("1", "2", "3"));
	  dbCollection.insert(dbObject);
	  System.out.println(dbCollection.findOne());
	  BasicDBObject dbObject2 = new BasicDBObject(dbObject);
	  dbObject2.append("kids", Arrays.asList("1", "2", "3", "4"));
	  dbCollection.update(dbObject, dbObject2);
	  System.out.println(dbCollection.findOne());
	} catch (UnknownHostException e) {
	  e.printStackTrace();
	}
  }

//  @Test
  public void testGetNoReadMsgNum() {
	String chatId = "1-2";
	String userId = "1";
	Query query = new Query();
	query.addCriteria(Criteria.where("id").is(chatId).and("messages.userId").is(userId).and("messages.read").is(false));
//	int num = getMongoTemplate().find(query, Chat.class).size();
	int num = (int) getMongoTemplate().count(query, Chat.class);
	System.out.println(num);
  }

  public MongoTemplate getMongoTemplate() {
	String configLocations = "spring-mongodb.xml";
	ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configLocations);
	MongoTemplate mongoTemplate = applicationContext.getBean(MongoTemplate.class);
	return mongoTemplate;
  }
}
