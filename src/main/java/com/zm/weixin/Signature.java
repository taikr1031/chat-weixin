package com.zm.weixin;

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import java.util.HashMap;
import java.util.Map;

public class Signature {

  public static Map getSign(String openId, String secret, String token, String url) {
	WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
	config.setAppId(openId); // 设置微信公众号的appid
	config.setSecret(secret); // 设置微信公众号的app corpSecret
	config.setToken(token); // 设置微信公众号的token
//	config.setAesKey(""); // 设置微信公众号的EncodingAESKey

	WxMpService wxService = new WxMpServiceImpl();
	wxService.setWxMpConfigStorage(config);

	Map signMap = null;
	try {
//	  WxJsapiSignature signatureObj = wxService.createJsapiSignature(url);
//	  signMap = BeanUtils.describe(signatureObj);

	  signMap = new HashMap();
	  signMap.put("timestamp", "123");
	  signMap.put("noncestr", "123");
	  signMap.put("signature", "123");
	} catch (Exception e) {
	  e.printStackTrace();
	}
	return signMap;
  }
}
