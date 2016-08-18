package com.zm.service.impl;

import com.zm.model.chat.Chat;
import com.zm.model.chat.Message;
import com.zm.mongo.core.GenericMongoServiceImpl;
import com.zm.service.ChatService;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("chatService")
public class ChatServiceImpl extends GenericMongoServiceImpl<Chat> implements ChatService {

  @Override
  public List<Chat> queryChat(String userId) {
	Query query = new Query();
	Criteria c = new Criteria().orOperator(Criteria.where("auserId").is(userId), Criteria.where("buserId").is(userId));
	query.addCriteria(c);
	query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "originalTime")));
	List<Chat> chats = getMongoTemplate().find(query, Chat.class);
	for (Chat chat : chats) {
	  int num = getNoReadMsgNum(chat.getId(), getOtherUserId(chat.getId(), userId));
	  chat.setNoReadMsgNum(num);
	}
	return chats;
  }

  private String getOtherUserId(String chatId, String userId) {
	return chatId.split("-")[0].equals(userId) ? chatId.split("-")[1] : chatId.split("-")[0];
  }

  public int getNoReadMsgNum(String chatId, String userId) {
	Query query = new Query();
	query.addCriteria(Criteria.where("chatId").is(chatId).and("userId").is(userId).and("read").is(false));
	int num = (int) getMongoTemplate().count(query, Message.class);
	System.out.println(chatId + "-" + userId + "= " + num);
	return num;
  }

  @Override
  public Chat getChatByFriend(String ownId, String friendId) {
	return null;
  }

  @Override
  public void sendMessage(String ownId, String friendId, String message) {

  }

  @Override
  public void save(Message message) {
	Query query = new Query(Criteria.where("id").is(message.getChatId()));
	Update update = new Update().addToSet("messages", message);
	this.getMongoTemplate().upsert(query, update, Chat.class);
  }

  @Override
  public void update(String chatId, String userId) {
	Query query = new Query();
	query.addCriteria(Criteria.where("chatId").is(chatId).and("userId").is(userId).and("read").is(false));
	Update update = Update.update("read", true);
	this.getMongoTemplate().updateMulti(query, update, Message.class);
  }

  private boolean isExistFriendChat(String openid, List<Chat> chats) {
	for (Chat chat : chats) {
	  if (chat.getBuserCode().equals(openid)) {
		return true;
	  }
	}
	return false;
  }
}
