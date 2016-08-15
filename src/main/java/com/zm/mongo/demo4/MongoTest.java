package com.zm.mongo.demo4;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MongoTest {

  public static void main(String[] args) {

	ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring-mongodb.xml");

	Repository<Person> repository = context.getBean(NatureRepositoryImpl.class);
	repository.dropCollection();
	repository.createCollection();
	Person person = new Person();
	Address address = new Address();
	address.setAddr("合肥");
	address.setPro("安徽");
	person.setAddress(address);
	person.setAge(20);
	person.setName("senssic");
	person.setId("1");
	repository.saveObject(person);
	System.out.println("1. " + repository.getAllObjects());
	person.setId("101");
	repository.saveObject(person);
	System.out.println("2. " + repository.getAllObjects());
	System.out.println("Tree with id 1" + repository.getObject("1"));
	repository.updateObject("1", "sen");
	System.out.println("3. " + repository.getAllObjects());
	repository.deleteObject("1");
	System.out.println("4. " + repository.getAllObjects());
	context.close();
  }
}