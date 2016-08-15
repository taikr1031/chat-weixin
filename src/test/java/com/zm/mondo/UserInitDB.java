package com.zm.mondo;

import com.zm.model.chat.Message;
import com.zm.model.user.Location;
import com.zm.model.user.Picture;
import com.zm.model.user.User;
import com.zm.mongo.core.GenericMongoService;
import com.zm.mongo.core.GenericMongoServiceImpl;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Timestamp;

public class UserInitDB extends GenericMongoServiceImpl<User>{

  private static GenericMongoService mongoService;

  static {
	ConfigurableApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath:/spring-mongodb-test.xml"});
	mongoService = (GenericMongoServiceImpl) context.getBean("genericMongoService");
  }

  public static void main(String[] args) {
	mongoService.dropCollectionByClass(Message.class);
	createUser(mongoService, "1", "男", "ZM", "oMPxav8gQa7VgRFjILtzRX_lhymE");
	createUser(mongoService, "2", "女", "YJ", "oMPxav6aC_TuPncPkgHhE998bboA");
	createUser(mongoService, "3", "男", "LJ", "oMPxav8EjT7cotajZ7_YSisGbFtc");
	createUser(mongoService, "4", "女", "ZZL", "oMPxav7E8tYZADIRz58AEcez-RAo");
  }

  private static void createUser(GenericMongoService mongoService, String own, String sex, String name, String code) {
	User user = new User();
	user.setId(own);
	user.setName(name);
	user.setPassword("password" + own);
	user.setCode(code);
	user.setPhone("phone" + own);
	user.setCell("cell" + own);
	user.setEmail(own + "@qq.com");
	user.setGender("女");
	user.setTime(new Timestamp(System.currentTimeMillis()));
	Location location = new Location();
	location.setState("中国");
	location.setCity("武汉");
	location.setStreet("宝丰路");
	location.setZip(430064);
	user.setLocation(location);
	Picture picture = new Picture();
	picture.setLarge("/www/img/" + own + ".png");
	picture.setMedium("/www/img/" + own + ".png");
	picture.setThumbnail("/www/img/" + own + ".png");
	user.setPicture(picture);

	mongoService.saveObject(user);
  }

  public static void dropCollection(GenericMongoService mongoService) {
	mongoService.deleteObject("1");
	mongoService.deleteObject("2");
	mongoService.deleteObject("3");
	mongoService.deleteObject("4");
	mongoService.dropCollection();
  }
}
