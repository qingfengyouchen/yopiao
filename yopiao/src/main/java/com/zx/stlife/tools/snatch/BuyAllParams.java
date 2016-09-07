package com.zx.stlife.tools.snatch;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by micheal on 16/1/17.
 */
public class BuyAllParams implements Serializable{

    /**商品期号id*/
    private Integer goodsTimesId;
    /**用户数量*/
    private Integer userAmount;
    /**用时（分钟）*/
    private Integer takeTime;
    /**买满时间*/
    private Date fullTime;

    public BuyAllParams() {
    }

    @NotNull(message = "商品期号不为空")
    public Integer getGoodsTimesId() {
        return goodsTimesId;
    }

    public void setGoodsTimesId(Integer goodsTimesId) {
        this.goodsTimesId = goodsTimesId;
    }

    @NotNull(message = "用户数量不为空")
    public Integer getUserAmount() {
        return userAmount;
    }

    public void setUserAmount(Integer userAmount) {
        this.userAmount = userAmount;
    }

    @NotNull(message = "买满时间（分钟）不为空")
    public Integer getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(Integer takeTime) {
        this.takeTime = takeTime;
    }

    public Date getFullTime() {
        return fullTime;
    }

    public void setFullTime(Date fullTime) {
        this.fullTime = fullTime;
    }
}
