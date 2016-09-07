package com.zx.stlife.entity.notice;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.tools.DateUtils;

/**
 * 通知
 */
@Entity
@Table(name = "notice")
public class Notice extends SuperIntEntity {


	/**
     * Default constructor
     */
    public Notice() {
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
     * 创建人
     */
    private User sender;

    /**
     * 创建人
     */
    private User user;

    /**
     * 发送人名称
     */
    private String senderName;

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

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    
    private String createTimeStr;

    @Transient
	public String getCreateTimeStr() {
    	if(createTimeStr == null){
    		createTimeStr = DateUtils.dateToYYYYMMDDHHMMSSSSSString(
    				getCreateTime());
    	}
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

}