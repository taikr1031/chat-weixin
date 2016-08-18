package com.zm.controller;

import com.zm.model.user.User;
import com.zm.service.UserService;
import com.zm.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @RequestMapping("/getAllUser")
  public List<User> getAllUser() {
	return userService.getAllObjects();
  }

  @RequestMapping("/getUserById/{userId}")
  public User getUserById(@PathVariable String userId) {
	return userService.getObject(userId);
  }

  @RequestMapping("/getAllUserWithoutOwn")
  public List<User> getAllUserWithoutOwn(HttpServletRequest request) {
    String ownOpenid= ((User)request.getSession().getAttribute(Constants.SESSION_USERNAME)).getCode();
	return userService.getAllUserWithoutOwn(ownOpenid);
  }

  @RequestMapping("/getUserId")
  public List<String> getUserId(HttpServletRequest request) {
    if(request.getSession().getAttribute(Constants.SESSION_USERNAME) != null) {
      String userId = ((User) request.getSession().getAttribute(Constants.SESSION_USERNAME)).getId();
      List<String> list = new ArrayList<String>();
      list.add(userId);
      return list;
    }
    return null;
  }
}
