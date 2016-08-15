package com.zm.service;

import com.zm.model.user.User;
import com.zm.mongo.core.GenericMongoService;

import java.util.List;

public interface UserService extends GenericMongoService<User> {

  List<User> getAllUserWithoutOwn(String ownCode);
}
