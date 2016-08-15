package com.zm.service.impl;

import com.zm.model.user.User;
import com.zm.mongo.core.GenericMongoServiceImpl;
import com.zm.service.LoginService;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("loginService")
public class LoginServiceImpl extends GenericMongoServiceImpl<User> implements LoginService {

  @Override
  public User login(String username, String password) {
	if (username.isEmpty() || password.isEmpty()) {
	  return new User();
	}
	Query query = new Query();
	query.addCriteria(Criteria.where("name").is(username).and("password").is(password));
	query.with(new Sort(new Sort.Order(Direction.DESC, "time")).and(new Sort(Direction.DESC, "openid")));
	List<User> users = getMongoTemplate().find(query, User.class);
	if (users != null && users.size() > 0) {
	  return users.get(0);
	}
	return new User();
  }

  @Override
  public void logout(String username) {

  }
}
