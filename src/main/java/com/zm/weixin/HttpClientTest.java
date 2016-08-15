package com.zm.weixin;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpClientTest {

  public static String getTicket(String url) {
	HttpClient httpclient = new DefaultHttpClient();
	HttpGet httpget = new HttpGet(url);
	try {
	  HttpResponse response = httpclient.execute(httpget);
	  HttpEntity entity = response.getEntity();
	  if (entity != null) {
		String result = EntityUtils.toString(entity);
		return result;
	  }
	} catch (Exception e) {
	  e.printStackTrace();
	}
	new IllegalArgumentException("url传入参数异常!");
	return null;
  }
}
