package com.netease.myBlog.myStruct;

public class GlobalParameters {
	final static String BASE_USER = "/api/user"; 
	final static String BASE_BLOG = "/api/blog";
	final static String BASE_PICTURE = "/api/picture"; 
	public static class MiniBlogInterface {
		public static final String BASE_URL = "http://study-miniblog-new.qa.netease.com";
		public static String CREATE_USERR = BASE_USER + "/createuser";
		public static String LOGIN = BASE_USER + "/login";
		public static String USER_INFO = BASE_USER + "/userinfo";
		public static String CREATE_BLOG = BASE_BLOG + "/createblog";
		public static String DELETE_BLOG = BASE_BLOG + "/deleteblog";
		public static String UPDATE_BLOG = BASE_BLOG + "/updateblog";
		public static String GET_BLOG_LIST = BASE_BLOG + "/bloglist";
		public static String GET_BLOG_DETAIL = BASE_BLOG + "/blogdetail";
		public static String UPDATE_PICTURE = BASE_PICTURE + "/uploadpicture";
	}
	
	public static String TOKEN = "";
	public final static int ACCOUNT_PASSWORD_NULL = -1;
	

}
