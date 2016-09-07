package com.zx.stlife.tools.quartz.entity;


import com.base.modules.util.DateUtilsEx;
import com.zx.stlife.tools.quartz.QuartzUtils;

import java.util.Date;

/**
 * Created by micheal on 15/9/10.
 */
public class QuartzPayRecord extends QuartzEntity {

    public QuartzPayRecord(String outTradeNo, String cronExpression) {
        super( outTradeNo, cronExpression );
    }

    public QuartzPayRecord(String outTradeNo, Date execDate) {
        super( outTradeNo, DateUtilsEx.formatDate(execDate, QuartzUtils.CRON_DATE_FORMAT) );
    }

}
