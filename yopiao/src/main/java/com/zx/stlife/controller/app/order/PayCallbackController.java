package com.zx.stlife.controller.app.order;

import com.base.modules.util.DateUtilsEx;
import com.base.modules.util.Threads;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.app.base.BaseRestController;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.order.PayRecord;
import com.zx.stlife.entity.order.SnatchRecordDetail;
import com.zx.stlife.service.order.PayRecordService;
import com.zx.stlife.service.order.SnatchNumService;
import com.zx.stlife.service.order.SnatchRecordDetailService;
import com.zx.stlife.service.order.SnatchRecordService;
import com.zx.stlife.tools.LogUtils;
import com.zx.stlife.tools.RandomUitls;
import com.zx.stlife.tools.RequestUtils;
import com.zx.stlife.tools.alipay.AlipayNotify;
import com.zx.stlife.tools.quartz.QuartzUtils;
import com.zx.stlife.tools.quartz.entity.QuartzPayRecord;
import com.zx.stlife.tools.quartz.entity.QuartzRecharge;
import com.zx.stlife.tools.thread.ThreadWorkUtils;
import com.zx.stlife.tools.weixin.PayCommonUtils;
import com.zx.stlife.tools.weixin.WeixinUitls;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.StaleObjectStateException;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.font.TrueTypeFont;

import javax.persistence.LockTimeoutException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.zx.stlife.controller.app.base.JsonResultUtils.buildFailureResult;

/**
 * 支付回调
 *
 * @author micheal cao
 */
@RestController
@RequestMapping("/pay/callback")
public class PayCallbackController extends BaseRestController {

    @Autowired
    private SnatchRecordService snatchRecordService;

    @Autowired
    private PayRecordService payRecordService;

    @Autowired
    private SnatchRecordDetailService snatchRecordDetailService;

    @Autowired
    private SnatchNumService snatchNumService;

    /**
     * 微信APP支付成功回调方法
     * @param request
     * @param response
     * @throws JDOMException
     * @throws IOException
     */
    @RequestMapping(value = "/weixin/buy", method = RequestMethod.POST)
    public void weixinAppPayCallBack(HttpServletRequest request, HttpServletResponse response)
            throws JDOMException, IOException {
        weixinPayCallBack(request, response, false);
    }

    /**
     * 微信H5支付成功回调方法
     * @param request
     * @param response
     * @throws JDOMException
     * @throws IOException
     */
    @RequestMapping(value = "/weixin/buyH5", method = RequestMethod.POST)
    public void weixinH5PayCallBack(HttpServletRequest request, HttpServletResponse response)
            throws JDOMException, IOException {
        weixinPayCallBack(request, response, true);
    }

