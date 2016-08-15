package com.zm.service;

import com.zm.model.chat.Message;

import java.util.List;

public interface MessageService {

  List<Message> queryMessage(String chatId);

  void save(Message message);
}
