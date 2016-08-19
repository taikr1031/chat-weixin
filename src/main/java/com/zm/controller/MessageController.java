package com.zm.controller;

import com.zm.model.chat.Message;
import com.zm.service.MessageService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
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
  public List<Message> queryMessage(@PathVariable String chatId) {
	return messageService.queryMessage(chatId);
  }

  @RequestMapping("/save")
  public void save(@RequestBody Message message, HttpServletRequest request) {
	File file = null;
	try {
	  if (message.getType().equals("IMAGE")) {
		file = wxMpService.mediaDownload((String) message.getContent()); //
		message.setContent(FileUtils.readFileToByteArray(file));
	  }
	  this.messageService.save(message);
	} catch (Exception e) {
	  e.printStackTrace();
	}
  }

}
