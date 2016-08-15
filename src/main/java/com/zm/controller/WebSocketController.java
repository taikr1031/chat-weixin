package com.zm.controller;

import com.zm.handler.SystemWebSocketHandler;
import com.zm.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/websocket")
public class WebSocketController {

  @Autowired
  private WebSocketService webSocketService;

  @Bean
  public SystemWebSocketHandler systemWebSocketHandler() {
	return new SystemWebSocketHandler();
  }

  @RequestMapping("toClient")
  public String toClient(HttpServletRequest request) {
	return "client";
  }

  @RequestMapping("/auditing")
  @ResponseBody
  public String auditing(HttpServletRequest request) {
	String userName = (String) request.getSession().getAttribute("userName");
	//无关代码都省略了
	int unReadNewsCount = webSocketService.getUnReadNews(userName);
	systemWebSocketHandler().sendMessageToUser(userName, new TextMessage(unReadNewsCount + ""));
	return null;
  }
}
