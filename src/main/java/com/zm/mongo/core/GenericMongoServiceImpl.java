package com.zm.mongo.core;

import com.mongodb.WriteResult;
import com.zm.util.DefectHelper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Service("genericMongoService")
public class GenericMongoServiceImpl<T extends Serializable> implements GenericMongoService<T> {

  private Class<T> entityClass;

  @Resource
  private MongoTemplate mongoTemplate;

  public GenericMongoServiceImpl() {
//	entityClass = DefectHelper.getSuperClassGenericType(getClass());
	entityClass = getSuperClassGenricType(getClass(), 0);
  }

  public static Class getSuperClassGenricType(Class clazz, int index) {

	Type genType = clazz.getGenericSuperclass();// 得到泛型父类

	// 如果没有实现ParameterizedType接口，即不支持泛型，直接返回Object.class
	if (!(genType instanceof ParameterizedType)) {

	  return Object.class;
	}

	// 返回表示此类型实际类型参数的Type对象的数组,数组里放的都是对应类型的Class, 如BuyerServiceBean extends
	// DaoSupport<Buyer,Contact>就返回Buyer和Contact类型
	Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
	if (index >= params.length || index < 0) {

	  throw new RuntimeException("你输入的索引"
			  + (index < 0 ? "不能小于0" : "超出了参数的总数"));
	}
	if (!(params[index] instanceof Class)) {

	  return Object.class;
	}
	return (Class) params[index];
  }

  @Override
  public List<T> getAllObjects() {
	return mongoTemplate.findAll(entityClass);
  }

  @Override
  public void saveObject(T tree) {
	mongoTemplate.insert(tree);
  }

  @Override
  public T getObject(String id) {
	return mongoTemplate.findOne(new Query(Criteria.where("id").is(id)), entityClass);
  }

  @Override
  public WriteResult updateObject(String id, String name) {
	return mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), Update.update("name", name), entityClass);
  }

  @Override
  public void deleteObject(String id) {
	mongoTemplate.remove(new Query(Criteria.where("id").is(id)), entityClass);
  }

  @Override
  public void createCollection() {
	if (!mongoTemplate.collectionExists(entityClass)) {
	  mongoTemplate.createCollection(entityClass);
	}
  }

  @Override
  public void dropCollection() {
	if (mongoTemplate.collectionExists(entityClass)) {
	  mongoTemplate.dropCollection(entityClass);
	}
  }

  @Override
  public void dropCollectionByClass(Class<T> entityClass) {
	mongoTemplate.dropCollection(entityClass);
  }

  public MongoTemplate getMongoTemplate() {
	return mongoTemplate;
  }
}
