package com.zx.stlife.entity.goods;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.constant.Const;

import javax.persistence.*;
import java.util.Date;

/**
 * 商品图片
 */
@Entity
@Table(name = "goods_image")
public class GoodsImage extends SuperIntEntity {

    /**
     * 
     */
    private String url;

    /**
     * 
     */
    private GoodsInfo goodsInfo;

    /**
     * 类别：1-头部切换图片，2-图文详情
     */
    private Byte category;

    /**
     * 
     */
    private Date editTime;
    /**
     * Default constructor
     */
    public GoodsImage() {
        super(Const.CommonState.ENABLE);
    }

    public GoodsImage(GoodsInfo goodsInfo, String url, Byte category) {
        super(Const.CommonState.ENABLE);
        this.url = url;
        this.goodsInfo = goodsInfo;
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    public GoodsInfo getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(GoodsInfo goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public Byte getCategory() {
        return category;
    }

    public void setCategory(Byte category) {
        this.category = category;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

}