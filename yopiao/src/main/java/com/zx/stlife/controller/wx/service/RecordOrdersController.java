package com.zx.stlife.controller.wx.service;

import static com.zx.stlife.constant.Const.canPayByAlipay;
import static com.zx.stlife.constant.Const.canPayByWeixin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.base.jpa.query.Page;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.base.UserWxUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.app.base.BaseRestController;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.controller.common.BizCommon;
import com.zx.stlife.entity.member.Member;
import com.zx.stlife.entity.service.Activity;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.order.SnatchRecordService;
import com.zx.stlife.service.service.ActivityService;
import com.zx.stlife.service.sys.AccountService;

@Controller
@RequestMapping("/wx/service/record")
public class RecordOrdersController extends BaseRestController {

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private SnatchRecordService snatchRecordService;

	/**
	 * 充值
	 * @param userId
	 * @param payWay
	 * @param money
	 * @return
	 */
	@RequestMapping(value="/rechargeOrdersWX",method=RequestMethod.POST)
	public JsonResult rechargeOrdersWX(@RequestParam("userId") Integer userId,
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
			return snatchRecordService.rechargeOrders(user, isWeixinPay, false, isAlipayWithH5, money, request);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
}
