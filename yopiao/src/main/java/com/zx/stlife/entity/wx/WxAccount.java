package com.zx.stlife.entity.wx;

import com.base.jpa.model.SuperIntEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 微信公众号信息
 * 
 * @author micheal cao
 */
@Entity
@Table(name = "wx_account")
public class WxAccount extends SuperIntEntity {
	/**公众帐号名称*/
	private String name;
	/**公众帐号TOKEN*/
	private String token;
	/**公众原始ID*/
	private String accountId;
	/**电子邮箱*/
	private String email;
	/**公众帐号APPID*/
	private String appId;
	/**公众帐号APPSECRET*/
	private String appSecret;
	/**添加accessToken的时间*/
	private Date addTokenTime;
	/**公众号的全局唯一票据 access_token的有效期目前为2个小时*/
	private String accessToken;
	/**accessToken失效时间*/
	private Date expiresDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Date getAddTokenTime() {
		return addTokenTime;
	}

	public void setAddTokenTime(Date addTokenTime) {
		this.addTokenTime = addTokenTime;
	}

	public Date getExpiresDate() {
		return expiresDate;
	}

	public void setExpiresDate(Date expiresDate) {
		this.expiresDate = expiresDate;
	}
}
