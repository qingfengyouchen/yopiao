package com.zx.stlife.service.snatch;

import com.base.modules.util.DateUtilsEx;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.goods.GoodsInfo;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.goods.GoodsTimesNum;
import com.zx.stlife.entity.order.SnatchNum;
import com.zx.stlife.entity.order.SnatchRecord;
import com.zx.stlife.entity.order.SnatchRecordDetail;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.goods.GoodsService;
import com.zx.stlife.service.goods.GoodsTimesNumService;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.member.MemberService;
import com.zx.stlife.service.order.SnatchNumService;
import com.zx.stlife.service.order.SnatchRecordDetailService;
import com.zx.stlife.service.order.SnatchRecordService;
import com.zx.stlife.service.sys.AccountService;
import com.zx.stlife.service.sys.ConfigService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import scala.Int;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by micheal on 15/7/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
public class SnatchRecordServiceTest {
    @Autowired
    private GoodsTimesService goodsTimesService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private SnatchRecordService snatchRecordService;
    @Autowired
    private SnatchRecordDetailService snatchRecordDetailService;
    @Autowired
    private GoodsTimesNumService goodsTimesNumService;
    @Autowired
    private SnatchNumService snatchNumService;

    private static Logger logger = LoggerFactory
            .getLogger(SnatchRecordServiceTest.class);

    //@Test
    public void createAndFullAll(){
        GoodsTimes goodsTimes = goodsTimesService.get(4); // The new Macbook 12英寸 标配 256G闪存
        List<User> userList = accountService.findByVirtualUser();
        Random random = new Random();
        int totalTimes = goodsTimes.getTotalTimes();
        int remainderTimes = totalTimes; // 剩余次数
        int maxTimes = goodsTimes.getIsTenYuan() ? 10 : 100;
        GoodsInfo goodsInfo = goodsTimes.getGoodsInfo();

        List<Integer> numList = goodsTimesNumService.findNumByGoodsTimes(goodsTimes.getId());

        while (remainderTimes > 0){
            int buyTimes = random.nextInt(maxTimes) + 1;
            if(goodsTimes.getIsTenYuan()){
                buyTimes *= 10;
            }
            if(buyTimes > remainderTimes){
                buyTimes = remainderTimes;
            }
            remainderTimes -= buyTimes;

            User user = userList.get( random.nextInt(userList.size()) );
            SnatchRecord snatchRecord = snatchRecordService.findByUserAndGoodsTimes(user.getId(), goodsTimes.getId());
            if(snatchRecord == null) {
                snatchRecord = new SnatchRecord(goodsTimes.getGoodsName(), goodsTimes, goodsTimes.getTimes(),
                        goodsTimes.getTotalTimes(), buyTimes, buyTimes, user, Const.PayResult.PAY_SUCCESS);
            }else{
                snatchRecord.setBuyTimes(snatchRecord.getBuyTimes() + buyTimes);
                snatchRecord.setMoney(snatchRecord.getMoney() + buyTimes);
            }
            snatchRecordService.save(snatchRecord);

            SnatchRecordDetail snatchRecordDetail = new SnatchRecordDetail(null, goodsInfo, goodsTimes,
                    buyTimes, buyTimes, user, memberService.getHeadImgByUser(user.getId()),
                    user.getNickName(), "中国", null, snatchRecord, Const.PayResult.PAY_SUCCESS);
            snatchRecordDetailService.save(snatchRecordDetail);

            for(int i = 0; i < buyTimes; i ++){
                /*GoodsTimesNum goodsTimesNum = goodsTimesNumService
                        .getRandmonByGoodsTimes(goodsTimes);*/
                int index = random.nextInt(numList.size());
                int num = numList.get(index);
                GoodsTimesNum goodsTimesNum = goodsTimesNumService.getByGoodsTimesAndNum(goodsTimes.getId(), num);

                SnatchNum snatchNum = new SnatchNum(goodsTimesNum.getNum(), snatchRecordDetail, goodsTimes, user);
                snatchNumService.save(snatchNum);
                goodsTimesNumService.delete(goodsTimesNum);
                numList.remove(index);
            }
        }

        // 模拟中奖
        int luckNum = Const.SNATCH_BASE_NUM + random.nextInt(totalTimes);
        User user = accountService.getByGoodsTimesAndNum(goodsTimes.getId(), luckNum);

        goodsTimes.setState(Const.GoodsTimesState.OVER);
        goodsTimes.setTotalBuyTimes(totalTimes);
        goodsTimes.setSnatchProgress(100);
        goodsTimes.setLuckNum(luckNum);
        goodsTimes.setOpenTime(new Date());
        goodsTimes.setWinngUser(user);
        goodsTimes.setWinngUserIdentity(user.getUserName());
        goodsTimes.setWinngUserName(user.getNickName());
        goodsTimes.setWinngUserHeadImg(memberService.getHeadImgByUser(user.getId()));
        goodsTimes.setWinngUserBuyTimes(snatchRecordService
                .getBuyTimesByGoodsTimesAndUser(goodsTimes.getId(), user.getId()));
        goodsTimes.setWinngState(Const.WinngState.CONFIRM_ADDRESS);
        goodsTimesService.save(goodsTimes);
    }

