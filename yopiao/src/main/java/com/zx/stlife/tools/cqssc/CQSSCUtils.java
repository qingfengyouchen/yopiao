package com.zx.stlife.tools.cqssc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.base.modules.util.DateUtilsEx;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.tools.DateUtils;
import com.zx.stlife.tools.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 重庆时时彩
 * 全天120期，每天上午10:00—22:00,10分钟一期; 22:00-02:00五分钟一期
 * Created by micheal on 15/12/28.
 */
public class CQSSCUtils {

    private static Logger logger = LoggerFactory.getLogger(CQSSCUtils.class);

    private static String FORMAT_HHMM = "HHmm";

    public static Map<Integer, Integer> MAP = new LinkedHashMap<>(120);
    static {
        Calendar cal = Calendar.getInstance();
        DateUtilsEx.emptyHMS(cal);

        initMap(cal, 5, 23, 0);

        cal.set(Calendar.HOUR, 9);
        cal.set(Calendar.MINUTE, 50);
        initMap(cal, 10, 73, 23);

        initMap(cal, 5, 24, 96);
    }

    private static void initMap(Calendar cal, int rate, int len, int startIndex){
        for (int i = 0; i < len; i++){
            int key = startIndex + i + 1;
            cal.add(Calendar.MINUTE, rate);
            Integer value = SimpleUtils.stringToInteger(
                    DateUtilsEx.formatDate(cal.getTime(), FORMAT_HHMM));
            MAP.put(key, value);
        }
    }

    public static Period getLastPeriod(){
        return getPeriodByDate(DateUtilsEx.getNow());
    }

    public static Period getPeriodByDate(Date date){
        if(date == null){
            date = new Date();
        }

        Integer hhmm = SimpleUtils.stringToInteger(
                DateUtilsEx.formatDate(date, FORMAT_HHMM));
        int periodNum = 0;

        if(hhmm < MAP.get(1)){
            hhmm = MAP.get(1);
            periodNum = 1;
        }else {
            for (int i = 1; i < MAP.size(); i++) {
                int val1 = MAP.get(i);
                int val2 = MAP.get(i + 1);

                if (hhmm >= val1 && hhmm < val2) {
                    periodNum = i + 1;
                    break;
                }
            }
            hhmm = MAP.get(periodNum);
        }

        boolean isAddDay = false;
        if(periodNum == 0 || hhmm == null){
            hhmm = MAP.get(MAP.size());
            periodNum = MAP.size();
            isAddDay = true;
        }

        String periodNo = DateUtilsEx.formatDate(date, "yyMMdd") + SimpleUtils.formatNumber(periodNum, 3, "0");

        if(isAddDay){
            date = DateUtilsEx.addDays(date, 1);
        }
        Date openTime = DateUtilsEx.stringToDate(
                DateUtilsEx.formatDate(date, DateUtilsEx.FORMAT_YYYYMMDD) +
                        SimpleUtils.formatNumber(hhmm, 4, "0"), "yyyyMMddHHmm");


        return new Period(periodNo, openTime);
    }

    public static Long getLuckNum(String periodNo){
        Long luckNum = null;
        try {
            String result = HttpUtils.doGet(
                    "http://caipiao.163.com/award/getAwardNumberInfo.html?gameEn=ssc&period="
                            + periodNo, "utf-8");

            luckNum = parseJson(result);
        }catch (Exception ex){
            //ex.printStackTrace();
            logger.error("获取时时彩开奖结果失败");
        }

        return luckNum;
    }

    private static Long parseJson(String jsonStr){
        logger.info("json str: {}", jsonStr);
        Map<String, Object> map = JSON.parseObject(jsonStr, Map.class);
        JSONArray jsonArray = (JSONArray)map.get("awardNumberInfoList");
        String status = (String) map.get("status");

        Long luckNum = null;
        if(StringUtils.equals(status, "0")){
            Map<String, String> map1 = (Map<String, String>)jsonArray.get(0);
            luckNum = SimpleUtils.stringToLong(map1.get("winningNumber").replace(" ", ""));
        }
        return luckNum;
    }
}
