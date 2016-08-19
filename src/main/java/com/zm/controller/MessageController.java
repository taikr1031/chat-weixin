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
  public void save(@RequestBody Message message, HttpServletRequest request) {
    try {
      this.messageService.save(message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
