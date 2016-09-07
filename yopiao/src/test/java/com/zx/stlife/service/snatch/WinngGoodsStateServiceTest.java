package com.zx.stlife.service.snatch;

import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.order.WinngGoodsStateService;
import com.zx.stlife.service.record.BuyRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
public class WinngGoodsStateServiceTest {
    @Autowired
    private WinngGoodsStateService winngGoodsStateService;
    @Autowired
    private GoodsTimesService goodsTimesService;

    private static Logger logger = LoggerFactory.getLogger(WinngGoodsStateServiceTest.class);

    @Test
    public void testInitWinngStates(){
        List<GoodsTimes> goodsTimesList = goodsTimesService.findByState(Const.GoodsTimesState.OVER);
        if(goodsTimesList != null){
            for(GoodsTimes goodsTimes : goodsTimesList){
                winngGoodsStateService.initWinngStates(goodsTimes);
            }
        }
    }

}
