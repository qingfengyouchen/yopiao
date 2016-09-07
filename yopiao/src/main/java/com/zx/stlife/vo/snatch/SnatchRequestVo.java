package com.zx.stlife.vo.snatch;

import java.util.ArrayList;
import java.util.List;

/**
 * 夺宝下单或支付请求参数
 * Created by micheal on 15/12/22.
 */
public class SnatchRequestVo {

    /**用户id*/
    private Integer userId;
    /**夺宝商品列表*/
    private List<SnatchVo> snatchList;
    /**付款方式结合*/
    private List<Byte> payWays;
    /**红包id集合*/
    private List<Integer> redPackIds;
    /**第三方付款金额*/
    private Integer thirdpartMoney;
    /**是否使用支付宝H5支付*/
    private Boolean isAlipayWithH5;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<SnatchVo> getSnatchList() {
        return snatchList;
    }

    public void setSnatchList(List<SnatchVo> snatchList) {
        this.snatchList = snatchList;
    }

    public List<Byte> getPayWays() {
        return payWays;
    }

    public void setPayWays(List<Byte> payWays) {
        this.payWays = payWays;
    }

    public List<Integer> getRedPackIds() {
        return redPackIds;
    }

    public void setRedPackIds(List<Integer> redPackIds) {
        this.redPackIds = redPackIds;
    }

    public Integer getThirdpartMoney() {
        return thirdpartMoney;
    }

    public void setThirdpartMoney(Integer thirdpartMoney) {
        this.thirdpartMoney = thirdpartMoney;
    }

    public Boolean getIsAlipayWithH5() {
        if(isAlipayWithH5 == null)
            isAlipayWithH5 = false;
        return isAlipayWithH5;
    }

    public void setIsAlipayWithH5(Boolean isAlipayWithH5) {
        this.isAlipayWithH5 = isAlipayWithH5;
    }

    public List<Integer> getGoodsTimesIds(){
        if(getSnatchList() == null) {
            return null;
        }

        List<Integer> goodsTimesIds = new ArrayList<>(snatchList.size());
        for (SnatchVo snatchVo: snatchList){
            goodsTimesIds.add(snatchVo.getGid());
        }

        return goodsTimesIds;
    }
}
