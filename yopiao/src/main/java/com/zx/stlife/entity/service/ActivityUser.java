package com.zx.stlife.entity.service;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.entity.sys.User;

/**
 * 天台活动参与用户
 */
@Entity
@Table(name = "service_activity_user")
public class ActivityUser extends SuperIntEntity {

	/**
	 * Default constructor
	 */
	public ActivityUser() {
	}

	/**
	 * 活动
	 */
	private Activity activity;

	/**
	 * 参与用户
	 */
	private User user;

	/**
	 * 真实姓名
	 */
	private String trueName;

	/**
	 * 手机号码
	 */
	private String mobileNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "activity_id")
	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

}