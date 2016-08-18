package com.zm.weixin;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@WebServlet(name = "/wxServlet", urlPatterns = "/wxServlet", loadOnStartup = 1,
		initParams = {@WebInitParam(name = "appid", value = "wx791d897f45713b38"),
				@WebInitParam(name = "secret", value = "ec1be18ba7c332f5ff260c08f3c93e2d"),
				@WebInitParam(name = "token", value = "12345678901234567890123456789012")})
public class wxServlet extends HttpServlet {

  private WxMpService wxService = new WxMpServiceImpl();

  public void init() {
	WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
	config.setAppId(getServletConfig().getInitParameter("appid")); // 设置微信公众号的appid
	config.setSecret(getServletConfig().getInitParameter("secret")); // 设置微信公众号的app corpSecret
	config.setToken(getServletConfig().getInitParameter("token")); // 设置微信公众号的token
//	config.setAesKey(""); // 设置微信公众号的EncodingAESKey

	wxService.setWxMpConfigStorage(config);
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doPost(request, response);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String openid = request.getParameter("openid");
	String content = request.getParameter("content");
	String type = request.getParameter("type");
	sendMsg(type, openid, content);
  }

  public void sendMedia(String type, String fileName) {
	String url = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	try {
	  url.replace("ACCESS_TOKEN", wxService.getAccessToken());
	  url.replace("TYPE", type);
	  url.replace("media", fileName);
	} catch (WxErrorException e) {
	  e.printStackTrace();
	}
  }

  public void sendMsg(String type, String openid, String content) throws UnsupportedEncodingException {
	WxMpCustomMessage message = null;
//	content = new String(content.getBytes("GBK"), "UTF-8");
	content = new String(content.getBytes("ISO8859-1"), "UTF-8");
	if (type.equals("TEXT")) {
	  message = WxMpCustomMessage.TEXT().toUser(openid).content(content).build();
	} else if (type.equals("IMAGE")) {
	  message = WxMpCustomMessage.IMAGE().toUser(openid).mediaId(content).build();
	} else if (type.equals("VOICE")) {
	  message = WxMpCustomMessage.VOICE().toUser(openid).mediaId(content).build();
	}
	try {
	  wxService.customMessageSend(message);
	} catch (Exception e) {
	  e.printStackTrace();
	}
  }
}

//http://localhost:8080/wxServlet?openid=oMPxav8gQa7VgRFjILtzRX_lhymE&msg=888