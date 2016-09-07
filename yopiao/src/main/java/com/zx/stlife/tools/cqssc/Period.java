package com.zx.stlife.tools.cqssc;

import java.util.Date;

/**
 * Created by micheal on 15/12/28.
 */
public class Period {

    /**期数*/
    private String periodNo;
    /**幸运号码*/
    private Integer luckNum;
    /**开奖时间*/
    private Date openTime;

    public Period(String periodNo, Date openTime) {
        this.periodNo = periodNo;
        this.openTime = openTime;
    }

    public String getPeriodNo() {
        return periodNo;
    }

    public void setPeriodNo(String periodNo) {
        this.periodNo = periodNo;
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
}
