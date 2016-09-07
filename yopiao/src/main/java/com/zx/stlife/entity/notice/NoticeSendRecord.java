package com.zx.stlife.entity.notice;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.entity.sys.User;

import javax.persistence.*;
import java.util.*;

/**
 * 通知记录
 */
@Entity
@Table(name = "notice_send_record")
public class NoticeSendRecord extends SuperIntEntity {

    /**
     * Default constructor
     */
    public NoticeSendRecord() {
    }

    /**
     * 通知实体
     */
    private Notice notice;

    /**
     * 通知目标
     */
    private User toUser;

    /**
     * 通知目标名字
     */
    private String toUserName;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }
}