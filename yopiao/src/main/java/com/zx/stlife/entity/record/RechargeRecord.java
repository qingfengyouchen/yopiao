package com.zx.stlife.entity.record;

import com.base.jpa.model.SuperIntEntity;
import com.base.modules.util.DateUtilsEx;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.tools.DateUtils;

import javax.persistence.*;
import java.util.*;

/**
 * 充值记录
 */
@Entity
@Table(name = "record_recharge")
public class RechargeRecord extends SuperIntEntity {

    /**
     * 用户
     */
    private User user;

    /**
     * 充值金额
     */
    private Integer money;

    /**
     * 支付方式
     */
    private Byte payWay;
    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 是否需要查询支付结果，针对微信
     */
    private Boolean needQueryPayResult;

    /**
     * 查询支付结果的截至时间
     */
    private Date queryDeadlineTime;

    /**
     * 付款时间
     */
    private Date payTime;

    /**
     * Default constructor
     */
    public RechargeRecord() {
        super(Const.RechargeState.UNRECHARGE);
    }

    public RechargeRecord(User user, Integer money, Byte payWay, Byte state, Date payTime) {
        super(state);
        this.user = user;
        this.money = money;
        this.payWay = payWay;
        this.payTime = payTime;
        this.needQueryPayResult = false;
    }

    public RechargeRecord(User user, Integer money, Byte payWay, String outTradeNo,
                          Boolean needQueryPayResult, Date queryDeadlineTime, Byte state) {
        super(state);
        this.user = user;
        this.money = money;
        this.payWay = payWay;
        this.outTradeNo = outTradeNo;
        this.needQueryPayResult = needQueryPayResult;
        this.queryDeadlineTime = queryDeadlineTime;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Byte getPayWay() {
        return payWay;
    }

    public void setPayWay(Byte payWay) {
        this.payWay = payWay;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Boolean getNeedQueryPayResult() {
        return needQueryPayResult;
    }

    public void setNeedQueryPayResult(Boolean needQueryPayResult) {
        this.needQueryPayResult = needQueryPayResult;
    }

    public Date getQueryDeadlineTime() {
        return queryDeadlineTime;
    }

    public void setQueryDeadlineTime(Date queryDeadlineTime) {
        this.queryDeadlineTime = queryDeadlineTime;
    }

    // ################非持久化属性#################
    private String payTimeStr;

    @Transient
    public String getPayTimeStr() {
        if(payTimeStr == null){
            payTimeStr = DateUtils.dateToYYYYMMDDHHMMSSSSSString(getPayTime());
        }
        return payTimeStr;
    }

    public void setPayTimeStr(String payTimeStr) {
        this.payTimeStr = payTimeStr;
    }
}