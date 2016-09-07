package com.zx.stlife.service.goods;

import com.base.modules.util.SimpleUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.goods.GoodsInfo;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.service.sys.ConfigService;
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
public class GoodsTimesServiceTest {

    private static Logger logger = LoggerFactory.getLogger(GoodsTimesServiceTest.class);

    @Autowired
    private GoodsTimesService goodsTimesService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private GoodsTimesNumService goodsTimesNumService;

    @Test
    public void createAllNotSalesGoods(){
        List<GoodsInfo> goodsList = goodsService.findByNotSales();
        if(SimpleUtils.isNotNullList(goodsList)){
            for(GoodsInfo goodsInfo : goodsList){
                logger.info("goods name: {}", goodsInfo.getName());
                // 生成商品期号实体
                GoodsTimes goodsTimes = new GoodsTimes(
                        configService.getGoodsTimesNo(), goodsInfo, goodsInfo.getName(),
                        goodsInfo.getTip(), goodsInfo.getThumbnail(), goodsInfo.getIsTenYuan(),
                        goodsInfo.getTotalTimes(), 0, 0);
                goodsTimesService.save(goodsTimes);

                goodsTimesNumService.createNums(goodsTimes);
            }
        }
        System.out.println("over...");
    }

    @Test
    public void testCreateHtml(){
        List<GoodsTimes> goodsTimesList = goodsTimesService.findByState(Const.GoodsTimesState.OVER);
        for(GoodsTimes goodsTimes: goodsTimesList) {
            goodsTimesService.createHtml(goodsTimes);
        }
    }
}
