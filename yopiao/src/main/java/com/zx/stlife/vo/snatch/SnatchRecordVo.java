package com.zx.stlife.vo.snatch;

import com.zx.stlife.tools.DateUtils;

import java.util.Date;

/**
 * Created by micheal on 15/12/12.
 */
public class SnatchRecordVo {
    /**商品名称*/
    private String goodsName;
    /**商品缩略图*/
    private String thumbnail;
    /**夺宝记录id*/
    private Integer snatchRecordId;
    /**商品期号id*/
    private Integer goodsTimesId;
    /**
     * 是否10元夺宝；当为false时，表示1元夺宝
     */
    private Boolean isTenYuan;
    /**状态，类型, 1-进行中， 2-待开奖，3-已揭晓 */
    private Byte state;
    /**参与期号*/
    private Integer goodsTimesName;
    /**总需人次*/
    private Integer totalTimes;
    /**已购买人次*/
    private Integer totalBuyTimes;
    /**夺宝进度*/
    private Integer snatchProgress;
    /**用户购买人次*/
    private Integer buyTimes;
    /**获奖者id*/
    private Integer winngUserId;
    /**获奖者昵称*/
    private String winngUserName;
    /**获奖者本期参与人次*/
    private Integer winngUserBuyTimes;
    /**幸运号码*/
    private Integer luckNum;
    /**揭晓时间*/
    private Date openTime;
    /**揭晓时间字符串*/
    private String openTimeStr;

    public SnatchRecordVo(String goodsName, String thumbnail, Boolean isTenYuan,
                          Integer snatchRecordId, Integer goodsTimesId, Byte state,
                          Integer goodsTimesName, Integer totalTimes, Integer totalBuyTimes,
                          Integer snatchProgress, Integer buyTimes, Integer winngUserId, String winngUserName,
                          Integer winngUserBuyTimes, Integer luckNum, Date openTime) {
        this.goodsName = goodsName;
        this.thumbnail = thumbnail;
        this.isTenYuan = isTenYuan;
        this.snatchRecordId = snatchRecordId;
        this.goodsTimesId = goodsTimesId;
        this.state = state;
        this.goodsTimesName = goodsTimesName;
        this.totalTimes = totalTimes;
        this.totalBuyTimes = totalBuyTimes;
        this.snatchProgress = snatchProgress;
        this.buyTimes = buyTimes;
        this.winngUserId = winngUserId;
        this.winngUserName = winngUserName;
        this.winngUserBuyTimes = winngUserBuyTimes;
        this.luckNum = luckNum;
        this.openTime = openTime;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Boolean getIsTenYuan() {
        return isTenYuan;
    }

    public void setIsTenYuan(Boolean isTenYuan) {
        this.isTenYuan = isTenYuan;
    }

    public Integer getSnatchRecordId() {
        return snatchRecordId;
    }

    public void setSnatchRecordId(Integer snatchRecordId) {
        this.snatchRecordId = snatchRecordId;
    }

    public Integer getGoodsTimesId() {
        return goodsTimesId;
    }

    public void setGoodsTimesId(Integer goodsTimesId) {
        this.goodsTimesId = goodsTimesId;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Integer getGoodsTimesName() {
        return goodsTimesName;
    }

    public void setGoodsTimesName(Integer goodsTimesName) {
        this.goodsTimesName = goodsTimesName;
    }

    public Integer getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(Integer totalTimes) {
        this.totalTimes = totalTimes;
    }

    public Integer getTotalBuyTimes() {
        return totalBuyTimes;
    }

    public void setTotalBuyTimes(Integer totalBuyTimes) {
        this.totalBuyTimes = totalBuyTimes;
    }

    public Integer getWinngUserId() {
        return winngUserId;
    }

    public void setWinngUserId(Integer winngUserId) {
        this.winngUserId = winngUserId;
    }

    public String getWinngUserName() {
        return winngUserName;
    }

    public void setWinngUserName(String winngUserName) {
        this.winngUserName = winngUserName;
    }

    public Integer getBuyTimes() {
        return buyTimes;
    }

    public void setBuyTimes(Integer buyTimes) {
        this.buyTimes = buyTimes;
    }

    public Integer getLuckNum() {
        return luckNum;
    }

    public void setLuckNum(Integer luckNum) {
        this.luckNum = luckNum;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public String getOpenTimeStr() {
        if(openTimeStr==null&&getOpenTime()!=null){
            openTimeStr= DateUtils.dateToYYYYMMDDHHMMSSSSSString(getOpenTime());
        }
        return openTimeStr;
    }

    public void setOpenTimeStr(String openTimeStr) {
        this.openTimeStr = openTimeStr;
    }

    public Integer getWinngUserBuyTimes() {
        return winngUserBuyTimes;
    }

    public void setWinngUserBuyTimes(Integer winngUserBuyTimes) {
        this.winngUserBuyTimes = winngUserBuyTimes;
    }

    public Integer getSnatchProgress() {
        return snatchProgress;
    }

    public void setSnatchProgress(Integer snatchProgress) {
        this.snatchProgress = snatchProgress;
    }
}
