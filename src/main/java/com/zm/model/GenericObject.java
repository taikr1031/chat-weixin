package com.zm.model;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public class GenericObject implements Serializable {

  @Id
  private String id;

  private String name;

  public String getId() {
	return id;
  }

  public void setId(String id) {
	this.id = id;
  }

  public void setId(String auserId, String buserId) {
	if (Integer.parseInt(auserId) > Integer.parseInt(buserId)) {
	  this.id = buserId + "-" + auserId;
	} else {
	  this.id = auserId + "-" + buserId;
	}
  }

  public String getName() {
	return name;
  }

  public void setName(String name) {
	this.name = name;
  }
}
