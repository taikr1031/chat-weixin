package com.zm.controller;


import com.zm.model.chat.Message;
import com.zm.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
