package com.zm.controller;

import com.zm.model.chat.Chat;
import com.zm.model.chat.Message;
import com.zm.model.user.User;
import com.zm.service.ChatService;
import com.zm.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
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
  public List<Chat> queryChat(HttpServletRequest request) {
	List<Chat> chats = null;
	try {
	  User user = (User) request.getSession().getAttribute(Constants.SESSION_USERNAME);
	  if (user == null) {
		return new ArrayList<Chat>();
	  }
	  chats = chatService.queryAllChatByUserId(user.getId());
	} catch (Exception e) {
	  e.printStackTrace();
	}
	return chats;
  }

  @RequestMapping("/save")
  public void save(@RequestBody String msg, HttpServletRequest request) {
//	String ownCode = (String) request.getSession().getAttribute(Constants.WEBSOCKET_USERNAME);
	String[] params = msg.split("&");
	try {
	  Message message = generateMessage(params[0].split("=")[1], params[1].split("=")[1], params[2].split("=")[1], params[3].split("=")[1], params[4].split("=")[1]);
	  this.chatService.save(message);
	} catch (UnsupportedEncodingException e) {
	  e.printStackTrace();
	}
  }

  @RequestMapping("/update/{chatId}/{userId}")
  public void update(@PathVariable String chatId, @PathVariable String userId) {
	this.chatService.update(chatId, userId);
  }

  private Message generateMessage(String chatId, String ownId, String pic, String msg, String type) throws UnsupportedEncodingException {
	String fristDecodeChatset = "ISO8859-1";
	String secondDecodeChatset = "GB2312";
	Message message = new Message();
	message.setChatId(chatId);
	message.setUserId(new String(ownId.getBytes(fristDecodeChatset), secondDecodeChatset));
	message.setPic(new String(pic.getBytes(fristDecodeChatset), secondDecodeChatset));
	message.setContent(new String(msg.getBytes(fristDecodeChatset), secondDecodeChatset));
	message.setType(new String(type.getBytes(fristDecodeChatset), secondDecodeChatset));
	return message;
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
