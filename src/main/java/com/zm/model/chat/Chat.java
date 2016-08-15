package com.zm.model.chat;

import com.zm.model.GenericObject;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Document
public class Chat extends GenericObject {

  private String auserId;
  private String auserPic;
  private String auserCode;
  private String auserName;

  private String buserId;
  private String buserPic;
  private String buserCode;
  private String buserName;

  private long auserTimeFrome1970;
  private long buserTimeFrome1970;

  private Timestamp auserOriginalTime;
  private Timestamp buserOriginalTime;
  private Boolean auserTop;
  private Boolean buserTop;
  private Boolean auserShowHints; // 标记为未读
  private Boolean buserShowHints;

  private int noReadMsgNum;

  private Message lastMessage;

  public long getBuserTimeFrome1970() {
	return buserTimeFrome1970;
  }

  public void setBuserTimeFrome1970(long buserTimeFrome1970) {
	this.buserTimeFrome1970 = buserTimeFrome1970;
  }

  public String getAuserId() {
	return auserId;
  }

  public void setAuserId(String auserId) {
	this.auserId = auserId;
  }

  public String getAuserPic() {
	return auserPic;
  }

  public void setAuserPic(String auserPic) {
	this.auserPic = auserPic;
  }

  public String getAuserCode() {
	return auserCode;
  }

  public void setAuserCode(String auserCode) {
	this.auserCode = auserCode;
  }

  public String getAuserName() {
	return auserName;
  }

  public void setAuserName(String auserName) {
	this.auserName = auserName;
  }

  public String getBuserId() {
	return buserId;
  }

  public void setBuserId(String buserId) {
	this.buserId = buserId;
  }

  public String getBuserPic() {
	return buserPic;
  }

  public void setBuserPic(String buserPic) {
	this.buserPic = buserPic;
  }

  public String getBuserCode() {
	return buserCode;
  }

  public void setBuserCode(String buserCode) {
	this.buserCode = buserCode;
  }

  public String getBuserName() {
	return buserName;
  }

  public void setBuserName(String buserName) {
	this.buserName = buserName;
  }

  public long getAuserTimeFrome1970() {
	return auserTimeFrome1970;
  }

  public void setAuserTimeFrome1970(long auserTimeFrome1970) {
	this.auserTimeFrome1970 = auserTimeFrome1970;
  }

  public Timestamp getAuserOriginalTime() {
	return auserOriginalTime;
  }

  public void setAuserOriginalTime(Timestamp auserOriginalTime) {
	this.auserOriginalTime = auserOriginalTime;
  }

  public Timestamp getBuserOriginalTime() {
	return buserOriginalTime;
  }

  public void setBuserOriginalTime(Timestamp buserOriginalTime) {
	this.buserOriginalTime = buserOriginalTime;
  }

  public Boolean getAuserTop() {
	return auserTop;
  }

  public void setAuserTop(Boolean auserTop) {
	this.auserTop = auserTop;
  }

  public Boolean getBuserTop() {
	return buserTop;
  }

  public void setBuserTop(Boolean buserTop) {
	this.buserTop = buserTop;
  }

  public Boolean getAuserShowHints() {
	return auserShowHints;
  }

  public void setAuserShowHints(Boolean auserShowHints) {
	this.auserShowHints = auserShowHints;
  }

  public Boolean getBuserShowHints() {
	return buserShowHints;
  }

  public void setBuserShowHints(Boolean buserShowHints) {
	this.buserShowHints = buserShowHints;
  }

  public int getNoReadMsgNum() {
	return noReadMsgNum;
  }

  public void setNoReadMsgNum(int noReadMsgNum) {
	this.noReadMsgNum = noReadMsgNum;
  }

  public Message getLastMessage() {
	return lastMessage;
  }

  public void setLastMessage(Message lastMessage) {
	this.lastMessage = lastMessage;
  }

}
