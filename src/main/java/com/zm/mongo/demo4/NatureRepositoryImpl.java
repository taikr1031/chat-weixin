package com.zm.mongo.demo4;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;

public class NatureRepositoryImpl implements Repository<Person> {
  @Resource
  MongoTemplate mongoTemplate;

  @Override
  public List<Person> getAllObjects() {
	return mongoTemplate.findAll(Person.class);
  }

  @Override
  public void saveObject(Person tree) {
	mongoTemplate.insert(tree);
  }

  @Override
  public Person getObject(String id) {
	return mongoTemplate.findOne(new Query(Criteria.where("id").is(id)), Person.class);
  }

  @Override
  public WriteResult updateObject(String id, String name) {
	return mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), Update.update("name", name), Person.class);
  }

  @Override
  public void deleteObject(String id) {
	mongoTemplate.remove(new Query(Criteria.where("id").is(id)), Person.class);
  }

  @Override
  public void createCollection() {
	if (!mongoTemplate.collectionExists(Person.class)) {
	  mongoTemplate.createCollection(Person.class);
	}
  }

  @Override
  public void dropCollection() {
	if (mongoTemplate.collectionExists(Person.class)) {
	  mongoTemplate.dropCollection(Person.class);
	}
  }
}