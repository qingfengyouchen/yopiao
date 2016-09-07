package com.zx.stlife.service.order;

import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.base.modules.util.DateUtilsEx;
import com.base.modules.util.SimpleUtils;
import com.base.modules.util.Threads;
import com.zx.stlife.base.exception.BizException;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.app.base.AppStatusCode;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.entity.goods.GoodsInfo;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.member.Member;
import com.zx.stlife.entity.order.*;
import com.zx.stlife.entity.record.BuyRecord;
import com.zx.stlife.entity.record.RechargeRecord;
import com.zx.stlife.entity.record.RedPack;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.order.SnatchRecordDao;
import com.zx.stlife.service.goods.GoodsService;
import com.zx.stlife.service.goods.GoodsTimesNumService;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.member.MemberService;
import com.zx.stlife.service.record.BuyRecordService;
import com.zx.stlife.service.record.RechargeRecordService;
import com.zx.stlife.service.record.RedPackService;
import com.zx.stlife.service.sms.SmsSendRecordService;
import com.zx.stlife.service.sys.AccountService;
import com.zx.stlife.tools.*;
import com.zx.stlife.tools.alipay.AlipayUtils;
import com.zx.stlife.tools.cqssc.CQSSCUtils;
import com.zx.stlife.tools.cqssc.Period;
import com.zx.stlife.tools.jpush.JPushUtils;
import com.zx.stlife.tools.quartz.QuartzUtils;
import com.zx.stlife.tools.quartz.entity.QuartzEntity;
import com.zx.stlife.tools.quartz.entity.QuartzGoodsTimes;
import com.zx.stlife.tools.quartz.entity.QuartzPayRecord;
import com.zx.stlife.tools.quartz.entity.QuartzRecharge;
import com.zx.stlife.tools.quartz.job.JobGoodsTimes;
import com.zx.stlife.tools.quartz.job.JobPayRecord;
import com.zx.stlife.tools.quartz.job.JobRecharge;
import com.zx.stlife.tools.weixin.WeixinUitls;
import com.zx.stlife.vo.snatch.SnatchRecord4GoodsTimesVo;
import com.zx.stlife.vo.snatch.SnatchRecordVo;
import com.zx.stlife.vo.snatch.SnatchRequestVo;
import com.zx.stlife.vo.snatch.SnatchVo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

import static com.zx.stlife.constant.Const.*;
import static com.zx.stlife.controller.app.base.JsonResultUtils.*;
import static com.zx.stlife.controller.app.base.JsonResultUtils.buildFailureResult;
import static com.zx.stlife.controller.app.base.JsonResultUtils.buildOneElementMap;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Service
@Transactional
public class SnatchRecordService {

    private static Logger logger = LoggerFactory.getLogger(SnatchRecordService.class);

    @Autowired
    private SnatchRecordDao snatchRecordDao;
    @Autowired
    private RedPackService redPackService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private GoodsTimesService goodsTimesService;
    @Autowired
    private PayRecordService payRecordService;
    @Autowired
    private SnatchRecordDetailService snatchRecordDetailService;
    @Autowired
    private PayWayService payWayService;
    @Autowired
    private GoodsTimesNumService goodsTimesNumService;
    @Autowired
    private SnatchNumService snatchNumService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RechargeRecordService rechargeRecordService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private BuyRecordService buyRecordService;
    @Autowired
    private SmsSendRecordService smsSendRecordService;
    @Autowired
    private WinngGoodsStateService winngGoodsStateService;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private String KEY_RESULT = "result";

    private static final String QL_QUERY_RECORD_BY_USER =
            "SELECT NEW com.zx.stlife.vo.snatch.SnatchRecordVo(" +
                    "t.goodsName, t.thumbnail, t.isTenYuan, r.id, t.id, t.state, " +
                    "t.times, t.totalTimes, t.totalBuyTimes, t.snatchProgress, r.buyTimes, " +
                    "t.winngUser.id, t.winngUserName, t.winngUserBuyTimes, t.luckNum, t.openTime) " +
                    "from SnatchRecord r left join r.goodsTimes t where r.user.id=:userId " +
                    "and r.createTime <=:createTime and r.state=:payState";

    private static final String QL_QUERY_BUY_TIMES_BY_USER_GOODS_TIMES =
            "select t.buyTimes from SnatchRecord t where t.user.id=?1 " +
                    "and t.goodsTimes.id=?2 and t.state=?3";

    private static final String QL_QUERY_RANDOM1_SNATCH_VIRTUAL_USER_BY_GOODS_TIMES =
            "select u from SnatchRecord t left join t.user u where t.goodsTimes.id=?1 " +
                "and t.state=?2 and u.isVirtual=true order by rand()";

    private static final String SQL_UPDATE_GOODS_NAME =
            "update order_snatch_record t, goods_times g set t.goods_name=?2 where t.goods_times_id=g.id and g.id=?1";

    public SnatchRecord findByUserAndGoodsTimes(Integer userId, Integer goodsTimesId) {
        return snatchRecordDao.findByUserAndGoodsTimes(userId, goodsTimesId, PayResult.PAY_SUCCESS);
    }

    public List<SnatchRecord4GoodsTimesVo> findByGoodsTimes(Integer goodsTimesId){
        return snatchRecordDao.findByGoodsTimes(goodsTimesId, PayResult.PAY_SUCCESS);
    }

    @Transactional
    public void save(SnatchRecord entity) {
        entity = snatchRecordDao.save(entity);
    }

    public Integer getBuyTimesByGoodsTimesAndUser(Integer goodsTimeId, Integer userId) {
        return snatchRecordDao.getBuyTimesByGoodsTimesAndUser(goodsTimeId, userId, PayResult.PAY_SUCCESS);
    }

    public void findPageByUser(Page<SnatchRecordVo> page, Integer userId, Byte goodsTimesState, Date date) {
        StringBuffer sb = new StringBuffer(QL_QUERY_RECORD_BY_USER);
        boolean hasState = false;
        Map<String, Object> values = new HashMap<>();
        values.put("userId", userId);
        values.put("createTime", date);
        values.put("payState", PayResult.PAY_SUCCESS);

        if (goodsTimesState != null) {
            if(goodsTimesState == 1) { // 进行中
                sb.append(" and t.state in (:goingStates)");
                values.put("goingStates", GoodsTimesState.goingStates);
            }else {
                sb.append(" and t.state=:goodsTimesState");
                values.put("goodsTimesState", goodsTimesState);
            }
        }
        sb.append(" order by r.editTime desc");

        snatchRecordDao.queryPage(page, sb.toString(), values);
    }

    public void findWinListPageByUser(Page<GoodsTimes> page, Integer userId, Date date, Byte state) {
        Query query = new Query();
        query.table("select t from GoodsTimes t")
                .eq("t.winngUser.id", userId)
                .le("t.createTime", date)
                .eq("t.state", state)
                .orderBy("t.openTime desc");

        snatchRecordDao.queryPage(page, query.getQLString(), query.getValues());
    }

    public SnatchRecord getSnatchRecord(Integer snatchRecordId) {
        return snatchRecordDao.findOne(snatchRecordId);
    }

