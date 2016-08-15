package com.zm.service;

import com.zm.model.chat.Chat;
import com.zm.model.chat.Message;
import com.zm.mongo.core.GenericMongoService;

import java.util.List;

public interface ChatService extends GenericMongoService<Chat> {

  List<Chat> queryChat(String ownCode);

  Chat getChatByFriend(String ownId, String friendId);

  void sendMessage(String ownId, String friendId, String message);

  void save(Message message);

  void update(String chatId, String userId);

  int getTotalNoReadMsgNum(List<Chat> chats);
}
