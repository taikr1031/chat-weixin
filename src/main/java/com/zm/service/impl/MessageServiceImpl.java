package com.zm.service.impl;

import com.zm.model.chat.Chat;
import com.zm.model.chat.Message;
import com.zm.mongo.core.GenericMongoServiceImpl;
import com.zm.service.MessageService;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("messageService")
public class MessageServiceImpl extends GenericMongoServiceImpl<Message> implements MessageService{

  public List<Message> queryMessage(String chatId) {
	Query query = new Query();
	query.addCriteria(Criteria.where("chatId").is(chatId));
	List<Message> messages = getMongoTemplate().find(query, Message.class);
	return messages;
  }

  @Override
  public void save(Message message) {
	this.getMongoTemplate().save(message);
  }

}
