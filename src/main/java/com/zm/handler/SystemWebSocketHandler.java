package com.zm.handler;

import java.io.IOException;
import java.util.ArrayList;

import com.zm.service.WebSocketService;
import com.zm.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class SystemWebSocketHandler implements WebSocketHandler {
  private static final Logger logger;
  private static final ArrayList<WebSocketSession> users;

  static {
	users = new ArrayList<WebSocketSession>();
	logger = LoggerFactory.getLogger(SystemWebSocketHandler.class);
  }

  @Autowired
  private WebSocketService webSocketService;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
	logger.debug("connect to the websocket success......");
	users.add(session);
	String userName = (String) session.getAttributes().get(Constants.WEBSOCKET_USERNAME);
	if (userName != null) {
	  //查询未读消息
	  int count = webSocketService.getUnReadNews(userName);
	  session.sendMessage(new TextMessage(count + ""));
	}
  }

  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
	logger.debug("websocket sendMessage: " + message);
	String friendCode = message.getPayload().toString().split("___")[1];
	sendMessageToUser(friendCode, (TextMessage) message);
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	logger.debug("websocket transport error......");
	if (session.isOpen()) {
	  session.close();
	}
	users.remove(session);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
	logger.debug("websocket connection closed......");
	users.remove(session);
  }

  @Override
  public boolean supportsPartialMessages() {
	return false;
  }

  /**
   * 给所有在线用户发送消息
   *
   * @param message
   */
  public void sendMessageToUsers(TextMessage message) {
	for (WebSocketSession user : users) {
	  try {
		if (user.isOpen()) {
		  user.sendMessage(message);
		}
	  } catch (IOException e) {
		e.printStackTrace();
	  }
	}
  }

  /**
   * 给某个用户发送消息
   */
  public void sendMessageToUser(String code, TextMessage message) {
	for (WebSocketSession user : users) {
	  if (user.getAttributes().get(Constants.WEBSOCKET_USERNAME).equals(code)) {
		try {
		  if (user.isOpen()) {
			user.sendMessage(message);
		  }
		} catch (IOException e) {
		  e.printStackTrace();
		}
		break;
	  }
	}
  }
}