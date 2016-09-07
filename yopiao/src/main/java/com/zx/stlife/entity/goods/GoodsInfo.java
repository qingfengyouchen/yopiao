package com.zx.stlife.entity.goods;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.SuperIntVersion;

import javax.persistence.*;
import java.util.*;

/**
 * 商品信息
 */
@Entity
@Table(name = "goods_info")
public class GoodsInfo extends SuperIntVersion {

    /**
     * Default constructor
     */
    public GoodsInfo() {
        super(Const.CommonState.ENABLE);
    }

    /**
     * 商品名称
     */
    private String name;

    /**提示信息，突出显示*/
    private String tip;

    /**
     * 价格
     */
    private Integer price;

    /**
     * 最小单元金额
     */
    private Integer unitMoney;

    /**
     * 总需购买人次
     */
    private Integer totalTimes;
    
    /**
     * 可兑换的积分
     */
    private Integer changeJifen;

    /**
     * 销量
     */
    private Integer salesAmount = 0;

    /**
     * 商品类别
     */
    private GoodsCategory goodsCategory;

    /**
     * 是否10元夺宝；当为false时，表示1元夺宝
     */
    private Boolean isTenYuan = false;

    /**图文详情html url*/
    private String detailsHtmlUrl;

    /**
     * 
     */
    private String creator;

    /**
     * 修改时间
     */
    private Date editTime;

    /**
     * 
     */
    private String editor;

    /**
     * 状态： 1-正常，2-下架，3-删除
     */
    //private Byte state;

    /**
     * 缩略图
     */
    private String thumbnail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getUnitMoney() {
        return unitMoney;
    }

    public void setUnitMoney(Integer unitMoney) {
        this.unitMoney = unitMoney;
    }

    public Integer getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(Integer totalTimes) {
        this.totalTimes = totalTimes;
    }
    
    public Integer getChangeJifen() {
        return changeJifen;
    }

    public void setChangeJifen(Integer changeJifen) {
        this.changeJifen = changeJifen;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "goods_category_id")
    public GoodsCategory getGoodsCategory() {
        return goodsCategory;
    }

    public void setGoodsCategory(GoodsCategory goodsCategory) {
        this.goodsCategory = goodsCategory;
    }

    public Boolean getIsTenYuan() {
        return isTenYuan;
    }

    public void setIsTenYuan(Boolean isTenYuan) {
        this.isTenYuan = isTenYuan;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getDetailsHtmlUrl() {
        return detailsHtmlUrl;
    }

    public void setDetailsHtmlUrl(String detailsHtmlUrl) {
        this.detailsHtmlUrl = detailsHtmlUrl;
    }

    public Integer getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(Integer salesAmount) {
        this.salesAmount = salesAmount;
    }

    // #################非持久化属性##############

    private Integer goodsCategoryId;

    @Transient
    public Integer getGoodsCategoryId() {
        if(goodsCategoryId == null){
            if(getGoodsCategory() != null){
                goodsCategoryId = getGoodsCategory().getId();
            }
        }
        return goodsCategoryId;
    }

    public void setGoodsCategoryId(Integer goodsCategoryId) {
        this.goodsCategoryId = goodsCategoryId;
    }
}