    private void weixinPayCallBack(HttpServletRequest request, HttpServletResponse response, boolean isH5Pay)
            throws JDOMException, IOException {
        Map<String, String> params = RequestUtils.collectXmlParamsToMap(request);
        if (params == null || params.size() == 0) {
            logger.error("无法获取微信充值成功回调结果");
            return;
        }

        if (WeixinUitls.SUCCESS.equalsIgnoreCase(params.get("result_code"))
                && WeixinUitls.SUCCESS.equalsIgnoreCase(params.get("return_code"))) {

            if( WeixinUitls.isValidSign((TreeMap)params, isH5Pay ? Const.WX_H5_API_KEY : Const.WX_API_KEY) ){
                // 对订单的处理
                String outTradeNo = params.get("out_trade_no");// 获取支付成功的商户订单号
                String timeEndStr = params.get("time_end");
                Date payTime = DateUtilsEx.toDateFromYYYYMMDDHHMMSS(timeEndStr,
                        StringUtils.isBlank(timeEndStr) ? DateUtilsEx.getNow() : null);
                while (true) {
                    // 支付成功，根据PayRecord和夺宝记录，判断是否已满等，相应去生成夺宝号码或充值到余额中
                    try {
                        Map<String, Object> resultMap = snatchRecordService.computeBuyTimsAndSantch(outTradeNo, payTime);
                        if((boolean)resultMap.get("isSccuess")) {
                            QuartzUtils.removeJob(new QuartzPayRecord(outTradeNo, ""));
                            notifyWeixinCallbackSuccess(response);

                            List<GoodsTimes> goodsTimesList = (List<GoodsTimes>)resultMap.get("preopenGoodsTimesList");
                            //若有待开奖的期号，则开启下一期和产生全站50条购买记录
                            ThreadWorkUtils.createNextGoodsTimesAndBuyRecord(goodsTimesList);
                        }

                        logger.info("微信支付回调 - 处理成功，商户订单号：{} - end", outTradeNo);
                        break;
                    } catch (ObjectOptimisticLockingFailureException | StaleObjectStateException ex) {
                        logger.info("微信支付回调 - 出现并发，商户订单号：{}", outTradeNo);
                        Threads.sleep(RandomUitls.randomInt(100));
                    } catch(CannotAcquireLockException | LockTimeoutException ex){
                        logger.info("微信支付回调 - 出现死锁，等待继续执行，商户订单号：{}", outTradeNo);
                        Threads.sleep(RandomUitls.randomInt(300));
                    } catch (Exception ex) {
                        logger.error("微信支付回调 - 处理失败，商户订单：{}(411)", outTradeNo);
                        logger.error(ex.getMessage(), ex);

                        // 退钱到余额中
                        paySuccessToBalance(outTradeNo);
                        notifyWeixinCallbackSuccess(response);

                        break;
                    }
                }
            }else{
                LogUtils.logMap("微信支付回调 - 签名验证失败，参数列表如下：", params);
            }
        }else{
            LogUtils.logMap("微信支付回调 - 参数错误：", params);
        }
    }

    /**
     * 微信app充值成功回调方法
     * @param request
     * @param response
     * @throws JDOMException
     * @throws IOException
     */
    @RequestMapping(value = "/weixin/recharge", method = RequestMethod.POST)
    public void weixinAppRechargeCallBack(HttpServletRequest request, HttpServletResponse response)
            throws JDOMException, IOException {
        weixinRechargeCallBack(request, response, false);
    }

    /**
     * 微信H5充值成功回调方法
     * @param request
     * @param response
     * @throws JDOMException
     * @throws IOException
     */
    @RequestMapping(value = "/weixin/rechargeH5", method = RequestMethod.POST)
    public void weixinH5RechargeCallBack(HttpServletRequest request, HttpServletResponse response)
            throws JDOMException, IOException {
        weixinRechargeCallBack(request, response, true);
    }

    private void weixinRechargeCallBack(HttpServletRequest request, HttpServletResponse response, boolean isH5Pay)
            throws JDOMException, IOException {
        Map<String, String> params = RequestUtils.collectXmlParamsToMap(request);
        if (params == null || params.size() == 0) {
            logger.error("无法获取微信充值成功回调结果");
            return;
        }

        if (WeixinUitls.SUCCESS.equalsIgnoreCase(params.get("result_code"))
                && WeixinUitls.SUCCESS.equalsIgnoreCase(params.get("return_code"))) {

            if( WeixinUitls.isValidSign((TreeMap)params, isH5Pay ? Const.WX_H5_API_KEY : Const.WX_API_KEY) ) {
                // 对订单的处理
                String outTradeNo = params.get("out_trade_no");// 获取支付成功的商户订单号
                String timeEndStr = params.get("time_end");
                Date payTime = DateUtilsEx.toDateFromYYYYMMDDHHMMSS(timeEndStr,
                        StringUtils.isBlank(timeEndStr) ? DateUtilsEx.getNow() : null);
                while (true) {

                    try {
                        boolean isSuccess = snatchRecordService.recharge(outTradeNo, payTime);
                        if(isSuccess) {
                            QuartzUtils.removeJob(new QuartzRecharge(outTradeNo, ""));
                            notifyWeixinCallbackSuccess(response);
                        }
                        logger.info("微信充值回调 - 处理成功，商户订单号：{} - end", outTradeNo);
                        break;
                    } catch (ObjectOptimisticLockingFailureException | StaleObjectStateException ex) {
                        logger.info("微信充值回调 - 出现并发，商户订单号：{}", outTradeNo);
                        Threads.sleep(RandomUitls.randomInt(100));
                    } catch(CannotAcquireLockException | LockTimeoutException ex){
                        logger.info("微信充值回调 - 出现死锁，等待继续执行，商户订单号：{}", outTradeNo);
                        Threads.sleep(RandomUitls.randomInt(300));
                    } catch (Exception ex) {
                        logger.error("微信充值回调 - 处理失败，商户订单号：{}(412)", outTradeNo);
                        logger.error(ex.getMessage(), ex);

                        notifyWeixinCallbackSuccess(response);
                        break;
                    }
                }
            }else{
                LogUtils.logMap("微信充值回调 - 签名验证失败，参数列表如下：", params);
            }
        }else{
            LogUtils.logMap("微信充值回调 - 参数错误：", params);
        }
    }

