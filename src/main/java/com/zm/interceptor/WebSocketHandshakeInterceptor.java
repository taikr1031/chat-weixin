package com.zm.interceptor;

import com.zm.model.user.User;
import com.zm.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

  private static Logger logger = (Logger) LoggerFactory.getLogger(WebSocketHandshakeInterceptor.class);

  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
								 Map<String, Object> attributes) throws Exception {
//	if(request.getHeaders().containsKey("Sec-WebSocket-Extensions")) {
//	  request.getHeaders().set("Sec-WebSocket-Extensions", "permessage-deflate");
//	}

	if (request instanceof ServletServerHttpRequest) {
	  ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
	  HttpSession session = servletRequest.getServletRequest().getSession(false);
	  if (session != null && session.getAttribute(Constants.SESSION_USERNAME) != null) {
		//使用userName区分WebSocketHandler，以便定向发送消息
		String userId = ((User) session.getAttribute(Constants.SESSION_USERNAME)).getId();
		attributes.put(Constants.WEBSOCKET_USERNAME, userId);
	  }
	}
	return true;
  }

  @Override
  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

  }
}