package com.zx.stlife.controller.app.order;

import com.base.modules.util.ConvertUtils;
import com.base.modules.util.SimpleUtils;
import com.base.modules.util.Threads;
import com.zx.stlife.base.exception.BizException;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.app.base.AppStatusCode;
import com.zx.stlife.controller.app.base.BaseRestController;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.controller.common.BizCommon;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.order.SnatchListItemService;
import com.zx.stlife.service.order.SnatchRecordService;
import com.zx.stlife.service.sys.AccountService;
import com.zx.stlife.tools.RandomUitls;
import com.zx.stlife.tools.thread.ThreadWorkUtils;
import com.zx.stlife.vo.snatch.SnatchRequestVo;
import com.zx.stlife.vo.snatch.SnatchVo;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.LockTimeoutException;
import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.zx.stlife.constant.Const.canPayByAlipay;
import static com.zx.stlife.constant.Const.canPayByWeixin;
import static com.zx.stlife.controller.app.base.JsonResultUtils.buildFailureResult;

/**
 * 
 * @author micheal cao
 *
 */
@RestController
@RequestMapping("/app/snatch")
public class SnatchRestController extends BaseRestController{

	@Autowired
	private SnatchRecordService snatchRecordService;
	
	@Autowired
	private AccountService accountService;

	@Autowired
	private SnatchListItemService snatchListItemService;

	/**
	 * 下单/支付
	 * @param snatchRequestVo
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/prepay", method=RequestMethod.POST)
	public JsonResult prepay(@RequestBody SnatchRequestVo snatchRequestVo,
							 HttpServletRequest request)
	{
		User user = accountService.getUser(snatchRequestVo.getUserId());
		return BizCommon.pay(user, snatchRequestVo, snatchListItemService, snatchRecordService, request, false);
	}

	/**
	 *
	 * 5.14. 微信/支付宝取消支付
	 * @param userId        用户id
	 * @param outTradeNo    商户订单号
	 * @return
	 */
	@RequestMapping(value="/getPayResult",method=RequestMethod.GET)
	public JsonResult prepay(@RequestParam("userId") Integer userId,
							 @RequestParam("outTradeNo") String outTradeNo) {
		try{
			return snatchRecordService.getPayResult(userId, outTradeNo);
		}catch (Exception ex){
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 5.14. 微信/支付宝取消支付
	 * @param userId        用户id
	 * @param outTradeNo    商户订单号
	 * @return
	 */
	@RequestMapping(value="/cancel",method=RequestMethod.POST)
	public JsonResult cancelPay(@RequestParam("userId") Integer userId,
							 @RequestParam("outTradeNo") String outTradeNo) {
		return BizCommon.cancelPay(snatchRecordService, userId, outTradeNo);
	}

	/**
	 * 充值
	 * @param userId
	 * @param payWay
	 * @param money
	 * @return
	 */
	@RequestMapping(value="/rechargeOrders",method=RequestMethod.POST)
	public JsonResult prepay(@RequestParam("userId") Integer userId,
							 @RequestParam("payWay") Byte payWay,
							 @RequestParam("money") Integer money,
							 @RequestParam(value = "isAlipayWithH5", required = false) String isAlipayWithH5Str,
							 HttpServletRequest request)
	{

		boolean isWeixinPay = payWay == Const.PayWayType.WEIXIN;
		boolean isAliPay =  payWay == Const.PayWayType.ALIPAY;

		if( !isWeixinPay && !isAliPay){
			return buildFailureResult(301); // 仅支持微信或支付宝支付充值
		}

		if ( money < 1 || money > 10000000) {
			return buildFailureResult(302); // 充值金额范围在1-10000000之间
		}

		User user = accountService.getUser(userId);
		if(user == null){
			return buildFailureResult(330); // 用户错误
		}

		if(isWeixinPay && !canPayByWeixin){
			return buildFailureResult(311);// 不支持微信支付
		}
		if(isAliPay && !canPayByAlipay){
			return buildFailureResult(312);// 不支持支付宝支付
		}

		boolean isAlipayWithH5 = SimpleUtils.stringToboolean(isAlipayWithH5Str);
		try {
			return snatchRecordService.rechargeOrders(user, isWeixinPay, true, isAlipayWithH5, money, request);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 5.17. 取消充值
	 * @param userId        用户id
	 * @param outTradeNo    商户订单号
	 * @return
	 */
	@RequestMapping(value="/cancel/recharge",method=RequestMethod.POST)
	public JsonResult cancelRecharge(@RequestParam("userId") Integer userId,
								@RequestParam("outTradeNo") String outTradeNo) {
		while (true) {
			try {
				return snatchRecordService.cancelRecharge(userId, outTradeNo);
			} catch (ObjectOptimisticLockingFailureException | StaleObjectStateException ex) {
				logger.info("取消充值 - 出现并发访问...");
				Threads.sleep(RandomUitls.randomInt(100));
			} catch(CannotAcquireLockException | LockTimeoutException ex){
				logger.info("取消充值 - 出现死锁，等待继续执行");
				Threads.sleep(RandomUitls.randomInt(300));
			} catch (Exception ex) {
				logger.error(ex.getClass().getName());
				logger.error("取消充值 - 系统错误：" + ex.getMessage(), ex);
				return buildFailureResult();
			}
		}
	}
}
