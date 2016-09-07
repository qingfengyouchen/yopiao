package com.zx.stlife.tools.thread;

import com.base.modules.util.SimpleUtils;
import com.base.modules.util.Threads;
import com.zx.stlife.base.SpringContextHolder;
import com.zx.stlife.base.exception.BizException;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.notice.Notice;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.notice.NoticeService;
import com.zx.stlife.service.order.SnatchListItemService;
import com.zx.stlife.service.order.SnatchRecordService;
import com.zx.stlife.tools.RandomUitls;
import com.zx.stlife.tools.snatch.BuyAllParams;

import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.LockTimeoutException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by micheal on 16/1/13.
 */
public class ThreadWorkUtils {

    private static Logger logger = LoggerFactory.getLogger(ThreadWorkUtils.class);

    /**
     * 若有待开奖的期号，则开启下一期和产生全站50条购买记录
     * @param goodsTimesList
     */
    public static void createNextGoodsTimesAndBuyRecord(List<GoodsTimes> goodsTimesList){
        if(SimpleUtils.isNotNullList(goodsTimesList)) {
            for(final GoodsTimes goodsTimes: goodsTimesList) {
                getThreadPoolTaskExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        SnatchRecordService snatchRecordService = SpringContextHolder.getBean("snatchRecordService");
                        GoodsTimesService goodsTimesService = SpringContextHolder.getBean("goodsTimesService");
                        try {
                            // 记录全站50条购买记录
                            snatchRecordService.recordTop50BuyRecord(goodsTimes);
                        } catch (Exception ex) {
                            logger.error("记录全站50条购买记录失败，期号id:" + goodsTimes.getId(), ex);
                        }

                        // 开启下一期
                        try {
                            goodsTimesService.createNext(goodsTimes);
                        } catch (Exception ex) {
                            logger.error("记录全站50条购买记录失败，期号id:" + goodsTimes.getId(), ex);
                        }
                    }
                });
            }
        }
    }

    /**
     * 清空夺宝清单
     */
    public static void deleteSnatchList(final SnatchListItemService snatchListItemService,
                                        final List<Integer> goodsTimesIds){
        if(SimpleUtils.isNullList(goodsTimesIds)){
            return;
        }

        getThreadPoolTaskExecutor().execute(new Runnable() {
            @Override
            public void run() {
                snatchListItemService.deleteByGoodsTimes(goodsTimesIds);
            }
        });
    }

    /**
     * 还需要考虑是否10专区，是的话先除以10
     * @param buyAllParams
     * @param virtualUsers
     */
    public static void buyGoodsTimesFull(final BuyAllParams buyAllParams,
                                         final List<User> virtualUsers, final int[] buyTimesArr){
        getThreadPoolTaskExecutor().execute(new Runnable() {
            @Override
            public void run() {
                int goodsTimesId = buyAllParams.getGoodsTimesId();
                int userAmount = virtualUsers.size();
                long totalMillis = buyAllParams.getTakeTime() * 60 * 1000;
                long avgMillis = totalMillis / userAmount; // 平均间隔毫秒
                int i = 0;
                logger.info("商品期号id[{}]执行{}个虚拟用户买满start...", goodsTimesId, userAmount);
                long startMillis = System.currentTimeMillis();
                for(User user: virtualUsers){
                    i ++;
                    if(i < userAmount) {
                        long start = System.currentTimeMillis();

                        int beforExecSleepMillis = 0;
                        long max = avgMillis - 600;
                        if(max > 0 ){
                            beforExecSleepMillis = RandomUitls.randomInt((int) max) + 1;
                        }
                        logger.info("beforExecSleepMillis: {}", beforExecSleepMillis);
                        Threads.sleep(beforExecSleepMillis);

                        //处理业务
                        Integer buyTimes = buyTimesArr[i - 1];
                        buyTimes = snatch(user, goodsTimesId, buyTimes);
                        if(buyTimes < 1){
                            logger.info("[{}] buy times: {}, 已买满，不能购买了，结束自动购买。", i, buyTimes);
                            break;
                        }else{
                            logger.info("[{}] buy times: {}", i, buyTimes);
                        }

                        long end = System.currentTimeMillis();
                        long afterExecSleepMillis = avgMillis - (end - start) ;
                        logger.info("afterExecSleepMillis: {}", afterExecSleepMillis);
                        Threads.sleep(afterExecSleepMillis);
                    }else{
                        Integer buyTimes = buyTimesArr[i - 1];
                        long endMillis = System.currentTimeMillis();
                        logger.info("last endMillis: {}, buyTimes", endMillis, buyTimes);
                        if( (endMillis - startMillis)  < totalMillis ) {
                            long sleepMillis = avgMillis - 3000;
                            logger.info("last sleep: {}", sleepMillis);
                            Threads.sleep(sleepMillis);
                        }

                        //处理业务
                        buyTimes = snatch(user, goodsTimesId, buyTimes);
                        logger.info("[{}] buy times: {}", i, buyTimes);
                    }
                }
                logger.info("商品期号id[{}]执行{}个虚拟用户, 实际{}个用户买满end...", goodsTimesId, userAmount, i);
            }
        });
    }

    private static Integer snatch(User user, Integer goodsTimesId, Integer buyTimes){
        SnatchRecordService snatchRecordService = SpringContextHolder.getBean("snatchRecordService");
        while(true) {
            try {
                Map<String, Object> resultMap = snatchRecordService
                        .virtualUserSnatch(user, goodsTimesId, buyTimes);
                if(resultMap == null){
                    buyTimes = 0;
                }else{
                    buyTimes = (Integer)resultMap.get("buyTimes");
                    GoodsTimes preopenGoodsTimes = (GoodsTimes)resultMap.get("preopenGoodsTimes");
                    if(preopenGoodsTimes != null){
                        createNextGoodsTimesAndBuyRecord(Arrays.asList(preopenGoodsTimes));
                    }
                }
                break;
            } catch (BizException ex){
                logger.error(ex.getMessage());
                if(ex.getErrorState() != 101){ // 当已超过总需人次时，重新购买
                    break;
                }
            }catch (ObjectOptimisticLockingFailureException | StaleObjectStateException ex) {
                logger.info("虚拟用户夺宝 - 出现并发，期号id：{}，用户id：{}", goodsTimesId, user.getId());
                Threads.sleep(RandomUitls.randomInt(100));
            } catch (CannotAcquireLockException | LockTimeoutException ex) {
                logger.info("虚拟用户夺宝 - 出现死锁，等待继续执行，期号id：{}，用户id：{}", goodsTimesId, user.getId());
                Threads.sleep(RandomUitls.randomInt(300));
            } catch (Exception ex) {
                logger.error("虚拟用户夺宝 - 夺宝失败，期号id：{}，用户id：{}", goodsTimesId, user.getId());
                logger.error(ex.getMessage(), ex);
                break;
            }
        }

        return buyTimes;
    }

    public static ThreadPoolTaskExecutor getThreadPoolTaskExecutor(){
        return (ThreadPoolTaskExecutor)SpringContextHolder.getBean("threadPoolTaskExecutor");
    }

    /**
     * 后台群发消息处理
     * @param maxId
     * @param entity
     * @param user
     */
    public static void addNoticeToAllUser(final Integer maxId,
                                         final Notice entity,
                                         final User user){
        getThreadPoolTaskExecutor().execute(new Runnable() {
            @Override
            public void run() {
                NoticeService noticeService = SpringContextHolder.getBean("noticeService");
                for(Integer i = 2 ; i <= maxId; i++){
                    Notice saveEntity = new Notice();
                    User tmp = new User();
                    tmp.setId(new Integer(i));
                    saveEntity.setContent(entity.getContent());
                    saveEntity.setTitle(entity.getTitle());
                    saveEntity.setUser(tmp);
                    saveEntity.setSenderName(user.getName());
                    saveEntity.setSender(user);
                    saveEntity.setState(Const.CommonState.ENABLE);
                    noticeService.save(saveEntity);
                }
                logger.info("批量给 " + (maxId - 1)
                        + " 个用户消息...(title="
                        + entity.getTitle() + ";content=" + entity.getContent() + ")");
            }
        });
    }
}
