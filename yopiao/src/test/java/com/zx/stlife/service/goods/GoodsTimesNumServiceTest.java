package com.zx.stlife.service.goods;

import com.base.modules.util.SimpleUtils;
import com.zx.stlife.entity.goods.GoodsInfo;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.goods.GoodsTimesNum;
import com.zx.stlife.service.sys.ConfigService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by micheal on 15/7/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
public class GoodsTimesNumServiceTest {

    private static Logger logger = LoggerFactory.getLogger(GoodsTimesNumServiceTest.class);

    @Autowired
    private GoodsTimesNumService goodsTimesNumService;
    @Autowired
    private GoodsTimesService goodsTimesService;

    //@Test
    public void testGetRadomNum(){
        List<GoodsTimesNum> goodsTimesNumList = goodsTimesNumService.findRandomNumByGoodsTimes(11, 20);
        for (GoodsTimesNum goodsTimesNum: goodsTimesNumList){
            logger.info("num: {}", goodsTimesNum.getNum());
        }
        Assert.assertEquals(20, goodsTimesNumList.size());
    }

    @Test
    public void testProduceNum(){
        Integer[] goodsTimesIds = {32, 33, 34};
        for(Integer goodsTimesId: goodsTimesIds) {
            GoodsTimes goodsTimes = goodsTimesService.get(goodsTimesId);
            goodsTimesNumService.createNums(goodsTimes);
        }
    }
}
