package com.zm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

  @RequestMapping(value = {"", "/home"}, method = RequestMethod.GET)
  public String home(HttpServletRequest httpRequest) {
	return "index";
  }

}
