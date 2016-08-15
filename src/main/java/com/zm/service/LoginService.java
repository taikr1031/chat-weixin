package com.zm.service;

import com.zm.model.user.User;
import com.zm.mongo.core.GenericMongoService;

public interface LoginService extends GenericMongoService<User>{

  User login(String username, String password);

  void logout(String username);
}
