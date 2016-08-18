package com.zm.nettysocketio;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

public class App {
  public static void main(String[] args) throws InterruptedException {
	Configuration config = new Configuration();
	config.setHostname("10.68.19.114");
	config.setPort(8088);

	SocketIOServer server = new SocketIOServer(config);
	CharteventListener listner = new CharteventListener();
	listner.setServer(server);
	// chatevent为事件名称
	server.addEventListener("chatevent", ChatObject.class, listner);
	//启动服务
	server.start();
	Thread.sleep(Integer.MAX_VALUE);
	server.stop();
  }
}