package com.zx.stlife.entity.sys;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.constant.Const;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

/**
 * 图片设置
 */
@Entity
@Table(name = "sys_image_setting")
public class ImageSetting extends SuperIntEntity {

    /**
     * 类别：1-介绍天台生活的图片，2-夺宝宣传图，3-发现的公告及活动图
     */
    private Byte category;

    /**动作类型*/
    private Byte actionType;

    /**值*/
    private String value;

    /**
     * 图片路径
     */
    private String url;

    /**
     * 排序号
     */
    private Byte sortNum;

    /**
     * Default constructor
     */
    public ImageSetting() {
        super(Const.CommonState.ENABLE);
    }

    public ImageSetting(Integer id, Byte actionType, String value, String url, Byte sortNum) {
        super(id);
        this.actionType = actionType;
        this.value = value;
        this.url = url;
        this.sortNum = sortNum;
    }

    public Byte getCategory() {
        return category;
    }

    public void setCategory(Byte category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Byte getSortNum() {
        return sortNum;
    }

    public void setSortNum(Byte sortNum) {
        this.sortNum = sortNum;
    }

    public Byte getActionType() {
        return actionType;
    }

    public void setActionType(Byte actionType) {
        this.actionType = actionType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}