package com.zx.stlife.service.snatch;

import com.zx.stlife.entity.order.SnatchNum;
import com.zx.stlife.entity.record.BuyRecord;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.order.SnatchNumService;
import com.zx.stlife.service.record.BuyRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
public class BuyRecordServiceTest {
    @Autowired
    private BuyRecordService buyRecordService;

    private static Logger logger = LoggerFactory.getLogger(BuyRecordServiceTest.class);

    @Test
    public void testSumTimeValue(){
        Long amount = buyRecordService.sumTimeValue(2);
        logger.info("sum num{}", amount);
    }

    @Test
    public void testGetRandom1ByGoodsTimes(){
        BuyRecord buyRecord = buyRecordService.getRandom1ByGoodsTimes(73);
        logger.info("{}, {}", buyRecord.getSnatchTime(), buyRecord.getTimeValue());
    }
}
