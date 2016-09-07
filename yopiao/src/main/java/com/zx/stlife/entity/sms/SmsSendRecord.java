package com.zx.stlife.entity.sms;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.base.jpa.model.SuperIntEntity;

/**
 * 短信发送记录
 */

@Entity
@Table(name = "sms_send_record")
public class SmsSendRecord extends SuperIntEntity {

	/** 发送者 */
	private String sender;
	/** 手机号码 */
	private String mobileNo;
	/** 短信内容 */
	private String content;
	/** 发送结果代码 */
	private String resultCode;

	public SmsSendRecord(String sender, String mobileNo, String content,
			String resultCode) {
		this.sender = sender;
		this.mobileNo = mobileNo;
		this.content = content;
		this.resultCode = resultCode;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

}
