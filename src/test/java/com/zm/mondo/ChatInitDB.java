package com.zm.mondo;

import com.zm.model.chat.Chat;
import com.zm.model.chat.Message;
import com.zm.mongo.core.GenericMongoService;
import com.zm.mongo.core.GenericMongoServiceImpl;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Timestamp;
import java.util.Date;

public class ChatInitDB {

  public static void main(String[] args) {
	ConfigurableApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath:/spring-mongodb-test.xml"});
	GenericMongoService mongoService = (GenericMongoServiceImpl) context.getBean("genericMongoService");

	try {
	  mongoService.dropCollectionByClass(Chat.class);

	  createChat(mongoService, "1", "ZM", "oMPxav8gQa7VgRFjILtzRX_lhymE", "2", "YJ", "oMPxav6aC_TuPncPkgHhE998bboA", 22);
	  Thread.sleep(1000);
	  createChat(mongoService, "1", "ZM", "oMPxav8gQa7VgRFjILtzRX_lhymE", "3", "LJ", "oMPxav8EjT7cotajZ7_YSisGbFtc", 33);
	  Thread.sleep(1000);
	  createChat(mongoService, "1", "ZM", "oMPxav8gQa7VgRFjILtzRX_lhymE", "4", "WY", "code4", 44);
	  Thread.sleep(1000);
	System.out.print("dddddddddddd");
	  createChat(mongoService, "2", "YJ", "oMPxav6aC_TuPncPkgHhE998bboA", "3", "LJ", "oMPxav8EjT7cotajZ7_YSisGbFtc", 33);
	  Thread.sleep(1000);
	  createChat(mongoService, "2", "YJ", "oMPxav6aC_TuPncPkgHhE998bboA", "4", "WY", "code4", 44);
	} catch (InterruptedException e) {
	  e.printStackTrace();
	}
  }

  private static void createChat(GenericMongoService mongoService, String auserId, String auserName, String auserCode, String buserId, String buserName, String buserCode, int num) {
	Chat chat = new Chat();
	chat.setId(auserId, buserId);
	chat.setAuserId(auserId);
	chat.setBuserId(buserId);
	chat.setAuserPic("/www/img/" + auserId + ".png");
	chat.setBuserPic("/www/img/" + buserId + ".png");
	chat.setAuserCode(auserCode);
	chat.setBuserCode(buserCode);
	chat.setAuserName(auserName);
	chat.setBuserName(buserName);
	chat.setAuserTimeFrome1970(new Date().getTime());
	chat.setBuserTimeFrome1970(new Date().getTime());
	chat.setAuserShowHints(false);
	chat.setBuserShowHints(false);
	chat.setAuserOriginalTime(new Timestamp(System.currentTimeMillis()));
	chat.setBuserOriginalTime(new Timestamp(System.currentTimeMillis()));

	mongoService.saveObject(chat);
  }

}
