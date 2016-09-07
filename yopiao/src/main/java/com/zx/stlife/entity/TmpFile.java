package com.zx.stlife.entity;

import com.base.jpa.model.SuperIntEntity;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tmp_file")
public class TmpFile extends SuperIntEntity {
    private Integer userId;
    private String url;

    public TmpFile() {
    }

    public TmpFile(Integer userId, String url) {
        this.userId = userId;
        this.url = url;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
