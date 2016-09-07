/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.base.modules.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author micheal cao
 * @version 2013-3-15
 */
public class DateUtilsEx extends DateUtils {
	public static final String FORMAT_YYYYMMDD = "yyyyMMdd";

	public static final String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	public static final String FORMAT_YYYYMMDDHHMMSS_SSS = "yyyyMMddHHmmssSSS";

	public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = FORMAT_YYYY_MM_DD_HH_MM + ":ss";

	public static final String FORMAT_MMDDHHMMSS = "MMddHHmmss";

	public static Date getNow(){
		return new Date();
	}

	public static String getYYYYMMDD(Date date){
		return SimpleUtils.dateToString(date, FORMAT_YYYYMMDD);
	}

	private static String[] parsePatterns = {
			"yyyy-MM-dd",
			"yyyy-MM-dd HH:mm",
			"yyyy-MM-dd HH:mm:ss",
			"yyyy-MM-dd HH:mm:ss.SSS",
			"yyyy/MM/dd",
			"yyyy/MM/dd HH:mm",
			"yyyy/MM/dd HH:mm:ss",
			"yyyy/MM/dd HH:mm:ss.SSS",
			"yyyyMMdd"/*,
			"yyyyMMddHHmm",
			"yyyyMMddHHmmss",
			"yyyyMMddHHmmssSSS"*/
	};

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		if(date == null)
			return null;

		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static int getYear() {
		return SimpleUtils.stringToInteger(formatDate(new Date(), "yyyy"));
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date parseDate(Object str, Date defaultDate){
		Date date = parseDate(str);
		return date == null ? defaultDate : date;
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	public static Date stringToDate(String dateStr, String pattern){
		return SimpleUtils.stringToDate(dateStr, null, pattern);
	}

	/**
	 * 获取小时，0 - 23
	 * @param date
	 * @return
	 */
	public static int getHour(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取分钟，0 - 59
	 * @param date
	 * @return
	 */
	public static int getMinute(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MINUTE);
	}

	/**
	 * 获得当前时间MMddHHmmss格式的字符串
	 * @return
	 */
	public static String getCurrentMMDDHHMMSS(){
		return SimpleUtils.dateToString(getNow(), FORMAT_MMDDHHMMSS);
	}

	public static String getYYYYMMDDHHMMSS(Date date){
		return  SimpleUtils.dateToString(date, FORMAT_YYYYMMDDHHMMSS);
	}

	public static Date toDateFromYYYYMMDDHHMMSS(String dateStr, Date defaultDate){
		return SimpleUtils.stringToDate(dateStr, defaultDate, FORMAT_YYYYMMDDHHMMSS);
	}

	/**
	 * 根据出生日期获取年龄
	 *
	 * @param date
	 * @return
	 */
	public static long getAgeByBirthday(Date date) {
		if (date == null)
			return 0;

		long birth = date.getTime();
		Date today = new Date();
		long now = today.getTime();
		long surplus = now - birth;
		return surplus / 1000 / 60 / 60 / 24 / 365;
	}

	/**
	 * 功能描述：取得指定月份的的第一天
	 *
	 * @param date
	 *            String 字符型日期
	 * @return date
	 */
	public static Date getFirstDayOfMonth(Date date) {
		return parseDate(formatDate(date, "yyyy-MM" + "-01" ));
	}

	/**
	 * 功能描述：取得指定月份的最后一天
	 *
	 * @param date
	 *            String 字符型日期
	 * @return date
	 */
	public static Date getLastDayOfMonth(Date date) {
		date = getFirstDayOfMonth(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return calendar.getTime();
	}

	/**
	 * 获得俩日期之间的周数（星期）
	 * @param lower
	 * @param upper
	 * @param isSundayAaFirstOfWeek 是否使用星期日作为一周的第一天，false：表示星期一作为一周中的第一天
	 * @return 周数
	 */
	public static int getBetweenWeeks(Date lower, Date upper, boolean isSundayAaFirstOfWeek){
		Calendar cal = Calendar.getInstance();
		cal.setTime(lower);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int days = getBetweenDays(lower, upper)+1;
		int weekAmount =  0;

		if( !isSundayAaFirstOfWeek ){
			if(dayOfWeek == 1)
				dayOfWeek = 7;
			else
				dayOfWeek--;
		}
		days = days - ( 7 - dayOfWeek + 1 );
		weekAmount =  days / 7;
		if(days % 7 != 0 )
			weekAmount ++;

		weekAmount ++;
		return weekAmount;
	}

	/**
	 * 计算两日期之间的天数
	 * @param lower
	 * @param upper
	 * @return
	 * @throws ParseException
	 */
	public static int getBetweenDays(Date lower, Date upper ){
		Calendar calst = Calendar.getInstance();
		Calendar caled = Calendar.getInstance();
		calst.setTime(lower);
		caled.setTime(upper);
		//设置时间为0时
		emptyHMS(calst);
		emptyHMS(caled);
		//得到两个日期相差的天数
		int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst
				.getTime().getTime() / 1000)) / 3600 / 24;

		return days;
	}

	/**
	 * 计算两日期之间的秒数
	 * @param lower
	 * @param upper
	 * @return
	 * @throws ParseException
	 */
	public static long getBetweenSecond(Date lower, Date upper ){
		Calendar calst = Calendar.getInstance();
		Calendar caled = Calendar.getInstance();
		calst.setTime(lower);
		caled.setTime(upper);

		//得到两个日期相差的天数
		long second = (caled.getTime().getTime() / 1000)
				- (calst.getTime().getTime() / 1000);// / 3600 / 24;

		return second;
	}

	/**
	 * 计算两日期之间的分钟
	 * @param lower
	 * @param upper
	 * @return
	 * @throws ParseException
	 */
	public static long getBetweenMinute(Date lower, Date upper ){
		return getBetweenSecond(lower, upper) / 60;
	}

	/**
	 * 获取时间范围内的周信息
	 * @param lower
	 * @param upper
	 * @param isSundayAaFirstOfWeek 是否采用星期天作为一周的第一天，否则为星期一
	 * @return
	 */
	public static List<Date[]> getBetweenWeekDates(Date lower, Date upper,
												   boolean isSundayAaFirstOfWeek){
		List<Date[]> list = new ArrayList<>();
		int weekAmount = getBetweenWeeks(lower, upper, isSundayAaFirstOfWeek);

		Calendar temCal = Calendar.getInstance();
		for (int i = 0; i < weekAmount; i++) {
			Date[] dates = new Date[2];
			if(i==0){
				temCal.setTime(lower);
				int dayOfWeek = temCal.get(Calendar.DAY_OF_WEEK);

				temCal.setTime(lower);
				if( !isSundayAaFirstOfWeek ){
					if(dayOfWeek == 1)
						dayOfWeek = 7;
					else
						dayOfWeek--;
				}

				temCal.add(Calendar.DAY_OF_MONTH, ( 7 - dayOfWeek));
				dates[0] = lower;
				dates[1] = temCal.getTime();
			}else{
				temCal.add(Calendar.DAY_OF_MONTH, 1);
				dates[0] = temCal.getTime();
				temCal.add(Calendar.DAY_OF_MONTH, 6);
				dates[1] = temCal.getTime();
			}

			list.add(dates);
		}
		return list;
	}

	/**
	 * 获得两日期的月数
	 * @param lower
	 * @param upper
	 * @return
	 */
	public static int getBetweenMonths(Date lower, Date upper){
		if(lower == null || upper == null)
			return -1;

		Calendar calLower = Calendar.getInstance();
		Calendar calUpper = Calendar.getInstance();
		calLower.setTime(lower);
		calUpper.setTime(upper);

		int month = 12*(calUpper.get(Calendar.YEAR) - calLower.get(Calendar.YEAR)) + calUpper.get(Calendar.MONTH) - calLower.get(Calendar.MONTH);
		return month;
	}

	public static Date emptyHMS(Date date){
		return parseDate(formatDate(date, FORMAT_YYYYMMDD));
	}

	public static Calendar emptySS_SSS(Calendar cal){
		if(cal == null)
			return null;

		cal.setTime(emptySS_SSS(cal.getTime()));
		return cal;
	}

	public static Date emptySS_SSS(Date date){
		return parseDate(formatDate(date, FORMAT_YYYY_MM_DD_HH_MM));
	}

	public static Calendar emptyHMS(Calendar cal){
		if(cal == null)
			return null;

		cal.setTime(emptyHMS(cal.getTime()));
		return cal;
	}

	/**
	 * 格式化时间
	 * **秒前, **分钟前, **小时前, 昨天 HH:mm:ss, 前天 HH:mm:ss, yyyy-MM-dd HH:mm:ss
	 * @param lower
	 * @param upper
	 * @return
	 */
	public static String formatDate(Date lower, Date upper){
		int SECOND_OF_1M = 60,//一分钟的秒数
				SECOND_OF_1H = 3600,//1小时的秒数
				SECOND_OF_1DAY = 86400,//一天的秒数
				SECOND_OF_2DAY = 172800,//两天的秒数
				SECOND_OF_3DAY = 259200;//三天的秒数
		String format1 = "HH:mm:ss";
		String format2 = "yyyy-MM-dd HH:mm:ss";
		String result = null;

		long second = getBetweenSecond(lower, upper);
		if( second < SECOND_OF_1M ){// **秒前
			result = String.format("%s秒前", second);
		}else if( second <  SECOND_OF_1H){//**分钟前
			result = String.format("%s分钟前", second / SECOND_OF_1M);
		}else if( second < SECOND_OF_1DAY ){//**小时前
			result = String.format("%s小时前", second / SECOND_OF_1H);
		}else if( second < SECOND_OF_2DAY ){//昨天 HH:mm:ss
			result = String.format("昨天 %s", SimpleUtils.dateToString(lower, format1));
		}else if( second < SECOND_OF_3DAY ){//前天 HH:mm:ss
			result = String.format("前天 %s", SimpleUtils.dateToString(lower, format1));
		}else{//yyyy-MM-dd HH:mm:ss
			result = SimpleUtils.dateToString(lower, format2);
		}

		return result;
	}

	/***
	 * 获得给定日期的前N天日期
	 * @param date 给定的日期
	 * @param beforeDays 前N天的
	 * @return
	 */
	public static String getBeforeNowDate(Date date, int beforeDays){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day=c.get(Calendar.DATE);
		c.set(Calendar.DATE,day-beforeDays);

		String dayBefore=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}

	/**
	 * 获取指定时间所在月份的总天数
	 * @return
	 */
	public static int getDaysOfMonth(Date date){
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(date);
		int days = calendar.getActualMaximum(Calendar.DATE);
		return days;
	}
}