    /**
     * 支付或下单
     *
     * @param snatchRequestVo
     * @param totalTimes
     * @param request
     * @return map
     *         有两个key: result-JsonResult对象，preopenGoodsTimesList-待开奖商品期号
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> payOrOrders(SnatchRequestVo snatchRequestVo,
                                  int totalTimes, HttpServletRequest request, boolean isWeixinPayWithH5) throws Exception {

        Integer userId = snatchRequestVo.getUserId();
        List<Byte> payWays = snatchRequestVo.getPayWays();
        List<Integer> redPackIds = snatchRequestVo.getRedPackIds();
        List<SnatchVo> snatchVoList = snatchRequestVo.getSnatchList();
        Integer thirdpartNeedPayMoney = snatchRequestVo.getThirdpartMoney();

        // 检查各种付款金额是否正确
        boolean isBalancePay = payWays.contains(PayWayType.CURRENCY);       // 是否使用余额支付
        boolean isRedPackPay = payWays.contains(PayWayType.REDPACKET);      // 是否使用红包支付
        boolean isWeixinPay = payWays.contains(PayWayType.WEIXIN);
        boolean isAliPay = payWays.contains(PayWayType.ALIPAY);

        if(isWeixinPay && !canPayByWeixin && !isWeixinPayWithH5){
            return buildFailureResultMap(311);// 不支持微信支付
        }
        if(isAliPay && !canPayByAlipay){
            return buildFailureResultMap(312);// 不支持支付宝支付
        }

        Integer totalCanUseRedPackMoney = 0;            // 实际能用的红包的大小
        List<RedPack> totalCanUseRedPackList = null;    // 实际能用的红包
        //Integer surplusPayMoney = 0;                  // 剩余支付的金额
        int redPackPayMoney = 0;                        // 红包支付金额
        int balancePayMoney = 0;                        // 余额支付金额
        int thirdPartPayMoney = 0;                      // 第三方支付金额

        // ####确定各商品购买人次
        // 实际能购买总人次
        int totalCanBuyTimes = 0;
        Map<GoodsTimes, Integer> goodsTimesMap = new LinkedHashMap<>();
        for (SnatchVo snatchVo : snatchVoList) {
            int buyTimes = snatchVo.getAmount();
            GoodsTimes goodsTimes = goodsTimesService.get(snatchVo.getGid());
            if (goodsTimes.getIsTenYuan() && buyTimes % 10 != 0) {
            	logger.info("==========throw new BizException(309)");
                throw new BizException(309); // 十元夺宝的商品购买人次必须能被10整除
            }

            if (goodsTimes.getState() == GoodsTimesState.GOING) { // 进行中
                int actualBuyTimes = computeGoodsTimesBuyTimes(goodsTimes, buyTimes);
                totalCanBuyTimes += actualBuyTimes;

                goodsTimesMap.put(goodsTimes, actualBuyTimes);
            }
        }

        if (totalCanBuyTimes == 0 && (
                payWays.contains(PayWayType.REDPACKET) ||
                        payWays.contains(PayWayType.CURRENCY)
        )) {
        	logger.info("==========throw new BizException(310)");
            return buildFailureResultMap(310); // 你选择的期号已买满，不能夺宝
        }

        logger.info("==========accountService.getUser(userId)");
        User user = accountService.getUser(userId);

        String ip = RequestUtils.getRealIp(request);

        // 1. 红包支付
        if (isRedPackPay) {
        	logger.info("==========isRedPackPay");
            List<RedPack> redPackList = null;
            if (SimpleUtils.isNullList(redPackIds) ||
                    SimpleUtils.isNullList(
                            redPackList = redPackService.
                                    findCanUseRedPackByUserAndIds(userId, redPackIds))) {
                return buildFailureResultMap(304);        // 选择的红包不可用
            }

            totalCanUseRedPackList = new ArrayList<>();
            for (RedPack redPack : redPackList) {
                totalCanUseRedPackMoney += redPack.getBalance();
                totalCanUseRedPackList.add(redPack);
                if (totalCanUseRedPackMoney >= totalCanBuyTimes) {
                    break;
                }
            }

            // 1.1. 完成支付
            if (totalCanUseRedPackMoney >= totalCanBuyTimes) { // 使用红包足够的情况
                redPackPayMoney = totalCanBuyTimes;
                return finishPay(
                        totalCanUseRedPackList, totalCanBuyTimes, goodsTimesMap, user,
                        isRedPackPay, isBalancePay, 0, memberService.findMember(userId), ip);
            } else if (!isWeixinPay & !isAliPay & !isBalancePay) {
                return buildFailureResultMap(311); // 红包不足，请选择其他方式支付
            }

            redPackPayMoney = totalCanUseRedPackMoney;
        }

        // 2. 余额支付
        if (isBalancePay) {
        	logger.info("==========isBalancePay");
            Member member = memberService.findMember(userId);
            Integer balance = member.getBalance();
            if (balance == null || balance < 1) {
                return buildFailureResultMap(305); // 余额不足
            }

            int surplusPayMoney = totalCanBuyTimes - redPackPayMoney;
            if (balance >= surplusPayMoney) {
            	logger.info("==========finishPay before");
                // 2.1. 完成支付
                return finishPay(
                        totalCanUseRedPackList, totalCanBuyTimes, goodsTimesMap, user,
                        isRedPackPay, isBalancePay, surplusPayMoney, member, ip);
            } else if (!isWeixinPay & !isAliPay) {
                return buildFailureResultMap(312); // 余额不足，请选择其他方式支付
            }

            balancePayMoney = balance;
        }

        
        logger.info("==========第三方支付");
        // 3. 第三方支付（微信或支付宝）
        if (!isWeixinPay & !isAliPay) {
            return buildFailureResultMap(306); // 不支持除微信和支付宝外的其他第三方支付
        }

        if (thirdpartNeedPayMoney == null || thirdpartNeedPayMoney < 1) {
            return buildFailureResultMap(307); // 需要微信支付的金额不能小于等于1
        }

        thirdPartPayMoney = totalTimes - redPackPayMoney - balancePayMoney;
        if (thirdPartPayMoney != thirdpartNeedPayMoney) { // 由于可能存在实际购买的人次少于预期的情况
            return buildFailureResultMap(308); // 实际需要微信或支付宝支付的金额与传人的参数不一致。
        }

        if (isWeixinPay) {      // 微信支付下单
            return recordWeixinOrders(isBalancePay, isRedPackPay,
                    totalCanUseRedPackList, thirdPartPayMoney, totalCanBuyTimes, user, request,
                    goodsTimesMap, isWeixinPayWithH5);

        } else {                // 支付宝支付下单
            return recordAlipayOrders(isBalancePay, isRedPackPay, totalCanUseRedPackList,
                    thirdPartPayMoney, snatchRequestVo.getIsAlipayWithH5(), totalCanBuyTimes, user, goodsTimesMap, ip);
        }
    }

    private Map<String, Object> buildFailureResultMap(int code){
        HashMap<String, Object> resultMap = new HashMap<>(1);
        resultMap.put(KEY_RESULT, buildFailureResult(code));
        return resultMap;
    }
    /**
     * 微信支付下单
     *
     * @param isPayWithBalance
     * @param isPayWithRedPack
     * @param totalCanUseRedPackList
     * @param thirdPartPayMoney
     * @param totalCanBuyTimes
     * @param user
     * @param request
     * @param goodsTimesMap
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> recordWeixinOrders(boolean isPayWithBalance, boolean isPayWithRedPack,
                                          List<RedPack> totalCanUseRedPackList, int thirdPartPayMoney,
                                          int totalCanBuyTimes, User user, HttpServletRequest request,
                                          Map<GoodsTimes, Integer> goodsTimesMap, boolean isH5Pay) throws Exception {

        String outTradeNo = SimpleUtils.getHibernateUUID();
        JsonResult jsonResult = WeixinUitls.order(
                request, user, outTradeNo, thirdPartPayMoney, true, isH5Pay);

        if (jsonResult.getState() == AppStatusCode.SC_SUCCESS){
            // 预生成支付记录
            recordOrders(isPayWithBalance, isPayWithRedPack, totalCanUseRedPackList,
                    thirdPartPayMoney, totalCanBuyTimes, user, goodsTimesMap, outTradeNo,
                    true, isH5Pay, RequestUtils.getRealIp(request));
        }

        return buildJsonResultMap(jsonResult);
    }

    private Map<String, Object> buildJsonResultMap(final JsonResult jsonResult){
        return buildOneElementMap(KEY_RESULT, jsonResult);
    }

    /**
     * 支付宝支付下单
     *
     * @param isPayWithBalance
     * @param isPayWithRedPack
     * @param totalCanUseRedPackList
     * @param thirdPartPayMoney
     * @param isAlipayWithH5
     * @param totalCanBuyTimes
     * @param user
     * @param goodsTimesMap
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> recordAlipayOrders(boolean isPayWithBalance, boolean isPayWithRedPack,
                                          List<RedPack> totalCanUseRedPackList, int thirdPartPayMoney,
                                          boolean isAlipayWithH5, int totalCanBuyTimes, User user,
                                          Map<GoodsTimes, Integer> goodsTimesMap, String ip) throws Exception {

        String outTradeNo = SimpleUtils.getHibernateUUID();
        JsonResult jsonResult = null;
        try {
            recordOrders(isPayWithBalance, isPayWithRedPack, totalCanUseRedPackList,
                    thirdPartPayMoney, totalCanBuyTimes, user, goodsTimesMap, outTradeNo, false, false, ip);

            Map<String, String> data = new HashMap<>(2);
            data.put("outTradeNo", outTradeNo);
            data.put("payParams", AlipayUtils.buildRequestUrl(outTradeNo, thirdPartPayMoney, true, isAlipayWithH5));
            LogUtils.logMap("支付宝支付参数：", data);
            jsonResult =  buildSuccessResult(data);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            jsonResult = buildFailureResult(buildOneElementMap("errDescr", "支付宝下单失败"));
        }

        return buildJsonResultMap(jsonResult);
    }

    /***
     * 支付宝或微信下单，预生成支付记录
     *
     * @param isPayWithBalance
     * @param isPayWithRedPack
     * @param totalCanUseRedPackList
     * @param thirdPartPayMoney
     * @param totalCanBuyTimes
     * @param user
     * @param goodsTimesMap
     * @param outTradeNo
     * @param isWeixinPay
     */
    @Transactional(rollbackFor = Exception.class)
    public void recordOrders(boolean isPayWithBalance, boolean isPayWithRedPack,
                              List<RedPack> totalCanUseRedPackList, int thirdPartPayMoney,
                              int totalCanBuyTimes, User user, Map<GoodsTimes, Integer> goodsTimesMap,
                              String outTradeNo, boolean isWeixinPay, boolean isH5Pay, String ip) throws Exception {
        // 预生成支付记录
        PayRecord payRecord = null;
        if(isWeixinPay){
            payRecord = new PayRecord(user, totalCanBuyTimes,
                    outTradeNo, true, PayUtils.getExpireDate(), PayResult.UNPAY, null);
            payRecordService.save(payRecord);
        }else{
            payRecord = new PayRecord(user, totalCanBuyTimes,
                    outTradeNo, false, null, PayResult.UNPAY, null);
            payRecordService.save(payRecord);
        }

        if (isPayWithRedPack) { // 使用红包
            for (RedPack redPack : totalCanUseRedPackList) {
                int payMoney = redPack.getBalance();
                redPack.setBalance(0);
                redPack.setState(RedPackState.USED);
                redPackService.save(redPack);

                PayWay payWay = new PayWay(PayWayType.REDPACKET, payMoney, payRecord, redPack);
                payWayService.save(payWay);
            }
        }

        if (isPayWithBalance) { // 使用余额
            Member member = memberService.findMember(user.getId());
            PayWay payWay = new PayWay(PayWayType.CURRENCY, member.getBalance(), payRecord, null);
            payWayService.save(payWay);

            // 更新用户余额
            member.setBalance(0);
            memberService.saveMember(member);
        }

        // 微信或支付宝支付
        PayWay payWay = new PayWay(
                isWeixinPay ? PayWayType.WEIXIN : PayWayType.ALIPAY, thirdPartPayMoney, payRecord, null);
        payWayService.save(payWay);

        // 生成夺宝记录明细(状态为未支付)
        for (Map.Entry<GoodsTimes, Integer> entry : goodsTimesMap.entrySet()) {
            GoodsTimes goodsTimes = entry.getKey();
            Integer buyTimes = entry.getValue();

            SnatchRecord snatchRecord = findByUserAndGoodsTimes(user.getId(), goodsTimes.getId());
            if (snatchRecord == null) {
                snatchRecord = new SnatchRecord(goodsTimes.getGoodsName(), goodsTimes, goodsTimes.getTimes(),
                        goodsTimes.getTotalTimes(), 0, 0, user, PayResult.UNPAY);
                save(snatchRecord);
            }

            SnatchRecordDetail snatchRecordDetail = new SnatchRecordDetail(
                    payRecord, goodsTimes.getGoodsInfo(), goodsTimes,
                    buyTimes, buyTimes, user, memberService.getHeadImgByUser(user.getId()),
                    user.getNickName(), null, ip, snatchRecord, PayResult.UNPAY);
            snatchRecordDetailService.save(snatchRecordDetail);
        }

        // 定时查询订单状态的任务
        if (isWeixinPay){
            addQueryWeixinOrderJob(true, isH5Pay, payRecord.getOutTradeNo(), PayUtils.getQueryJobCronExpression());
        }
    }

