package com.zm.controller;

import com.zm.model.chat.Message;
import com.zm.service.MessageService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/message")
public class MessageController {

  @Autowired
  private MessageService messageService;

  private WxMpService wxMpService = new WxMpServiceImpl();

  @RequestMapping("/queryMessage/{chatId}")
  public List<Message> queryMessage(@PathVariable String chatId, HttpServletResponse response) {
	response.setHeader("Access-Control-Allow-Origin", "*"); //允许所有域名访问
	return messageService.queryMessage(chatId);
  }

  @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = "application/json")
  public void save(@RequestBody Message message, HttpServletRequest request, HttpServletResponse response) {
	response.setHeader("Access-Control-Allow-Origin", "*"); //允许所有域名访问
//	response.setHeader("Access-Control-Allow-Methods", "POST");
	response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
	File file = null;
	try {
//	  if (message.getType().equals("IMAGE")) {
//		file = wxMpService.mediaDownload((String) message.getContent()); //
//		message.setContent(FileUtils.readFileToByteArray(file));
//	  }
	  this.messageService.save(message);
	} catch (Exception e) {
	  e.printStackTrace();
	}
  }

}
