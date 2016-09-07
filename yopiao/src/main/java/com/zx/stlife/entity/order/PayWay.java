package com.zx.stlife.entity.order;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.record.RedPack;

import javax.persistence.*;
import java.util.*;

/**
 * 付款方式
 */
@Entity
@Table(name = "order_pay_way")
public class PayWay extends SuperIntEntity {

    /**
     * 1-支付宝，2-微信支付，3-红包， 4-天台生活币
     */
    private Byte way;

    /**
     * 
     */
    private Integer money;

    private PayRecord payRecord;

    private RedPack redPack;

    /**
     * Default constructor
     */
    public PayWay() {
        super(Const.CommonState.ENABLE);
    }

    public PayWay(Byte way, Integer money, PayRecord payRecord, RedPack redPack) {
        super(Const.CommonState.ENABLE);
        this.way = way;
        this.money = money;
        this.payRecord = payRecord;
        this.redPack = redPack;
    }

    public Byte getWay() {
        return way;
    }

    public void setWay(Byte way) {
        this.way = way;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "pay_record_id")
    public PayRecord getPayRecord() {
        return payRecord;
    }

    public void setPayRecord(PayRecord payRecord) {
        this.payRecord = payRecord;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "redpack_id")
    public RedPack getRedPack() {
        return redPack;
    }

    public void setRedPack(RedPack redPack) {
        this.redPack = redPack;
    }
}