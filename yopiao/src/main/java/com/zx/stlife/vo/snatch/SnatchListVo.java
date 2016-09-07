package com.zx.stlife.vo.snatch;

import java.util.ArrayList;
import java.util.List;

/**
 * 夺宝清单
 */
public class SnatchListVo {

    /**清单id*/
    private Integer snatchListItemId;
    /**期号id*/
    private Integer goodsTimesId;
    /**商品名称*/
    private String goodsName;
    /**购买人次*/
    private Integer buyTimes;
    /**商品期号状态*/
    private Byte state;

    public SnatchListVo(Integer snatchListItemId, Integer goodsTimesId,
                        String goodsName, Integer buyTimes, Byte state) {
        this.snatchListItemId = snatchListItemId;
        this.goodsTimesId = goodsTimesId;
        this.goodsName = goodsName;
        this.buyTimes = buyTimes;
        this.state = state;
    }

    public Integer getSnatchListItemId() {
        return snatchListItemId;
    }

    public void setSnatchListItemId(Integer snatchListItemId) {
        this.snatchListItemId = snatchListItemId;
    }

    public Integer getGoodsTimesId() {
        return goodsTimesId;
    }

    public void setGoodsTimesId(Integer goodsTimesId) {
        this.goodsTimesId = goodsTimesId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getBuyTimes() {
        return buyTimes;
    }

    public void setBuyTimes(Integer buyTimes) {
        this.buyTimes = buyTimes;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }
}
