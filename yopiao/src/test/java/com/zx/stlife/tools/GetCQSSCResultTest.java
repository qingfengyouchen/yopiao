package com.zx.stlife.tools;

import com.base.modules.util.DateUtilsEx;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.tools.cqssc.CQSSCUtils;
import com.zx.stlife.tools.cqssc.Period;
import com.zx.stlife.tools.quartz.QuartzUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 全天120期，每天上午10:00—22:00,10分钟一期; 22:00-02:00五分钟一期
 * Created by micheal on 15/12/27.
 */
public class GetCQSSCResultTest {

    private static Logger logger = LoggerFactory.getLogger(GetCQSSCResultTest.class);
    @Test
    public void getCQSSCResult(){
        Period period = CQSSCUtils.getLastPeriod();
        logger.info("period: {}", ToStringBuilder.reflectionToString(period));
        logger.info("luckNum: {}", CQSSCUtils.getLuckNum(period.getPeriodNo()));
    }

    @Test
    public void testGetPeriodByDate(){
        Period period = CQSSCUtils.getPeriodByDate(DateUtilsEx.parseDate("2015-12-31 23:59:00"));
        logger.info(ToStringBuilder.reflectionToString(period));

        period = CQSSCUtils.getPeriodByDate(DateUtilsEx.parseDate("2015-12-31 00:00:30"));
        logger.info(ToStringBuilder.reflectionToString(period));

        period = CQSSCUtils.getPeriodByDate(DateUtilsEx.parseDate("2015-12-31 00:01:30"));
        logger.info(ToStringBuilder.reflectionToString(period));

        period = CQSSCUtils.getPeriodByDate(DateUtilsEx.parseDate("2015-12-31 01:54:00"));
        logger.info(ToStringBuilder.reflectionToString(period));

        period = CQSSCUtils.getPeriodByDate(DateUtilsEx.parseDate("2015-12-31 01:55:00"));
        logger.info(ToStringBuilder.reflectionToString(period));

        period = CQSSCUtils.getPeriodByDate(DateUtilsEx.parseDate("2015-12-31 10:00:00"));
        logger.info(ToStringBuilder.reflectionToString(period));

        period = CQSSCUtils.getPeriodByDate(DateUtilsEx.parseDate("2015-12-31 12:00:00"));
        logger.info(ToStringBuilder.reflectionToString(period));

        /*for(Map.Entry<Integer, Integer> entry : CQSSCUtils.MAP.entrySet()){
            logger.info("{}: {}",
                    SimpleUtils.formatNumber(entry.getKey(), 3, "0"),
                    SimpleUtils.formatNumber(entry.getValue(), 4, "0"));
        }*/

    }

}
