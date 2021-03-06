package com.zm.config;

import com.zm.handler.SystemWebSocketHandler;
import com.zm.interceptor.WebSocketHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
	registry.addHandler(systemWebSocketHandler(), "/ws").addInterceptors(new WebSocketHandshakeInterceptor()).setAllowedOrigins("*");
	registry.addHandler(systemWebSocketHandler(), "/sockjs/ws").addInterceptors(new WebSocketHandshakeInterceptor()).setAllowedOrigins("*").withSockJS();
  }

  @Bean
  public WebSocketHandler systemWebSocketHandler() {
	return new SystemWebSocketHandler();
  }
}
