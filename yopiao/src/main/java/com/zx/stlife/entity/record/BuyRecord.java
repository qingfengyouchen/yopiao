package com.zx.stlife.entity.record;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.member.Member;
import com.zx.stlife.entity.order.SnatchRecordDetail;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.tools.DateUtils;

import javax.persistence.*;

/**
 * 全站购买记录，用于统计
 */
@Entity
@Table(name = "record_buy_record")
public class BuyRecord extends SuperIntEntity {

    /**
     * 商品期号
     */
    private GoodsTimes goodsTimes;

    /**
     * 购买记录
     */
    private SnatchRecordDetail snatchRecordDetail;

    /**
     * 用户
     */
    private User user;

    /**
     * 用户昵称
     */
    private String userNickName;

    /**
     * 夺宝时间毫秒
     */
    private Long snatchTime;
    /**
     * 购买时间, 由HHmmssSSS组成
     */
    private Integer timeValue;

    /**
     * Default constructor
     */
    public BuyRecord() {
        super(Const.CommonState.ENABLE);
    }

    public BuyRecord(GoodsTimes goodsTimes, SnatchRecordDetail snatchRecordDetail, User user) {
        super(Const.CommonState.ENABLE);
        this.goodsTimes = goodsTimes;
        this.snatchRecordDetail = snatchRecordDetail;
        this.user = user;
        this.userNickName = user.getNickName();
        this.snatchTime = snatchRecordDetail.getSnatchTime();
        this.timeValue = DateUtils.millisecondsToHHMMSSSSS(snatchTime);
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
    @JoinColumn(name = "snatch_record_detail_id")
    public SnatchRecordDetail getSnatchRecordDetail() {
        return snatchRecordDetail;
    }

    public void setSnatchRecordDetail(SnatchRecordDetail snatchRecordDetail) {
        this.snatchRecordDetail = snatchRecordDetail;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public Long getSnatchTime() {
        return snatchTime;
    }

    public void setSnatchTime(Long snatchTime) {
        this.snatchTime = snatchTime;
    }

    public Integer getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(Integer timeValue) {
        this.timeValue = timeValue;
    }

    // #################非持久化属性##############

    private String snatchTimeStr;

    @Transient
    public String getSnatchTimeStr() {
        if(snatchTimeStr == null){
            snatchTimeStr = DateUtils
                    .millisecondsToYYYY_MM_DD_HH_MM_SS_SSSString(getSnatchTime());
        }
        return snatchTimeStr;
    }

    public void setSnatchTimeStr(String snatchTimeStr) {
        this.snatchTimeStr = snatchTimeStr;
    }
}