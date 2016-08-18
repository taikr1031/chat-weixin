package com.zm.controller;

import com.zm.model.chat.Chat;
import com.zm.model.user.User;
import com.zm.service.ChatService;
import com.zm.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("chat")
public class ChatController {

  private String msg;
  private String openid;

  @Autowired
  private ChatService chatService;

  @RequestMapping("/queryChat")
  public List<Chat> queryChat(HttpServletRequest request, Model model) {
	List<Chat> chats = null;
	try {
	  User user = (User) request.getSession().getAttribute(Constants.SESSION_USERNAME);
	  if (user == null) {
		return new ArrayList<Chat>();
	  }
	  chats = chatService.queryChat(user.getId());
	} catch (Exception e) {
	  e.printStackTrace();
	}
	return chats;
  }

  @RequestMapping("/update/{chatId}/{userId}")
  public void update(@PathVariable String chatId, @PathVariable String userId) {
	this.chatService.update(chatId, userId);
  }

  public String getOpenid() {
	return openid;
  }

  public void setOpenid(String openid) {
	this.openid = openid;
  }

  public String getMsg() {
	return msg;
  }

  public void setMsg(String msg) {
	this.msg = msg;
  }
}