    /**
     * 使用红包或余额或(红包+余额)方式直接完成支付
     *
     * @param totalCanUseRedPackList
     * @param totalCanBuyTimes
     * @param goodsTimesMap
     * @param user
     * @param isPayWithRedPack
     * @param isPayWithBalance
     * @param surplusPayMoney
     * @param member
     * @return map
     *         有两个key: result-JsonResult对象，preopenGoodsTimesList-待开奖商品期号
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> finishPay(List<RedPack> totalCanUseRedPackList, int totalCanBuyTimes,
                                Map<GoodsTimes, Integer> goodsTimesMap, User user, boolean isPayWithRedPack,
                                boolean isPayWithBalance, int surplusPayMoney, Member member, String ip)
            throws Exception {
    	logger.info("==========finishPay in");
        final String resultDescr = "你已成功参与了" + goodsTimesMap.size() + "件商品共" + totalCanBuyTimes + "人次夺宝";
        // 生成支付记录
        PayRecord payRecord = new PayRecord(user, totalCanBuyTimes,
                null, false, null, PayResult.PAY_SUCCESS, resultDescr);
        payRecordService.save(payRecord);
        if (isPayWithRedPack) { // 使用红包
            int surplusMoney = totalCanBuyTimes;
            for (RedPack redPack : totalCanUseRedPackList) {
                int payMoney = redPack.getBalance();
                surplusMoney -= payMoney;
                if (surplusMoney >= 0) {
                    redPack.setBalance(0);
                    redPack.setState(RedPackState.USED);
                } else {
                    redPack.setBalance(Math.abs(surplusMoney));
                    payMoney -= redPack.getBalance();
                }
                redPackService.save(redPack);

                PayWay payWay = new PayWay(PayWayType.REDPACKET, payMoney, payRecord, redPack);
                payWayService.save(payWay);
            }
        }
        logger.info("==========isPayWithBalance");
        if (isPayWithBalance) { // 使用余额
            PayWay payWay = new PayWay(PayWayType.CURRENCY, surplusPayMoney, payRecord, null);
            payWayService.save(payWay);

            // 更新用户余额
            member.setBalance(member.getBalance() - surplusPayMoney);
            memberService.saveMember(member);
        }

        logger.info("==========preopenGoodsTimesList");
        List<GoodsTimes> preopenGoodsTimesList = null;
        final List<Map<String, Object>> snatchList = new ArrayList<>(goodsTimesMap.size());
        for (Map.Entry<GoodsTimes, Integer> entry : goodsTimesMap.entrySet()) {
            GoodsTimes goodsTimes = entry.getKey();
            Integer buyTimes = entry.getValue();

            logger.info("==========computeAndSaveBuyTimes before");
            // 更新商品期号夺宝进度
            goodsTimesService.computeAndSaveBuyTimes(goodsTimes, buyTimes);
            logger.info("==========computeAndSaveBuyTimes after");
            Map<String, Object> resultMap = generateSnatchRecordAndNum(user, goodsTimes, buyTimes, payRecord, ip);
            GoodsTimes preopenGoodsTimes = (GoodsTimes)resultMap.get("preopenGoodsTimes");
            if(preopenGoodsTimes != null){
                if(preopenGoodsTimesList == null){
                    preopenGoodsTimesList = new ArrayList<>();
                }
                preopenGoodsTimesList.add(preopenGoodsTimes);
                resultMap.remove("preopenGoodsTimes");
            }
            snatchList.add(resultMap);
        }

        /*// 计算积分 totalTimes
        memberService.recordJifen(user.getId(), totalCanBuyTimes - surplusPayMoney, "红包支付");*/

        Map<String, Object> data = new HashMap<String, Object>(2){{
            put("descr", resultDescr);
            put("snatchList", snatchList);
        }};

        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put(KEY_RESULT, buildSuccessResult(data));
        resultMap.put("preopenGoodsTimesList", preopenGoodsTimesList);

