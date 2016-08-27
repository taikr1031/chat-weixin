package com.zm.mondo;

import com.zm.model.chat.Message;
import com.zm.mongo.core.GenericMongoService;
import com.zm.mongo.core.GenericMongoServiceImpl;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MessageInitDB {

  public static void main(String[] args) {
	ConfigurableApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath:/spring-mongodb-test.xml"});
	GenericMongoService mongoService = (GenericMongoServiceImpl) context.getBean("genericMongoService");

	try {
	  mongoService.dropCollectionByClass(Message.class);

	  createMessage(mongoService, "1", "1-2");
	  Thread.sleep(1000);
	  createMessage(mongoService, "1", "1-2");
	  Thread.sleep(1000);
	  createMessage(mongoService, "2", "1-2");
	  Thread.sleep(1000);

	  createMessage(mongoService, "1", "1-3");
	  Thread.sleep(1000);
	  createMessage(mongoService, "3", "1-3");

	  createMessage(mongoService, "2", "2-3");
	  Thread.sleep(1000);
	  createMessage(mongoService, "3", "2-3");
	} catch (InterruptedException e) {
	  e.printStackTrace();
	}
  }

  private static void createMessage(GenericMongoService mongoService, String userId, String chatId) {
	Message msg1 = new Message();
	msg1.setChatId(chatId);
	msg1.setUserId(userId);
	msg1.setContent("content" + 1);
	msg1.setPic("img/" + userId + ".png");
	msg1.setType("TEXT");
	msg1.setMediaId("mdeia" + userId);
	msg1.setRead(false);
	mongoService.saveObject(msg1);
  }
}
