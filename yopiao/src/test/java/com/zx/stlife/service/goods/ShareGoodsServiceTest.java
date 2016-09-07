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
public class ShareGoodsServiceTest {

    private static Logger logger = LoggerFactory.getLogger(ShareGoodsServiceTest.class);

    @Autowired
    private ShareGoodsService shareGoodsService;

    @Test
    public void testGetFirstImagePath(){
        logger.info(shareGoodsService.getFirstImagePath(26));
        logger.info("over...");
    }

}
