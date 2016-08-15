package com.zm.mongo.core;

import com.mongodb.WriteResult;

import java.io.Serializable;
import java.util.List;

public interface GenericMongoService<T extends Serializable> {

  List<T> getAllObjects();

  void saveObject(T object);

  T getObject(String id);

  WriteResult updateObject(String id, String name);

  void deleteObject(String id);

  void createCollection();

  void dropCollection();

  void dropCollectionByClass(Class<T> entityClass);
}