    /**
     * TODO:支付宝回调方法
     * 支付宝回调方法
     * @param request
     * @param response
     * @throws JDOMException
     * @throws IOException
     */
    @RequestMapping(value = "/alipay/buy", method = RequestMethod.POST)
    public void alipayCallBack(HttpServletRequest request, HttpServletResponse response)
            throws JDOMException, IOException {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = RequestUtils.collectParams(request);

        if (AlipayNotify.verify(params) &&
                Const.ALIPAY_PARTNER.equals(params.get("seller_id"))) {//验证成功
            String tradeStatus = request.getParameter("trade_status");    // 交易状态
            logger.info("支付宝参数验证成功, tradeStatus: {}", tradeStatus);
            if (StringUtils.equals(tradeStatus, "TRADE_SUCCESS") ||
                    StringUtils.equals(tradeStatus, "TRADE_FINISHED")) {

                String outTradeNo = request.getParameter("out_trade_no");
                while (true) {

                    Date payTime = DateUtilsEx.parseDate(params.get("gmt_payment"), DateUtilsEx.getNow());
                    // 支付成功，根据PayRecord和夺宝记录，判断是否已满等，相应去生成夺宝号码或充值到余额中
                    try {
                        Map<String, Object> resultMap = snatchRecordService.computeBuyTimsAndSantch(outTradeNo, payTime);
                        if((boolean)resultMap.get("isSccuess")) {
                            notifyAlipayCallbackSuccess(response);

                            List<GoodsTimes> goodsTimesList = (List<GoodsTimes>)resultMap.get("preopenGoodsTimesList");
                            //若有待开奖的期号，则开启下一期和产生全站50条购买记录
                            ThreadWorkUtils.createNextGoodsTimesAndBuyRecord(goodsTimesList);
                        }
                        logger.info("支付宝支付回调 - 处理成功，商户订单号：{} - end", outTradeNo);
                        break;
                    } catch (ObjectOptimisticLockingFailureException | StaleObjectStateException ex) {
                        logger.info("支付宝支付回调 - 出现并发，商户订单号：{}", outTradeNo);
                        Threads.sleep(RandomUitls.randomInt(100));
                    } catch(CannotAcquireLockException | LockTimeoutException ex){
                        logger.info("支付宝支付回调 - 出现死锁，等待继续执行，商户订单号：{}", outTradeNo);
                        Threads.sleep(RandomUitls.randomInt(300));
                    } catch (Exception ex) {
                        logger.error("支付宝支付回调 - 处理失败，商户订单号：{}(421)", outTradeNo);
                        logger.error(ex.getMessage(), ex);

                        // 把钱转到余额中
                        paySuccessToBalance(outTradeNo);
                        break;
                    }
                }
            }
        } else {//验证失败
            LogUtils.logMap("支付宝支付回调 - 验证失败，参数列表：", params);
            notifyAlipayCallbackFail(response);
        }
    }

