package com.netease.myBlog.myStruct;

import java.text.SimpleDateFormat;
import java.util.Date;

public class commonFuntion {
	public static String fromatTime (String unixTime) {
		String time = "";
		
		long timeLong = Long.valueOf(unixTime);
		Date date = new Date(timeLong);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		time = formatter.format(date);
		
		return time;
	}
}
