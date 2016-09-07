package com.zx.stlife.entity.push;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.base.jpa.model.SuperIntEntity;

/**
 * 推送记录
 */

@Entity
@Table(name = "jpush_record")
public class JpushRecord extends SuperIntEntity {

	/** 推送者 */
	private String sender;
	/** 用户Id */
	private Integer userId;
	/** 消息Id */
	private Long messageId;
	/** 推送结果 */
	private Boolean isResultOk;

	public JpushRecord() {
		super();
	}

	public JpushRecord(String sender, Integer userId, Long messageId,
			Boolean isResultOk) {
		super();
		this.sender = sender;
		this.userId = userId;
		this.messageId = messageId;
		this.isResultOk = isResultOk;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Boolean getIsResultOk() {
		return isResultOk;
	}

	public void setIsResultOk(Boolean isResultOk) {
		this.isResultOk = isResultOk;
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

}
