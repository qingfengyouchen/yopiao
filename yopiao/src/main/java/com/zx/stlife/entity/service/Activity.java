package com.zx.stlife.entity.service;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.zx.stlife.entity.SuperContent;

/**
 * 天台活动
 */
@Entity
@Table(name = "service_activity")
public class Activity extends SuperContent {

	/**
	 * Default constructor
	 */
	public Activity() {
	}

	/**
	 * 缩略图url
	 */
	private String thumbImgUrl;

	/**
	 * 图片url
	 */
	private String imgUrl;

	/**
	 * 活动时间
	 */
	private Date activityTime;

	/**
	 * 设定人数
	 */
	private Integer maxJoinAmount;

	/**
	 * 已报名人数
	 */
	private Integer hasJoinAmount;
	
	/**
	 * 类型 1：活动，2：公告
	 */
	private Integer type;
	

	/**
	 * 活动地址
	 */
	private String address;

	public String getThumbImgUrl() {
		return thumbImgUrl;
	}

	public void setThumbImgUrl(String thumbImgUrl) {
		this.thumbImgUrl = thumbImgUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Date getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(Date activityTime) {
		this.activityTime = activityTime;
	}

	public Integer getMaxJoinAmount() {
		return maxJoinAmount;
	}

	public void setMaxJoinAmount(Integer maxJoinAmount) {
		this.maxJoinAmount = maxJoinAmount;
	}

	public Integer getHasJoinAmount() {
		return hasJoinAmount;
	}

	public void setHasJoinAmount(Integer hasJoinAmount) {
		this.hasJoinAmount = hasJoinAmount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}