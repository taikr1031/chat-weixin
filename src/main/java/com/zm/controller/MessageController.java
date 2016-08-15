package com.zm.controller;

import com.zm.model.chat.Message;
import com.zm.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/message")
public class MessageController {

  @Autowired
  private MessageService messageService;

  @RequestMapping("/queryMessage/{chatId}")
  public List<Message> queryMessage(@PathVariable String chatId) {
    return messageService.queryMessage(chatId);
  }

  @RequestMapping("/save")
  public void save(@RequestBody String msg, HttpServletRequest request) {
    String[] params = msg.split("&");
    try {
      Message message = generateMessage(params[0].split("=")[1], params[1].split("=")[1], params[2].split("=")[1], params[3].split("=")[1], params[4].split("=")[1]);
      this.messageService.save(message);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
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
    message.setRead(false);
    message.setTime(new Timestamp(System.currentTimeMillis()));
    return message;
  }

}