    private void paySuccessToBalance(String outTradeNo){
        logger.info("微信/支付宝回调，执行业务出错，把支付的钱退还到余额中。");
        while(true) {
            PayRecord payRecord = payRecordService.getByOutTradeNo(outTradeNo);
            try {
                snatchRecordService.paySuccessToBalance(payRecord);
                logger.info("把支付的钱退还到余额中, 执行成功  - end");
                break;
            } catch (ObjectOptimisticLockingFailureException | StaleObjectStateException ex) {
                logger.info("把支付的钱退还到余额中出现并发，商户订单号：{}", outTradeNo);
                Threads.sleep(RandomUitls.randomInt(100));
            } catch(CannotAcquireLockException | LockTimeoutException ex){
                logger.info("把支付的钱退还到余额中出现死锁，等待继续执行，商户订单号：{}", outTradeNo);
                Threads.sleep(RandomUitls.randomInt(300));
            } catch (Exception ex2) {
                logger.error("把支付的钱退还到余额中执行错误，payRecord Id:{}", payRecord.getId());
                logger.error(ex2.getMessage() , ex2);
                break;
            }
        }
    }

    /**
     * 支付宝充值成功回调方法
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/alipay/recharge", method = RequestMethod.POST)
    public void alipayRechargeCallBack(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = RequestUtils.collectParams(request);

        if (AlipayNotify.verify(params) &&
                Const.ALIPAY_PARTNER.equals(params.get("seller_id"))) {//验证成功
            String tradeStatus = request.getParameter("trade_status");    // 交易状态
            logger.info("支付宝参数验证成功, tradeStatus: {}", tradeStatus);
            if (StringUtils.equals(tradeStatus, "TRADE_SUCCESS") ||
                    StringUtils.equals(tradeStatus, "TRADE_FINISHED")) {

                String outTradeNo = request.getParameter("out_trade_no");
                Date payTime = DateUtilsEx.parseDate(params.get("gmt_payment"), DateUtilsEx.getNow());
                while (true) {
                    // 支付成功，根据PayRecord和夺宝记录，判断是否已满等，相应去生成夺宝号码或充值到余额中
                    try {
                        boolean isSuccess = snatchRecordService.recharge(outTradeNo, payTime);
                        if(isSuccess) {
                            notifyAlipayCallbackSuccess(response);
                        }
                        logger.info("支付宝充值回调 - 处理成功，商户订单号：{} - end", outTradeNo);
                        break;
                    } catch (ObjectOptimisticLockingFailureException | StaleObjectStateException ex) {
                        logger.info("支付宝充值回调 - 出现并发，商户订单号：{}", outTradeNo);
                        Threads.sleep(RandomUitls.randomInt(100));
                    } catch(CannotAcquireLockException | LockTimeoutException ex){
                        logger.info("支付宝充值回调 - 出现死锁，等待继续执行，商户订单号：{}", outTradeNo);
                        Threads.sleep(RandomUitls.randomInt(300));
                    } catch (Exception ex) {
                        logger.error("支付宝充值回调 - 处理失败，商户订单号：{}(422)", outTradeNo);
                        logger.error(ex.getMessage(), ex);
                        break;
                    }
                }
            }
        } else {//验证失败
            LogUtils.logMap("支付宝充值回调 - 验证失败，回调参数列表", params);
            notifyAlipayCallbackFail(response);
        }
    }

    public void notifyWeixinCallbackSuccess(HttpServletResponse response) throws IOException {
        response.getWriter().write(PayCommonUtils.setXML("SUCCESS", "")); // 告诉微信服务器，我收到信息了，不要在调用回调action了
    }

    public void notifyAlipayCallbackSuccess(HttpServletResponse response) throws IOException {
        response.getWriter().write("success"); // 告诉微信服务器，我收到信息了，不要在调用回调action了
    }

    public void notifyAlipayCallbackFail(HttpServletResponse response) throws IOException {
        response.getWriter().write("fail"); // 告诉微信服务器，我收到信息了，不要在调用回调action了
    }
}
