package com.zx.stlife.service.snatch;

import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.order.SnatchNum;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.order.SnatchNumService;
import com.zx.stlife.tools.RandomUitls;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import scala.Int;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
public class SnatchNumServiceTest {
    @Autowired
    private GoodsTimesService goodsTimesService;
    @Autowired
    private SnatchNumService snatchNumService;

    private static Logger logger = LoggerFactory.getLogger(SnatchNumServiceTest.class);

    @Test
    public void testRandomSnatchNum(){
        SnatchNum snatchNum = snatchNumService.getRandom1ByGoodsTimes(11);
        logger.info("snatch num: {}, ", snatchNum.getNum());
    }

    @Test
    public void testGetRandom1NumByGoodsTimes(){
        Integer num = snatchNumService.getRandom1NumByGoodsTimes(73, 3);
        logger.info("snatch num: {}, ", num);
    }
}
