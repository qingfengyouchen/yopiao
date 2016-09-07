package com.zx.stlife.entity.order;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.entity.goods.GoodsInfo;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.sys.User;

import javax.persistence.*;
import java.util.*;

/**
 * 夺宝清单，等同购物车
 */
@Entity
@Table(name = "order_snatch_list_item")
public class SnatchListItem extends SuperIntEntity {

    /**
     * Default constructor
     */
    public SnatchListItem() {
    }

    /**
     * 商品
     */
    private GoodsInfo goodsInfo;

    /**
     * 商品期数
     */
    private GoodsTimes goodsTimes;

    /**
     * 用户
     */
    private User user;

    /**
     * 购买人次
     */
    private Integer buyTimes;

    /**
     * 购买金额
     */
    private Integer money;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    public GoodsInfo getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(GoodsInfo goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "goods_times_id")
    public GoodsTimes getGoodsTimes() {
        return goodsTimes;
    }

    public void setGoodsTimes(GoodsTimes goodsTimes) {
        this.goodsTimes = goodsTimes;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getBuyTimes() {
        return buyTimes;
    }

    public void setBuyTimes(Integer buyTimes) {
        this.buyTimes = buyTimes;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }
}