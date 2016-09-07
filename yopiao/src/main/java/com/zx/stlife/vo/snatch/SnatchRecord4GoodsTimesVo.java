package com.zx.stlife.vo.snatch;

import com.zx.stlife.tools.DateUtils;

import java.util.Date;

/**
 * Created by micheal on 15/12/12.
 */
public class SnatchRecord4GoodsTimesVo {
    /**用户id*/
    private Integer userId;
    /**用户名*/
    private String userName;
    /**手机号码*/
    private String mobileNo;
    /**用户昵称*/
    private String nickName;
    /**购买人次*/
    private Integer buyTimes;

    public SnatchRecord4GoodsTimesVo() {
    }

    public SnatchRecord4GoodsTimesVo(
            Integer userId, String userName, String mobileNo, String nickName, Integer buyTimes) {
        this.userId = userId;
        this.userName = userName;
        this.mobileNo = mobileNo;
        this.nickName = nickName;
        this.buyTimes = buyTimes;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getBuyTimes() {
        return buyTimes;
    }

    public void setBuyTimes(Integer buyTimes) {
        this.buyTimes = buyTimes;
    }
}
