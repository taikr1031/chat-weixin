package com.zm.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/**
 * Timestamp转换器
 * 将Date转换成Timestamp
 */
public class TimestampConverter implements Converter<Date, Timestamp> {

  private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

  public Timestamp convert(Date date) {
	if (date != null) {
//	  SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
//	  return sdf.format(date);
	  return new Timestamp(date.getTime());
	}
	return null;
  }

}