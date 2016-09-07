package com.zx.stlife.entity.record;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.base.exception.BizException;
import com.zx.stlife.entity.SuperIntVersion;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.tools.DateUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.*;

/**
 * 红包
 */
@Entity
@Table(name = "record_redpack")
public class RedPack extends SuperIntVersion {

    /**
     * 用户
     */
    private User user;

    /**
     * 红包总金额
     */
    private Integer total;
    /**
     * 红包余额
     */
    private Integer balance;

    /**
     * 类别，1-夺宝红包，2-生活红包
     */
    private Byte category;

    /**
     * 红包来源类型
     */
    private Byte sourceType;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**是否已领取*/
    private Boolean hasDrawed;

    /**
     * Default constructor
     */
    public RedPack() {
        this.hasDrawed = true;
    }

    public RedPack(User user, Integer total, Integer balance,
                   Byte category, Byte sourceType, Date expireTime, Byte state) {
        super(state);
        this.user = user;
        this.total = total;
        this.balance = balance;
        this.category = category;
        this.sourceType = sourceType;
        this.expireTime = expireTime;
        this.hasDrawed = true;
    }

    public RedPack(User user, Integer total, Integer balance,
                   Byte category, Byte sourceType, Date expireTime, Byte state, Boolean hasDrawed) {
        super(state);
        this.user = user;
        this.total = total;
        this.balance = balance;
        this.category = category;
        this.sourceType = sourceType;
        this.expireTime = expireTime;
        this.hasDrawed = hasDrawed;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
    
    public Byte getSourceType() {
        return sourceType;
    }

    public void setSourceType(Byte sourceType) {
        this.sourceType = sourceType;
    }

    public Byte getCategory() {
        return category;
    }

    public void setCategory(Byte category) {
        this.category = category;
    }

    public Boolean getHasDrawed() {
        return hasDrawed;
    }

    public void setHasDrawed(Boolean hasDrawed) {
        this.hasDrawed = hasDrawed;
    }

    // ###############非持久化属性################
    private String expireTimeStr;

    @Transient
    public String getExpireTimeStr() {
        if(StringUtils.isBlank(expireTimeStr)){
            expireTimeStr = DateUtils.dateToYYYYMMDDHHMMSSSSSString(getExpireTime());
        }
        return expireTimeStr;
    }

    public void setExpireTimeStr(String expireTimeStr) {
        this.expireTimeStr = expireTimeStr;
    }

    public int addBalance(int money){
        if(money > 0) {
            money = (getBalance() == null ? 0 : getBalance()) + money;
            setBalance(money);
        }
        return getBalance();
    }

    public int subtractBalance(int money){
        money = (getBalance() == null ? 0 : getBalance()) - money;
        if(money < 0){
            throw new BizException("红包余额不足");
        }
        setBalance(money);
        return money;
    }
}