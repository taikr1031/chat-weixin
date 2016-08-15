package com.zm.model.chat;

import com.zm.model.GenericObject;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;

@Document
public class Message extends GenericObject {

  private String chatId;
  private String userId;
  private String content;
  private String pic;
  private String type;
  private String mediaId;
  private Timestamp time;
  private Boolean read;

  public String getChatId() {
	return chatId;
  }

  public void setChatId(String chatId) {
	this.chatId = chatId;
  }

  public String getUserId() {
	return userId;
  }

  public void setUserId(String userId) {
	this.userId = userId;
  }

  public String getContent() {
	return content;
  }

  public void setContent(String content) {
	this.content = content;
  }

  public Timestamp getTime() {
	return time;
  }

  public void setTime(Timestamp time) {
	this.time = time;
  }

  public String getPic() {
	return pic;
  }

  public void setPic(String pic) {
	this.pic = pic;
  }

  public String getType() {
	return type;
  }

  public void setType(String type) {
	this.type = type;
  }

  public String getMediaId() {
	return mediaId;
  }

  public void setMediaId(String mediaId) {
	this.mediaId = mediaId;
  }

  public Boolean getRead() {
	return read;
  }

  public void setRead(Boolean read) {
	this.read = read;
  }
}
