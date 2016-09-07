package com.zx.stlife.entity.goods;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.tools.DateUtils;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.*;

/**
 * 晒单
 */
@Entity
@Table(name = "goods_share")
public class ShareGoods extends SuperIntEntity {

    /**
     * 主题
     */
    private String title;

    /**
     * 获奖感言
     */
    private String content;

    /**
     * 晒单人
     */
    private User user;
    /**
     * 晒单人头像
     */
    private String userHeadImg;

    /**
     * 晒单人昵称
     */
    private String userNickName;

    /**
     * 商品期号
     */
    private GoodsTimes goodsTimes;

    /**
     * 商品期号名称
     */
    private Integer goodsTimesName;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 缩略图
     */
    private String thumbnail;

    private List<ShareImage> shareImageList;

    /**
     * Default constructor
     */
    public ShareGoods() {
        super(Const.ShareGoodsState.UNAUDIT);
    }

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
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "goods_times_id")
    public GoodsTimes getGoodsTimes() {
        return goodsTimes;
    }

    public void setGoodsTimes(GoodsTimes goodsTimes) {
        this.goodsTimes = goodsTimes;
    }

    public String getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
    }

    public Integer getGoodsTimesName() {
        return goodsTimesName;
    }

    public void setGoodsTimesName(Integer goodsTimesName) {
        this.goodsTimesName = goodsTimesName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_share_id")
    @Where(clause="state=1")
    @OrderBy("id asc")
    public List<ShareImage> getShareImageList() {
        return shareImageList;
    }

    public void setShareImageList(List<ShareImage> shareImageList) {
        this.shareImageList = shareImageList;
    }

    //############非持久化属性
    private String createTimeStr;

    @Transient
    public String getCreateTimeStr() {
        if(createTimeStr == null ){
            createTimeStr = DateUtils.dateToYYYYMMDDHHMMSSSSSString(this.getCreateTime());
        }
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }
}