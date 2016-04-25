package com.netease.myBlog.myStruct;

import java.util.ArrayList;
import java.util.List;

/**
 * 博客基本类
 * @author IBM T420S
 *
 */
public class BlogUnit {
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getBlogId() {
		return blogId;
	}

	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(List<String> imageUrl) {
		this.imageUrl = imageUrl;
	}

	String title = "";
	String createTime = "";
	String updateTime = "";
	String content = "";
	String summary = "";
	List<String> imageUrl = new ArrayList<String>();
	String blogId = "";

	/**
	 * @param blogId 
	 * @param title 
	 * @param abstruct
	 * @param createTime
	 * @param updateTime
	 * @param content
	 * @param imageUrl
	 */
	public BlogUnit(String blogId, String title, String abstruct, String createTime,
			String updateTime, String content, List<String> imageUrl) {
		this.blogId = blogId;
		this.title = title;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.content = content;
		this.imageUrl = imageUrl;
	}
	
	public BlogUnit() {
	}

	
}
