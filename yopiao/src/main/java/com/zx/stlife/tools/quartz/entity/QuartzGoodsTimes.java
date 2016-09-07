package com.zx.stlife.tools.quartz.entity;


import com.base.modules.util.DateUtilsEx;
import com.zx.stlife.tools.quartz.QuartzUtils;

import java.util.Date;

/**
 * Created by micheal on 15/9/10.
 */
public class QuartzGoodsTimes extends QuartzEntity {

    public QuartzGoodsTimes(Integer id, String cronExpression) {
        super( QuartzGoodsTimes.class.getName() + "@" + id, cronExpression );
    }

    public QuartzGoodsTimes(Integer id, Date execDate) {
        super( QuartzGoodsTimes.class.getName() + "@" + id,
                DateUtilsEx.formatDate(execDate, QuartzUtils.CRON_DATE_FORMAT) );
    }

}
