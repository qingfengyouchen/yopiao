package com.zx.stlife.tools;

import java.util.Calendar;
import java.util.Date;

import com.base.modules.util.DateUtilsEx;
import com.base.modules.util.Exceptions;
import com.base.modules.util.SimpleUtils;

/**
 * Created by micheal on 15/8/11.
 */
public class DateUtils extends DateUtilsEx{

    /**
     * 日期转yyyyMMddHHmmssSSS格式的字符串
     * @param date
     * @return
     */
    public static String dateToYYYYMMDDHHMMSSSSSString(Date date){
        return DateUtilsEx.formatDate(date,
                DateUtilsEx.FORMAT_YYYYMMDDHHMMSS_SSS);
    }

    /**
     * 当前时间
     * @param date
     * @return
     */
    public static String getNowYYYYMMDDHHMMSSSSSString(){
        return DateUtils.dateToYYYYMMDDHHMMSSSSSString(DateUtils.getNow());
    }

    /**
     * 毫秒转yyyyMMddHHmmssSSS格式的字符串
     * @param time
     * @return
     */
    public static String millisecondsToYYYYMMDDHHMMSSSSSString(Long time){
        if( null == time )
            return null;

        Date date = new Date(time);
        return dateToYYYYMMDDHHMMSSSSSString(date);
    }



    public static String millisecondsToYYYY_MM_DD_HH_MM_SS_SSSString(Long time){
        if( null == time )
            return null;

        Date date = new Date(time);
        return DateUtilsEx.formatDate(date, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    public static Integer millisecondsToHHMMSSSSS(Long time){
        if( null == time )
            return null;

        Date date = new Date(time);
        return SimpleUtils.stringToInteger(
                DateUtilsEx.formatDate(date, "HHmmssSSS"));
    }

    /**
     * yyyyMMddHHmmssSSS格式的字符串转日期
     * @param str
     * @return
     */
    public static Date YYYYMMDDHHMMSSSSSStringToDate(String str){
        //return DateUtilsEx.parseDate(str, DateUtilsEx.getNow());
        Date date = DateUtilsEx.stringToDate(str, DateUtilsEx.FORMAT_YYYYMMDDHHMMSS_SSS);
        return date == null ? DateUtilsEx.getNow() : date;
    }
    
    /**
     * 重置日期
     * 
     * @param date 日期
     * @param fieldAndValueArray 需设置的数组，长度为2，元素1表示field,元素2表示value
     * @return
     */
    public static Date resetDateFields(Date date, int[]... fieldAndValueArray){
    	date = (date == null) ? DateUtilsEx.getNow() : date;
    	try {
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(date);
    		for (int[] array : fieldAndValueArray) {
    			if (array.length == 2) {
    				calendar.set(array[0], array[1]);
    			}
			}
    		date = calendar.getTime();
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
    	return date;
    }
    
}
