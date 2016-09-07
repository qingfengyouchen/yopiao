package com.zx.stlife.entity.order;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.SuperIntVersion;
import com.zx.stlife.entity.sys.User;

import javax.persistence.*;
import java.util.*;

/**
 * 夺宝记录
 */
@Entity
@Table(name = "order_pay_record")
public class PayRecord extends SuperIntVersion{

    /**
     * 
     */
    private User user;

    /**
     * 总金额
     */
    private Integer money;

    /**商户订单号*/
    private String outTradeNo;

    /**第三方支付时间*/
    private Date thirdpartPayTime;

    /**
     * 支付描述信息
     */
    private String resultDescr;

    /**充值到余额中的金额*/
    private Integer toBalance = 0;

    /**
     * 是否需要查询支付结果，针对微信
     */
    private Boolean needQueryPayResult;

    /**
     * 查询支付结果的截至时间
     */
    private Date queryDeadlineTime;

    /**
     * 支付方式
     */
    private List<PayWay> payWayList;

    /**
     * 夺宝明细
     */
    private List<SnatchRecordDetail> snatchRecordDetailList;

    /**
     * Default constructor
     */
    public PayRecord() {
        super(Const.PayResult.UNPAY);
    }

    public PayRecord(User user, Integer money, String outTradeNo,
                     Boolean needQueryPayResult, Date queryDeadlineTime, Byte state, String resultDescr) {
        super(state);
        this.user = user;
        this.money = money;
        this.outTradeNo = outTradeNo;
        this.needQueryPayResult = needQueryPayResult;
        this.queryDeadlineTime = queryDeadlineTime;
        this.resultDescr = resultDescr;
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

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_record_id")
    public List<PayWay> getPayWayList() {
        return payWayList;
    }

    public void setPayWayList(List<PayWay> payWayList) {
        this.payWayList = payWayList;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_record_id")
    public List<SnatchRecordDetail> getSnatchRecordDetailList() {
        return snatchRecordDetailList;
    }

    public void setSnatchRecordDetailList(List<SnatchRecordDetail> snatchRecordDetailList) {
        this.snatchRecordDetailList = snatchRecordDetailList;
    }

    public Date getThirdpartPayTime() {
        return thirdpartPayTime;
    }

    public void setThirdpartPayTime(Date thirdpartPayTime) {
        this.thirdpartPayTime = thirdpartPayTime;
    }

    public String getResultDescr() {
        return resultDescr;
    }

    public void setResultDescr(String resultDescr) {
        this.resultDescr = resultDescr;
    }

    public Integer getToBalance() {
        return toBalance;
    }

    public void setToBalance(Integer toBalance) {
        this.toBalance = toBalance;
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
}