    //@Test
    public void createAndFullPart(){
        //Integer[] goodsTimesIds = goodsService.findByNotSales();
        GoodsTimes goodsTimes = goodsTimesService.get(4); // The new Macbook 12英寸 标配 256G闪存
        List<User> userList = accountService.findByVirtualUser();
        Random random = new Random();
        int totalTimes = goodsTimes.getTotalTimes();
        int remainderTimes = totalTimes;
        int maxTimes = goodsTimes.getIsTenYuan() ? 10 : 100;
        GoodsInfo goodsInfo = goodsTimes.getGoodsInfo();

        List<Integer> numList = goodsTimesNumService.findNumByGoodsTimes(goodsTimes.getId());

        while (remainderTimes > 0){
            int buyTimes = random.nextInt(maxTimes) + 1;
            if(goodsTimes.getIsTenYuan()){
                buyTimes *= 10;
            }
            if(buyTimes > remainderTimes){
                buyTimes = remainderTimes;
            }
            remainderTimes -= buyTimes;

            User user = userList.get( random.nextInt(userList.size()) );
            SnatchRecord snatchRecord = snatchRecordService.findByUserAndGoodsTimes(user.getId(), goodsTimes.getId());
            if(snatchRecord == null) {
                snatchRecord = new SnatchRecord(goodsTimes.getGoodsName(), goodsTimes, goodsTimes.getTimes(),
                        goodsTimes.getTotalTimes(), buyTimes, buyTimes, user, Const.PayResult.PAY_SUCCESS);
            }else{
                snatchRecord.setBuyTimes(snatchRecord.getBuyTimes() + buyTimes);
                snatchRecord.setMoney(snatchRecord.getMoney() + buyTimes);
            }
            snatchRecordService.save(snatchRecord);

            SnatchRecordDetail snatchRecordDetail = new SnatchRecordDetail(null, goodsInfo, goodsTimes,
                    buyTimes, buyTimes, user, memberService.getHeadImgByUser(user.getId()),
                    user.getNickName(), "中国", null, snatchRecord, Const.PayResult.PAY_SUCCESS);
            snatchRecordDetailService.save(snatchRecordDetail);

            for(int i = 0; i < buyTimes; i ++){
                /*GoodsTimesNum goodsTimesNum = goodsTimesNumService
                        .getRandmonByGoodsTimes(goodsTimes);*/
                int index = random.nextInt(numList.size());
                int num = numList.get(index);
                GoodsTimesNum goodsTimesNum = goodsTimesNumService.getByGoodsTimesAndNum(goodsTimes.getId(), num);

                SnatchNum snatchNum = new SnatchNum(goodsTimesNum.getNum(), snatchRecordDetail, goodsTimes, user);
                snatchNumService.save(snatchNum);
                goodsTimesNumService.delete(goodsTimesNum);
                numList.remove(index);
            }
        }

        // 模拟中奖
        int luckNum = Const.SNATCH_BASE_NUM + random.nextInt(totalTimes);
        User user = accountService.getByGoodsTimesAndNum(goodsTimes.getId(), luckNum);

        goodsTimes.setState(Const.GoodsTimesState.OVER);
        goodsTimes.setTotalBuyTimes(totalTimes);
        goodsTimes.setSnatchProgress(100);
        goodsTimes.setLuckNum(luckNum);
        goodsTimes.setOpenTime(new Date());
        goodsTimes.setWinngUser(user);
        goodsTimes.setWinngUserIdentity(user.getUserName());
        goodsTimes.setWinngUserName(user.getNickName());
        goodsTimes.setWinngUserHeadImg(memberService.getHeadImgByUser(user.getId()));
        goodsTimes.setWinngUserBuyTimes(snatchRecordService
                .getBuyTimesByGoodsTimesAndUser(goodsTimes.getId(), user.getId()));
        goodsTimes.setWinngState(Const.WinngState.CONFIRM_ADDRESS);
        goodsTimesService.save(goodsTimes);
    }

    /**
     * 开奖，产生夺宝号码
     */
    @Test
    public void testProduceLuckNum(){
        Integer[] goodsTimesIds = {51, 62};
        try {
            for (Integer goodsTimesId : goodsTimesIds) {
                snatchRecordService.produceLuckNum(goodsTimesId);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void testUpdateName(){
        snatchRecordService.updateGoodsName(2, "Apple iMac 27寸...");
    }
}
