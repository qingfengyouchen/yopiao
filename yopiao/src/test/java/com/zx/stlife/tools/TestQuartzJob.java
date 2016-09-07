package com.zx.stlife.tools;

import com.zx.stlife.tools.quartz.QuartzUtils;
import com.base.modules.test.spring.SpringTransactionalTestCase;
import com.base.modules.util.DateUtilsEx;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created by micheal on 15/9/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
public class TestQuartzJob extends SpringTransactionalTestCase {

    @Test
    public void testAdd(){
        Date date = DateUtilsEx.addDays(DateUtilsEx.getNow(), 1);
        String cron = DateUtilsEx.formatDate(date, QuartzUtils.CRON_DATE_FORMAT);
       // QuartzUtils.registerJob(new QuartzSalesHouse("aaa", cron), JobSalesHouse.class, null);
    }


}