        return resultMap;
    }

    /**
     * 产生夺宝记录和夺宝号码
     *
     * @param user
     * @param goodsTimes
     * @param buyTimes
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> generateSnatchRecordAndNum(User user, GoodsTimes goodsTimes,
                Integer buyTimes, PayRecord payRecord, String ip) throws Exception {
        SnatchRecord snatchRecord = findByUserAndGoodsTimes(user.getId(), goodsTimes.getId());
        if (snatchRecord == null) {
            snatchRecord = new SnatchRecord(goodsTimes.getGoodsName(), goodsTimes, goodsTimes.getTimes(),
                    goodsTimes.getTotalTimes(), buyTimes, buyTimes, user, PayResult.PAY_SUCCESS);
        } else {
            snatchRecord.setEditTime(DateUtils.getNow());
            snatchRecord.addBuyTimesAndMoney(buyTimes);
        }
        save(snatchRecord);

        SnatchRecordDetail snatchRecordDetail = new SnatchRecordDetail(
                payRecord, goodsTimes.getGoodsInfo(), goodsTimes,
                buyTimes, buyTimes, user, memberService.getHeadImgByUser(user.getId()),
                user.getNickName(), null, ip, snatchRecord, PayResult.PAY_SUCCESS);
        snatchRecordDetailService.save(snatchRecordDetail);
        logger.info("==========produceSnatchNumAndDoGoods(snatchRecordDetail before");
        Map<String, Object> resultMap = produceSnatchNumAndDoGoods(snatchRecordDetail);
        logger.info("==========produceSnatchNumAndDoGoods(snatchRecordDetail after");

        Map<String, Object> result = new HashMap<>(6);
        result.put("goodsName", goodsTimes.getGoodsName());
        result.put("goodsTimesId", goodsTimes.getId());
        result.put("goodsTimesName", goodsTimes.getTimes());
        result.put("buyTimes", buyTimes);
        result.putAll(resultMap);

        return result;
    }

    /**
     * 产生夺宝号码，更新销量等
     *
     * @param snatchRecordDetail
     * @return map
     *          包含2个key: nums-前几个夺宝号码，goodsTimes-待开奖的期号
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> produceSnatchNumAndDoGoods(SnatchRecordDetail snatchRecordDetail)
            throws Exception {
        Map<String, Object> resultMap = new HashMap<>(2);
        GoodsTimes goodsTimes = snatchRecordDetail.getGoodsTimes();

        Integer[] nums = goodsTimesNumService.produceSnatchNum(snatchRecordDetail);

        if (goodsTimes.getState() == GoodsTimesState.WAITING) {
            // 启动定时任务等待开奖
            Period period = CQSSCUtils.getLastPeriod();
            
            Date date=new Date();
 
 //           addGoodsTimesOpenJob(goodsTimes, DateUtilsEx.addMinutes(date, 2));
            addGoodsTimesOpenJob(goodsTimes, DateUtilsEx.addSeconds(date, 2));
 //           addGoodsTimesOpenJob(goodsTimes, date);

 //           goodsTimes.setOpenTime(DateUtilsEx.addSeconds(date, 15)); // 多加4分钟再开奖
            goodsTimes.setOpenTime(DateUtilsEx.addSeconds(date, 5)); // 多加4分钟再开奖
            goodsTimes.setCqsscPeriodNo(period.getPeriodNo());
            Date now = DateUtilsEx.getNow();
            goodsTimes.setFullTime(now.getTime());
            goodsTimesService.save(goodsTimes);

            // 销售商品+1
            GoodsInfo goodsInfo = goodsTimes.getGoodsInfo();
            goodsInfo.setSalesAmount(goodsInfo.getSalesAmount() + 1);
            goodsService.save(goodsInfo);

            resultMap.put("preopenGoodsTimes", goodsTimes);
            /*// 记录全站50条购买记录
            recordTop50BuyRecord(goodsTimes);
            // 开启下一期
            goodsTimesService.createNext(goodsTimes);*/

        }

        resultMap.put("nums", nums);

        return resultMap;
    }

    /**
     * 添加定时任务
     * @param goodsTimes
     * @param cronExpression
     */
    @Transactional(rollbackFor = Exception.class)
    public void addGoodsTimesOpenJob(GoodsTimes goodsTimes, Object cronExpression)
            throws Exception{
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("entityId", String.valueOf(goodsTimes.getId()));
        QuartzGoodsTimes quartzGoodsTimes = null;
        if(cronExpression instanceof Date){
            quartzGoodsTimes= new QuartzGoodsTimes(goodsTimes.getId(),(Date)cronExpression);
        }else{
            quartzGoodsTimes= new QuartzGoodsTimes(goodsTimes.getId(),(String)cronExpression);
        }
        QuartzUtils.registerJob(quartzGoodsTimes, JobGoodsTimes.class, params);
    }


    private static final Map<String, Lock> thirdpartPaySuccessLocks = new ConcurrentHashMap<>();
    /**
     * 支付成功，计算可以购买的商品期号及购买人次
     *
     * @param outTradeNo
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> computeBuyTimsAndSantch(String outTradeNo, Date payTime) throws Exception{

        LockUtils.lock(thirdpartPaySuccessLocks, outTradeNo);
        Map<String, Object> resultMap = new HashMap<>(2);
        try {

            PayRecord payRecord = payRecordService.getByOutTradeNo(outTradeNo);
            if (payRecord == null) {
                logger.error("支付成功回调：outTradeNo[{}]不存在", outTradeNo);
                resultMap.put("isSccuess", true);
                return resultMap;
            }

            if (payRecord.getState() == Const.PayResult.PAY_SUCCESS ||
                    payRecord.getState() == Const.PayResult.PAY_SUCCESS_BUT_CANNOT_SNATCH) {
                logger.info("支付商户订单号：{}，【{}】已处理，不需要重复处理 - end",
                        outTradeNo, Const.PayResult.MAP.get(payRecord.getState()));
                resultMap.put("isSccuess", true);
                return resultMap;
            }

            logger.info(" == 支付更改前的状态: " + Const.PayResult.MAP.get(payRecord.getState()));
            payRecord.setThirdpartPayTime(payTime);

            User user = payRecord.getUser();
            List<SnatchRecordDetail> snatchRecordDetailList = payRecord.getSnatchRecordDetailList();

            // ####确定各商品购买人次
            int totalCanBuyTimes = 0;       // 实际能购买总人次
            int totalOrderBuyTimes = 0;     // 总购买总人次
            int goodsAmount = 0;            // 商品数量
            List<GoodsTimes> preopenGoodsTimesList = null;
            for (SnatchRecordDetail snatchRecordDetail : snatchRecordDetailList) {
                GoodsTimes goodsTimes = snatchRecordDetail.getGoodsTimes();
                int buyTimes = snatchRecordDetail.getBuyTimes();
                totalOrderBuyTimes += buyTimes;

                boolean canSnatch = true;
                Byte state = null;
                SnatchRecord snatchRecord = snatchRecordDetail.getSnatchRecord();
                if (goodsTimes.getState() == GoodsTimesState.GOING) { // 进行中
                    goodsAmount++;
                    state = PayResult.PAY_SUCCESS;
                    int actualBuyTimes = computeGoodsTimesBuyTimes(goodsTimes, buyTimes);
                    totalCanBuyTimes += actualBuyTimes;

                    // 更新商品期号夺宝进度
                    goodsTimesService.computeAndSaveBuyTimes(goodsTimes, actualBuyTimes);

                    snatchRecordDetail.setBuyTimes(actualBuyTimes);

                    snatchRecord.setEditTime(DateUtils.getNow());
                    snatchRecord.addBuyTimesAndMoney(actualBuyTimes);
                } else { // 满额
                    canSnatch = false;
                    state = PayResult.PAY_SUCCESS_BUT_CANNOT_SNATCH;
                }

                snatchRecordDetail.setState(state);
                snatchRecordDetailService.save(snatchRecordDetail);

                snatchRecord.setState(state);
                save(snatchRecord);

                if (canSnatch) {
                    Map<String, Object> resultMap2 = produceSnatchNumAndDoGoods(snatchRecordDetail);
                    if(resultMap2.get("preopenGoodsTimes") != null){
                        if(preopenGoodsTimesList == null){
                            preopenGoodsTimesList = new ArrayList<>();
                        }
                        preopenGoodsTimesList.add((GoodsTimes)resultMap2.get("preopenGoodsTimes"));
                    }
                }
            }

            int returnBackMoney = totalOrderBuyTimes - totalCanBuyTimes; // 退还的金额
            int balance = 0;                        // 使用余额支付的金额
            boolean isWeixinPay = true;             // 是否微信支付
            int thirdpartPayMoney = 0;              // 微信或支付宝支付金额
            int toBalance = 0;                      // 充值到余额中的金额
            PayWay thirdpartPayWay = null;          // 微信或支付宝支付记录
            PayWay balancePayWay = null;            // 余额支付记录
            List<PayWay> redPackPayWayList = null;  // 红包支付记录
            List<PayWay> payWayList = payRecord.getPayWayList();
            for (PayWay payWay : payWayList) {
                if (payWay.getWay() == PayWayType.REDPACKET) {
                    if (redPackPayWayList == null) {
                        redPackPayWayList = new ArrayList<>();
                    }

                    //redPackMoney += payWay.getMoney();
                    redPackPayWayList.add(payWay);
                } else if (payWay.getWay() == PayWayType.CURRENCY) {
                    balance = payWay.getMoney();
                    balancePayWay = payWay;
                } else if (payWay.getWay() == PayWayType.WEIXIN) {
                    isWeixinPay = true;
                    thirdpartPayWay = payWay;
                } else if (payWay.getWay() == PayWayType.ALIPAY) {
                    isWeixinPay = false;
                    thirdpartPayWay = payWay;
                }
            }

            if (returnBackMoney > 0) {
                if (payWayList.size() == 1) { // 微信/支付宝支付
                    toBalance = returnBackMoney;

                    PayWay payWay = payWayList.get(0);
                    payWay.setMoney(totalCanBuyTimes);
                    payWay.setState(PayResult.PAY_SUCCESS);
                    payWayService.save(payWay);
                    thirdpartPayMoney = totalOrderBuyTimes;
                } else {
                    // 退还的金额 大于或等于 第三方支付的金额
                    if (thirdpartPayMoney >= returnBackMoney) {
                        toBalance = returnBackMoney;
                        if (thirdpartPayMoney == returnBackMoney) {
                            payWayService.delete(thirdpartPayWay);
                        } else {
                            thirdpartPayWay.setMoney(thirdpartPayMoney - returnBackMoney);
                            thirdpartPayWay.setState(PayResult.PAY_SUCCESS);
                            payWayService.save(thirdpartPayWay);
                        }
                    } else {
                        payWayService.delete(thirdpartPayWay);

                        toBalance = thirdpartPayMoney;
                        int surplusReturnBackMoney = returnBackMoney - thirdpartPayMoney; // 剩余返还金额
                        if (balance >= surplusReturnBackMoney) {
                            toBalance += surplusReturnBackMoney;
                            if (balance == surplusReturnBackMoney) {
                                payWayService.delete(balancePayWay);
                            } else {
                                balancePayWay.setMoney(balance - surplusReturnBackMoney);
                                balancePayWay.setState(PayResult.PAY_SUCCESS);
                                payWayService.save(balancePayWay);
                            }
                        } else {
                            toBalance += balance;
                            surplusReturnBackMoney -= balance; // 剩余返还金额

                            PayWay redPackPayWay = null;
                            int total = 0;
                            for (int i = redPackPayWayList.size() - 1; i >= 0; i--) {
                                redPackPayWay = redPackPayWayList.get(i);
                                total += redPackPayWay.getMoney();
                                if (total > surplusReturnBackMoney) {
                                    break;
                                }

                                RedPack redPack = redPackPayWay.getRedPack();
                                redPack.addBalance(redPackPayWay.getMoney());
                                redPack.setState(RedPackState.CAN_USE);
                                redPackService.save(redPack);
                                payWayService.delete(redPackPayWay);
                                redPackPayWay = null;
                            }

                            if (redPackPayWay != null) {
                                for(PayWay payWay: redPackPayWayList){
                                    redPackPayWay.setState(PayResult.PAY_SUCCESS);
                                    if(payWay.getId().intValue() == redPackPayWay.getId().intValue()){
                                        int backMoney = total - surplusReturnBackMoney;
                                        redPackPayWay.setMoney(redPackPayWay.getMoney() - backMoney);
                                        payWayService.save(redPackPayWay);

                                        if(backMoney > 0) {
                                            RedPack redPack = redPackPayWay.getRedPack();
                                            redPack.addBalance(backMoney);
                                            redPack.setState(RedPackState.CAN_USE);
                                            redPackService.save(redPack);
                                        }

                                        break;
                                    }else{
                                        payWayService.save(redPackPayWay);
                                    }
                                }
                            }
                        }
                    }
                }
            }else{
                thirdpartPayMoney = totalCanBuyTimes;
                for (PayWay payWay : payWayList) {
                    payWay.setState(PayResult.PAY_SUCCESS);
                    payWayService.save(payWay);
                }
            }

            // 保存充值记录
            if (thirdpartPayMoney > 0) {
                rechargeRecordService.save(thirdpartPayMoney, isWeixinPay,
                        user, RechargeState.RECHARGE_SUCCESS, DateUtils.getNow());
            }

            StringBuffer resultDescr = new StringBuffer("你已成功参与了");
            resultDescr.append(goodsAmount)
                    .append("件商品共")
                    .append(totalCanBuyTimes)
                    .append("人次夺宝");

            // 退还余额
            if (toBalance > 0) {
                Member member = memberService.findMember(user.getId());
                member.addBalance(toBalance);
                memberService.saveMember(member);
                resultDescr.append("，")
                        .append(toBalance)
                        .append("元已充值到余额中");
            }

            /*// 计算积分 (红包 + 第三方支付的金额)
            int jifenAmount = redPackMoney + toBalance;
            if (jifenAmount > 0) {
                memberService.recordJifen(user.getId(), jifenAmount, "夺宝支付");
            }*/

            payRecord.setResultDescr(resultDescr.toString());
            payRecord.setToBalance(toBalance);
            payRecord.setState(PayResult.PAY_SUCCESS);
            payRecord.setNeedQueryPayResult(false);
            payRecordService.save(payRecord);

            resultMap.put("isSccuess", true);
            resultMap.put("preopenGoodsTimesList", preopenGoodsTimesList);
            return resultMap;
        }finally {
            LockUtils.unlock(thirdpartPaySuccessLocks, outTradeNo);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public int computeGoodsTimesBuyTimes(GoodsTimes goodsTimes, int buyTimes)
            throws Exception{
        // 已购买总人次
        int totalBuyTimes = goodsTimes.getTotalBuyTimes();
        // 剩余购买人次
        int surplusGoodsTimes = goodsTimes.getTotalTimes() - totalBuyTimes;
        // 实际购买人次
        int actualBuyTimes = surplusGoodsTimes >= buyTimes
                ? buyTimes : surplusGoodsTimes;

        return actualBuyTimes;
    }

    /**
     * 5.13. 获取支付结果
     *
     * @param userId     用户id
     * @param outTradeNo 商户订单号
     * @return
     */
    public JsonResult getPayResult(Integer userId, String outTradeNo) {
        PayRecord payRecord = payRecordService.getByOutTradeNo(outTradeNo);
        if (payRecord == null) {
            return buildFailureResult(301); // 获取失败，参数错误
        }

        Map<String, Object> data = new HashMap<>();
        data.put("descr", payRecord.getResultDescr());

        List<SnatchRecordDetail> snatchRecordDetailList =
                snatchRecordDetailService.findByPayRecord(payRecord.getId());
        List<Map<String, Object>> snatchList = null;
        if (snatchRecordDetailList != null) {
            snatchList = new ArrayList<>(snatchRecordDetailList.size());
            for (SnatchRecordDetail snatchRecordDetail : snatchRecordDetailList) {
                GoodsTimes goodsTimes = snatchRecordDetail.getGoodsTimes();
                int buyTimes = snatchRecordDetail.getBuyTimes();
                List<Integer>nums = snatchNumService
                        .findTopNumsByBuyTimesAndSnatchRecordDetail(buyTimes, snatchRecordDetail.getId());

                Map<String, Object> map = new HashMap<>(5);
                map.put("goodsName", goodsTimes.getGoodsName());
                map.put("goodsTimesId", snatchRecordDetail.getGoodsTimes().getId());
                map.put("goodsTimesName", goodsTimes.getTimes());
                map.put("buyTimes", buyTimes);
                map.put("nums", nums);

                snatchList.add(map);
            }
        }

        data.put("snatchList", snatchList);
        return buildSuccessResult(data);
    }

    /**
     * 5.14. 微信/支付宝取消支付
     * 把预扣的余额或红包退还
     *
     * @param userId     用户id
     * @param outTradeNo 商户订单号
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult cancelPay(int userId, String outTradeNo) throws Exception{
        PayRecord payRecord = payRecordService.getByOutTradeNo(outTradeNo);
        if (payRecord == null) {
            return buildFailureResult(301); // 取消失败，参数错误
        }
        if (payRecord.getUser().getId() != userId) {
            return buildFailureResult(302); // 不能取消非本人的订单
        }
        if (payRecord.getState() != PayResult.UNPAY) {
            return buildFailureResult(303); // 只能取消未支付的订单
        }

        return cancelPay(payRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    public JsonResult cancelPay(PayRecord payRecord) throws Exception{
        int toBalance = 0;
        List<PayWay> payWayList = payRecord.getPayWayList();
        for (PayWay payWay : payWayList) {
            // 退还余额和红包
            if (payWay.getWay() == PayWayType.CURRENCY) {         // 余额支付
                toBalance += payWay.getMoney();
            } else if (payWay.getWay() == PayWayType.REDPACKET) {  // 红包
                RedPack redPack = payWay.getRedPack();
                redPack.addBalance(payWay.getMoney());
                redPack.setState(RedPackState.CAN_USE);
                redPackService.save(redPack);
            }
            payWay.setState(PayResult.CANCEL_PAY);
            payWayService.save(payWay);
        }

        // 退还到余额
        if (toBalance > 0) {
            Member member = memberService.findMember(payRecord.getUser().getId());
            member.addBalance(toBalance);
            memberService.saveMember(member);
        }

        List<SnatchRecordDetail> snatchRecordDetailList = payRecord.getSnatchRecordDetailList();
        if (SimpleUtils.isNotNullList(snatchRecordDetailList)) {
            for (SnatchRecordDetail snatchRecordDetail : snatchRecordDetailList) {
                snatchRecordDetail.setState(PayResult.CANCEL_PAY);
                snatchRecordDetailService.save(snatchRecordDetail);
                SnatchRecord snatchRecord = snatchRecordDetail.getSnatchRecord();
                if (snatchRecord != null) {
                    int surplusBuyTimes = snatchRecord.getBuyTimes() - snatchRecordDetail.getBuyTimes();
                    snatchRecord.setEditTime(DateUtils.getNow());
                    if(surplusBuyTimes < 1) {
                        snatchRecord.setState(PayResult.CANCEL_PAY);
                    }else{
                        snatchRecord.setBuyTimes(surplusBuyTimes);
                        snatchRecord.setState(PayResult.PAY_SUCCESS);
                    }
                    save(snatchRecord);
                }
            }
        }

        payRecord.setToBalance(toBalance);
        payRecord.setState(PayResult.CANCEL_PAY);
        payRecordService.save(payRecord);

        return buildSuccessResult();
    }

    /**
     * 支付成功单夺宝失败，把支付的钱转到余额中
     *
     * @param payRecord
     */
    @Transactional(rollbackFor = Exception.class)
    public void paySuccessToBalance(PayRecord payRecord) throws Exception {
        int toBalance = 0;              // 退回余额中的钱
        int thirdpartPayMoney = 0;      // 第三方支付的金额
        boolean isWeixinPay = true;     // 是否微信支付，否就表示支付宝
        List<PayWay> payWayList = payRecord.getPayWayList();
        for (PayWay payWay : payWayList) {
            // 退还余额和红包
            if (payWay.getWay() == PayWayType.CURRENCY) {         // 余额支付
                toBalance += payWay.getMoney();
            } else if (payWay.getWay() == PayWayType.REDPACKET) {  // 红包
                RedPack redPack = payWay.getRedPack();
                redPack.addBalance(payWay.getMoney());
                redPack.setState(RedPackState.CAN_USE);
                redPackService.save(redPack);
            } else {
                thirdpartPayMoney += payWay.getMoney();
                if (payWay.getWay() == PayWayType.ALIPAY) {
                    isWeixinPay = false;
                }
            }
            payWay.setState(PayResult.PAY_SUCCESS_BUT_CANNOT_SNATCH);
            payWayService.save(payWay);
        }

        toBalance += thirdpartPayMoney;
        User user = payRecord.getUser();

        // 保存充值记录
        if (thirdpartPayMoney > 0) {
            rechargeRecordService.save(thirdpartPayMoney, isWeixinPay,
                    user, RechargeState.RECHARGE_SUCCESS, DateUtils.getNow());
        }

        // 退还到余额
        if (toBalance > 0) {
            Member member = memberService.findMember(user.getId());
            member.addBalance(toBalance);
            memberService.saveMember(member);
        }

        /*// 计算积分
        int jifenAmount = thirdpartPayMoney;
        if (jifenAmount > 0) {
            memberService.recordJifen(payRecord.getUser().getId(), jifenAmount, "夺宝支付");
        }*/

        List<SnatchRecordDetail> snatchRecordDetailList = payRecord.getSnatchRecordDetailList();
        if (SimpleUtils.isNotNullList(snatchRecordDetailList)) {
            for (SnatchRecordDetail snatchRecordDetail : snatchRecordDetailList) {
                snatchRecordDetail.setState(PayResult.PAY_SUCCESS_BUT_CANNOT_SNATCH);
                snatchRecordDetailService.save(snatchRecordDetail);
                SnatchRecord snatchRecord = snatchRecordDetail.getSnatchRecord();
                if (snatchRecord != null) {
                    snatchRecord.setEditTime(DateUtils.getNow());
                    snatchRecord.setState(PayResult.PAY_SUCCESS_BUT_CANNOT_SNATCH);
                    save(snatchRecord);
                }
            }
        }

        String resultDescr = "抱歉，你未能参与本次夺宝，你支付的" + thirdpartPayMoney + "元已转到余额中";
        payRecord.setResultDescr(resultDescr);
        payRecord.setToBalance(toBalance);
        payRecord.setState(PayResult.PAY_SUCCESS_BUT_CANNOT_SNATCH);
        payRecordService.save(payRecord);
    }


    /**
     * 充值
     *
     * @param user
     * @param isWeixinPay
     * @param isAlipayWithH5
     * @param money
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult rechargeOrders(User user, boolean isWeixinPay, boolean isWeixinPayWithH5, boolean isAlipayWithH5,
                                     Integer money, HttpServletRequest request) throws Exception {

        if (isWeixinPay) {          // 微信充值下单
            return rechargeOrdersByWeixin(user, money, request, isWeixinPayWithH5);
        } else{   // 支付宝充值下单
            return rechargeOrdersByAlipay(user.getId(), money, isAlipayWithH5);
        }
    }

    /**
     * 微信充值下单
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult rechargeOrdersByWeixin(User user,
                Integer money, HttpServletRequest request, boolean isH5Pay) throws Exception {
        String outTradeNo = SimpleUtils.getHibernateUUID();
        JsonResult jsonResult = WeixinUitls.order(request, user, outTradeNo, money, false, true);
        if (jsonResult.getState() == AppStatusCode.SC_SUCCESS){
            RechargeRecord rechargeRecord = rechargeRecordService.save(money, true, user,
                    outTradeNo, true, PayUtils.getExpireDate(), RechargeState.UNRECHARGE);

            //添加定时查询订单任务
            addQueryWeixinOrderJob(false, isH5Pay, rechargeRecord.getOutTradeNo(), PayUtils.getQueryJobCronExpression());
        }
        return jsonResult;
    }

    /**
     * 支付宝充值下单
     *
     * @param userId
     * @param money
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult rechargeOrdersByAlipay(Integer userId, Integer money,
                                             boolean isAlipayWithH5) throws Exception{

        String outTradeNo = SimpleUtils.getHibernateUUID();
        /*SortedMap<String, String> params =
                AlipayUtils.buildRequstParams(outTradeNo, money, false);
        String paySign = AlipayUtils.getSign(params);
        AlipayUtils.putElement(params, "sign", paySign);*/

        try {
            rechargeRecordService.save(money, false,
                    new User(userId), outTradeNo, false, null, RechargeState.UNRECHARGE);

            Map<String, String> data = new HashMap<>(2);
            data.put("outTradeNo", outTradeNo);
            data.put("payParams", AlipayUtils.buildRequestUrl(outTradeNo, money, false, isAlipayWithH5));

            LogUtils.logMap("支付宝充值响应客户端参数:", data);
            return buildSuccessResult(data);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return buildFailureResult(buildOneElementMap("errDescr", "支付宝下单失败"));
        }
    }

    private static final Map<String, Lock> thirdpartRechargeSuccessLocks = new ConcurrentHashMap<>();
    /**
     * 充值
     *
     * @param outTradeNo
     * @param payTime
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean recharge(String outTradeNo, Date payTime) throws Exception {

        LockUtils.lock(thirdpartRechargeSuccessLocks, outTradeNo);

        try {

            RechargeRecord rechargeRecord = rechargeRecordService.getByOutTradeNo(outTradeNo);
            if (rechargeRecord == null) {
                logger.error("充值成功回调：outTradeNo[{}]不存在", outTradeNo);
                return true;
            }

            if (rechargeRecord.getState() == Const.RechargeState.RECHARGE_SUCCESS) {
                logger.info("充值商户订单号：{}，【{}】已处理，不需要重复处理 - end",
                        outTradeNo, Const.RechargeState.MAP.get(rechargeRecord.getState()));
                return true;
            }

            logger.info("充值更改前的状态: " + Const.PayResult.MAP.get(rechargeRecord.getState()));

            int money = rechargeRecord.getMoney();
            memberService.addAndSaveBalance(
                    rechargeRecord.getUser().getId(), money);

            rechargeRecord.setPayTime(payTime);
            rechargeRecord.setState(RechargeState.RECHARGE_SUCCESS);
            rechargeRecord.setNeedQueryPayResult(false);
            rechargeRecordService.save(rechargeRecord);

            // 充值送积分(微信充值不送积分)
            //memberService.recordJifen(rechargeRecord.getUser().getId(), money, "充值送积分");

            return true;
        }finally {
            LockUtils.unlock(thirdpartRechargeSuccessLocks, outTradeNo);
        }
    }

    /**
     * 取消充值
     *
     * @param userId     用户id
     * @param outTradeNo 商户订单号
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult cancelRecharge(int userId, String outTradeNo) throws Exception {
        RechargeRecord rechargeRecord = rechargeRecordService.getByOutTradeNo(outTradeNo);
        if (rechargeRecord == null) {
            return buildFailureResult(301); // 取消失败，参数错误
        }
        if (rechargeRecord.getUser().getId() != userId) {
            return buildFailureResult(302); // 不能取消非本人的充值
        }
        if (rechargeRecord.getState() != RechargeState.UNRECHARGE) {
            return buildFailureResult(303); // 只能取消未支付的充值
        }

        return cancelRecharge(rechargeRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    public JsonResult cancelRecharge(RechargeRecord rechargeRecord) throws Exception {
        rechargeRecord.setState(RechargeState.CANCEL_RECHARGE);
        rechargeRecordService.save(rechargeRecord);

        return buildSuccessResult();
    }

    /**
     * 记录全站购买记录，用于计算期号A值
     *
     * @param goodsTimes
     */
    @Transactional(rollbackFor = Exception.class)
    public void recordTop50BuyRecord(GoodsTimes goodsTimes) throws Exception {
        Long maxSnatchTime = goodsTimes.getFullTime();
        List<SnatchRecordDetail> snatchRecordDetailList =
                snatchRecordDetailService.findTop(50, maxSnatchTime);
        for (SnatchRecordDetail snatchRecordDetail : snatchRecordDetailList) {
            User user = snatchRecordDetail.getUser();
            buyRecordService.save(goodsTimes, snatchRecordDetail, user);
        }
    }


    /**
     * 开奖，产生夺宝号码
     *
     * @param goodstimesId
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean produceLuckNum(Integer goodstimesId) throws Exception {
        GoodsTimes goodsTimes = goodsTimesService.get(goodstimesId);
        if (goodsTimes == null ||
                goodsTimes.getState() != GoodsTimesState.WAITING)
            return true;

        logger.info("商品期号【{}】开奖中...", goodstimesId);
        Long cqsscLuckNum = 0L;
        int count = 0;
      String cqsscPeriodNo = goodsTimes.getCqsscPeriodNo();
             if (StringUtils.isBlank(cqsscPeriodNo)) {
            logger.error("cqsscPeriodNo为空...");
            return true;
        }
        while (cqsscLuckNum == null && count < 90) {
//            cqsscLuckNum = CQSSCUtils.getLuckNum(cqsscPeriodNo);
        	
            if (cqsscLuckNum == null) {
                Threads.sleep(RandomUitls.randomInt(5000) + 1);
                cqsscLuckNum=0L;
                count++;
            } else {
                break;
            }
        }

        if (cqsscLuckNum != null) {
            open(cqsscLuckNum, goodsTimes);
        } else {
            // 设置定时任务继续开奖
            if(goodsTimes.getIsFirstOpen()){
                addGoodsTimesOpenJob(goodsTimes, "0 2/10 * * * ?");//从02分开始每隔10分触发

                goodsTimes.setIsFirstOpen(false);
                goodsTimes.setFailDeadlineDate(DateUtils.addDays(goodsTimes.getOpenTime(), 1));
                goodsTimesService.save(goodsTimes);
                logger.info("商品期号ID【{}】无法开奖，未获取中奖号码，设置定时任务(0 2/10 * * * ?)", goodstimesId);
                goodsTimes.setCanGetCqsscno(false);
                return false;
            }
            // 设置bvalue＝0进行开奖
            else if(DateUtilsEx.getNow().compareTo(goodsTimes.getFailDeadlineDate()) >= 0){
                logger.info("商品期号ID【{}】还是无法开奖，设置bvalue＝0进行开奖", goodstimesId);
                open(0L, goodsTimes);
            }
            // 继续定时
            else{
                logger.error("商品期号ID【{}】无法开奖，未获取中奖号码，继续等待。。。", goodstimesId);
                return false;
            }
        }

        return true;
    }

    /**
     * 开奖
     * @param cqsscLuckNum
     * @param goodsTimes
     */
    @Transactional(rollbackFor = Exception.class)
    public void open(Long cqsscLuckNum, GoodsTimes goodsTimes) throws Exception{
        goodsTimes.setBvalue(cqsscLuckNum);
        Integer luckNum = null;
        Integer goodstimesId = goodsTimes.getId();

        Long avalue = buyRecordService.sumTimeValue(goodstimesId);
        if (avalue == null || avalue < 1) {
            Long fullTime = goodsTimes.getFullTime();
            if(fullTime == null){
                fullTime = DateUtilsEx.getNow().getTime();
                goodsTimes.setFullTime(fullTime);
            }
            recordTop50BuyRecord(goodsTimes);
            avalue = buyRecordService.sumTimeValue(goodstimesId);
        }

        Long tmp = (avalue + cqsscLuckNum) % goodsTimes.getTotalTimes();
        luckNum = tmp.intValue() + SNATCH_BASE_NUM;

        Integer winnerId = goodsTimes.getPresetWinnerUserId();
        boolean hasPresetWinner = winnerId != null;
        User winner = null;
        if( hasPresetWinner || hasVirtualUserBuy(goodstimesId)){ // 有虚拟用户参与
            winner = produceWinner(goodsTimes, luckNum);
            boolean needRecompute = false;
            if(hasPresetWinner && winner.getId().intValue() !=
                        goodsTimes.getPresetWinnerUserId().intValue()){ // 指定中奖用户，若非指定用户中奖则需要设置TA中奖

                winner = accountService.getUser(winnerId);
                needRecompute = true;
            }else if( !winner.getIsVirtual() ){ // 不指定中奖用户，非虚拟用户则需要设置虚拟用户中奖
                // 随机抓一个虚拟用户替换购买记录
                winner = getRandom1SnatchVirtualUser(goodstimesId);//accountService.getRandom1VirtualUser();
                needRecompute = true;
            }

            // 重新计算avalue 和 luckNum
            if(needRecompute){
                logger.info("重新计算avalue 和 luckNum");
                // 随机抽取用户的一个夺宝号码作为中奖号码
                int newLuckNum = snatchNumService.getRandom1NumByGoodsTimes(goodstimesId, winner.getId());

                // 随机抽一个被替换的购买记录
                BuyRecord buyRecord = buyRecordService.getRandom1ByGoodsTimes(goodstimesId);

                Date snatchDate = new Date(buyRecord.getSnatchTime());
                snatchDate = DateUtils.addMilliseconds(snatchDate, newLuckNum - luckNum);
                buyRecordService.changeUserAndSnatchTime(buyRecord, winner, snatchDate);
                logger.info("商品期号id:{}，原a值:{}", goodstimesId, avalue);
                avalue = buyRecordService.sumTimeValue(goodstimesId);
                logger.info("商品期号id:{}，新a值:{}", goodstimesId, avalue);
                luckNum = newLuckNum;
            }

        }else{ // 没有虚拟用户参与则计算产生
            winner = produceWinner(goodsTimes, luckNum);
        }

        goodsTimes.setAvalue(avalue);
        goodsTimes.setLuckNum(luckNum);
        logger.info("avalue: {}, cqsscLuckNum: {}, totalTimes: {}",
                avalue, cqsscLuckNum, goodsTimes.getTotalTimes());

        // 等待指定时间开奖
   //     long sleepMillis = goodsTimes.getOpenTime().getTime() - DateUtils.getNow().getTime() - 2000;
        long sleepMillis = goodsTimes.getOpenTime().getTime() - DateUtils.getNow().getTime() - 2000;
        if(sleepMillis > 0){
            logger.info("期号id【{}】未到开奖时间，睡眠{}ms...", goodstimesId, sleepMillis);
            Threads.sleep(sleepMillis);
        }else{
            logger.info("期号id【{}】即时开奖...", goodstimesId);
        }

        saveGoodsTimesWithLuckNum(goodsTimes, winner);

        winngGoodsStateService.initWinngStates(goodsTimes);

        // 通知中奖用户
        noticeWinngUser(goodsTimes);

        //短信通知
        smsSendRecordService.sendSms4NoticeWinng(accountService.getMobileNo(goodsTimes.getWinngUser().getId()));

        logger.info("商品期号【{}】开奖成功，luckNum: {}", goodstimesId, luckNum);
    }

    /**
     * 随机获取一个虚拟夺宝用户
     * @param goodsTimesId
     * @return
     */
    public User getRandom1SnatchVirtualUser(Integer goodsTimesId){
        List<User> list = snatchRecordDao.findTop(1,
                QL_QUERY_RANDOM1_SNATCH_VIRTUAL_USER_BY_GOODS_TIMES, goodsTimesId, Const.PayResult.PAY_SUCCESS);
        return SimpleUtils.isNullList(list) ? null : list.get(0);
    }

    /**
     * 是否有虚拟用户购买
     * @param goodsTimesId
     * @return
     */
    public boolean hasVirtualUserBuy(Integer goodsTimesId){
        Integer amount = snatchRecordDao
                .getVirtualUserBuyAmount(goodsTimesId, Const.PayResult.PAY_SUCCESS);
        return amount != null && amount > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveGoodsTimesWithLuckNum(GoodsTimes goodsTimes, User winngUser) throws Exception {
        logger.info("luckNum: {}", goodsTimes.getLuckNum());
        Integer buyTimes = getBuyTimesByGoodsTimesAndUser(goodsTimes.getId(), winngUser.getId());
        goodsTimes.setWinngUserBuyTimes(buyTimes);
        logger.info("winngUser: {}, buyTimes: {}", winngUser.getId(), buyTimes);

        goodsTimes.setWinngUser(winngUser);
        goodsTimes.setWinngUserIdentity(winngUser.getUserName());
        goodsTimes.setWinngUserName(winngUser.getNickName());
        goodsTimes.setWinngUserHeadImg(memberService.getHeadImgByUser(winngUser.getId()));
        goodsTimes.setWinngState(WinngState.CONFIRM_ADDRESS);
        goodsTimes.setState(GoodsTimesState.OVER);

        goodsTimesService.createHtml(goodsTimes);
    }

    /**
     * 产生中奖用户
     * @param goodsTimes
     * @param luckNum
     * @return
     */
    public User produceWinner(GoodsTimes goodsTimes, Integer luckNum){
        SnatchNum snatchNum = snatchNumService.getByGoodsTimesAndNum(goodsTimes.getId(), luckNum);
        User winngUser = snatchNum.getUser();
        return winngUser;
    }

    /**
     * 查询微信支付订单
     * @param outTradeNo
     * @return 是否需要删除定时任务
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean queryWeixinPayResult(String outTradeNo, boolean isH5Pay) throws Exception{
        logger.info("查询微信支付订单： PayRecord outTradeNo: {}", outTradeNo);
        PayRecord payRecord = payRecordService.getByOutTradeNo(outTradeNo);
        if(payRecord == null ||
                !payRecord.getNeedQueryPayResult() ||
                DateUtilsEx.getNow().compareTo(payRecord.getQueryDeadlineTime()) > 0 ){
            return true;
        }

        Object[] result = WeixinUitls.queryWeixinPayResult(
                payRecord.getOutTradeNo(), isH5Pay ? Const.WX_H5_API_KEY : Const.WX_API_KEY);
        if(result != null){
            payRecord.setNeedQueryPayResult(false);
            if( (boolean)result[0] ){
                computeBuyTimsAndSantch(payRecord.getOutTradeNo(), (Date)result[1]);
            }else{
                cancelPay(payRecord);
            }
        }else{
            return false;
        }
        return true;
    }

    /**
     * 查询微信充值支付订单
     * @param outTradeNo
     * @return 是否需要删除定时任务
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean queryWeixinRechargeResult(String outTradeNo, boolean isH5Pay) throws Exception{
        logger.info("查询微信充值支付订单： Recharge outTradeNo: {}", outTradeNo);
        RechargeRecord rechargeRecord = rechargeRecordService.getByOutTradeNo(outTradeNo);
        if(rechargeRecord == null ||
                !rechargeRecord.getNeedQueryPayResult() ||
                DateUtilsEx.getNow().compareTo(rechargeRecord.getQueryDeadlineTime()) > 0 ){
            return true;
        }

        Object[] result = WeixinUitls.queryWeixinPayResult(
                rechargeRecord.getOutTradeNo(), isH5Pay ? Const.WX_H5_API_KEY : Const.WX_API_KEY);
        if(result != null){
            rechargeRecord.setNeedQueryPayResult(false);
            if( (boolean)result[0] ){
                recharge(outTradeNo, (Date)result[1]);
            }else{
                cancelRecharge(rechargeRecord);
            }
        }else{
            return false;
        }
        return true;
    }

    /**
     * 添加查询微信支付订单定时任务
     * @param isPay 支付支付，否则为充值
     * @param outTradeNo
     * @param cronExpression
     */
    private void addQueryWeixinOrderJob(boolean isPay, boolean isH5Pay, String outTradeNo, String cronExpression){
        Map<String, String> params = new HashMap<String, String>(1); //0 2/10 * * * ?
        params.put("outTradeNo", outTradeNo);
        params.put("isH5Pay", String.valueOf(isH5Pay));
        QuartzEntity quartzEntity = null;
        Class clazz = null;
        if(isPay){
            quartzEntity= new QuartzPayRecord(outTradeNo, cronExpression);
            clazz = JobPayRecord.class;
        }else{
            quartzEntity= new QuartzRecharge(outTradeNo, cronExpression);
            clazz = JobRecharge.class;
        }
        QuartzUtils.registerJob(quartzEntity, clazz, params);
    }

    /**
     * 虚拟用户夺宝
     * @param user
     * @param goodsTimesId
     * @param buyTimes
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> virtualUserSnatch(User user, Integer goodsTimesId,
                                  Integer buyTimes) throws BizException, Exception{
        GoodsTimes goodsTimes = goodsTimesService.get(goodsTimesId);
        if (goodsTimes.getIsTenYuan() && buyTimes % 10 != 0) {
            throw new BizException(102, "十元夺宝的商品购买人次必须能被10整除");
        }

        if (goodsTimes.getState() == GoodsTimesState.GOING) { // 进行中
            int actualBuyTimes = computeGoodsTimesBuyTimes(goodsTimes, buyTimes);
            if(actualBuyTimes > 0){
                // 更新商品期号夺宝进度
                goodsTimesService.computeAndSaveBuyTimes(goodsTimes, buyTimes);

                String ip = null;
                return generateSnatchRecordAndNum(user, goodsTimes, actualBuyTimes, null, ip);
            }
        }

        return null;
    }

    /**
	 * 通知中奖用户
	 * @param goodsTimes
	 */
    private void noticeWinngUser(final GoodsTimes goodsTimes){
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //List<String> alias = Arrays.asList(goodsTimes.getWinngUser().getId().toString());
                    List<String> alias=new ArrayList<String>(1);
                    alias.add(goodsTimes.getWinngUser().getId().toString());
                    
                    Map<String, String> extras = new HashMap<>(4);
                    extras.put(Const.PUSH_MESSAGE_TYPE, Const.WINNG_TYPE);
                    extras.put(Const.WINNG_TIMES, goodsTimes.getTimes().toString());
                    extras.put(Const.WINNG_GOODS_NAME, goodsTimes.getGoodsName());
                    extras.put(Const.WINNG_GOODS_TIMES_ID, goodsTimes.getId().toString());

                    JPushUtils.sendPushNotification(alias, extras);
                    logger.info("中奖用户ID - " + goodsTimes.getWinngUser().getId().toString());
                }catch (Exception ex){
                    logger.error(ex.getMessage(), ex);
                }
            }
        });
	}

    @Transactional
    public void updateGoodsName(Integer goodsId, String goodsName){
        snatchRecordDao.executeSQLUpdate(SQL_UPDATE_GOODS_NAME, goodsId, goodsName);
    }
}
