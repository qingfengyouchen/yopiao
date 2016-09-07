package com.zx.stlife.entity.order;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.sys.User;

import javax.persistence.*;
import java.util.*;

/**
 * 夺宝号码
 */
@Entity
@Table(name = "order_snatch_num")
public class SnatchNum extends SuperIntEntity {

    /**
     * 号码，在范围内随机生成
     */
    private Integer num;

    /**
     * 订单明细
     */
    private SnatchRecordDetail snatchRecordDetail;

    /**
     * 商品期数
     */
    private GoodsTimes goodsTimes;

    /**
     * 用户
     */
    private User user;

    /**
     * Default constructor
     */
    public SnatchNum() {

    }

    public SnatchNum(Integer num, SnatchRecordDetail snatchRecordDetail, GoodsTimes goodsTimes, User user) {
        this.num = num;
        this.snatchRecordDetail = snatchRecordDetail;
        this.goodsTimes = goodsTimes;
        this.user = user;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "snatch_record_id")
    public SnatchRecordDetail getSnatchRecordDetail() {
        return snatchRecordDetail;
    }

    public void setSnatchRecordDetail(SnatchRecordDetail snatchRecordDetail) {
        this.snatchRecordDetail = snatchRecordDetail;
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
}