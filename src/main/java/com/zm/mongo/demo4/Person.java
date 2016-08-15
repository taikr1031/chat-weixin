package com.zm.mongo.demo4;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
public class Person implements Serializable {
  @Id
  private String id;
  private String name;
  private int age;

  private Address address;

  public String getId() {
	return id;
  }

  public void setId(String id) {
	this.id = id;
  }

  public String getName() {
	return name;
  }

  public void setName(String name) {
	this.name = name;
  }

  public int getAge() {
	return age;
  }

  public void setAge(int age) {
	this.age = age;
  }

  public Address getAddress() {
	return address;
  }

  public void setAddress(Address address) {
	this.address = address;
  }
}