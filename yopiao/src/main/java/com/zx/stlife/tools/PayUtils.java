package com.zx.stlife.tools;

import com.base.modules.util.DateUtilsEx;

import java.util.Date;

/**
 * Created by micheal on 16/1/2.
 */
public class PayUtils {

    public static Date getExpireDate(){
        Date now = DateUtilsEx.getNow();
        Date expireDate = DateUtilsEx.addMinutes(now, 10);// 10分钟后失效
        return expireDate;
    }

    public static String getQueryJobCronExpression(){
        int m = DateUtilsEx.addSeconds(DateUtils.getNow(), 27).getSeconds();
        if(m >= 30){
            m -= 30;
        }
        return m + "/27 * * * * ?";
    }
}
