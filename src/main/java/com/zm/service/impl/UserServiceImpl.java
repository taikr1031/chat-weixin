package com.zm.service.impl;

import com.zm.model.user.User;
import com.zm.mongo.core.GenericMongoServiceImpl;
import com.zm.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service("userService")
public class UserServiceImpl extends GenericMongoServiceImpl<User> implements UserService {

  @Override
  public List<User> getAllUserWithoutOwn(String ownCode) {
	List<User> users = super.getAllObjects();
	for (Iterator<User> iter = users.iterator(); iter.hasNext();) {
	  User user = iter.next();
	  if (user.getCode().equals(ownCode)) {
		iter.remove();
	  }
	}
	return users;
  }
}
