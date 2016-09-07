package com.zx.stlife.entity;

import javax.persistence.MappedSuperclass;

/**
 * 内容超类
 */
@MappedSuperclass
public class SuperContent extends SuperUserDate {

	/**
	 * Default constructor
	 */
	public SuperContent() {
	}

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
	 */
	private String content; 
	
	/**
	 * html路径
	 */
	private String htmlUrl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHtmlUrl() {
		return htmlUrl;
	}

	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}
	
}