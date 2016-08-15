package com.zm.controller;

import com.zm.model.user.User;
import com.zm.repository.ParticipantRepository;
import com.zm.service.LoginService;
import com.zm.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {

  @Autowired
  private ParticipantRepository participantRepository;

  @Autowired
  private LoginService loginService;

  @RequestMapping("/login/{username}/{password}")
  public User login(@PathVariable("username") String username, @PathVariable("password") String password, HttpServletRequest httpRequest) throws ServletException {
	User user = loginService.login(username, password);
	if(user.getName() != null) {
	  httpRequest.getSession().setAttribute(Constants.SESSION_USERNAME, user);
	  participantRepository.add(user.getCode(), user);
	  return user;
	}
	return new User();
  }

  @RequestMapping("/isLogin/{code}")
  public boolean isLoginSession(@PathVariable("code") String code, HttpServletRequest httpRequest) throws ServletException {
	if (httpRequest.getSession().getAttribute(Constants.SESSION_USERNAME) == null) {
	  return false;
	}
	if (!((User) httpRequest.getSession().getAttribute(Constants.SESSION_USERNAME)).getCode().equals(code)) {
	  return false;
	}
	return true;
  }

}
