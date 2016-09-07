package com.zx.stlife.controller.app.record;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.jpa.query.Page;
import com.zx.stlife.controller.app.base.BaseRestController;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.entity.member.Member;
import com.zx.stlife.entity.record.Jifen;
import com.zx.stlife.entity.sys.Config;
import com.zx.stlife.service.member.MemberService;
import com.zx.stlife.service.record.JifenService;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.tools.DateUtils;
import com.zx.stlife.tools.PageUtils;

/**
 * 积分服务API
 */

@RestController
@RequestMapping("/app/record/jifen")
public class JifenRestController extends BaseRestController {

	@Autowired
	private JifenService jifenService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ConfigService configService;
	/**
	 * 查看积分记录
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JsonResult getJifenRecord(@RequestParam("userId") Integer userId,
			@RequestParam("timestamp") String timestamp, Page<Jifen> page) {
		try {
			Date date = DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);
			jifenService.findPageByUser(page, date, userId);
			return PageUtils.buildPage(page, "id", "amount", "descr", "abtainTime");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 查看个人积分
	 */
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public JsonResult getUserJifen(@RequestParam("userId") Integer userId) {
		try {
			Integer jifen = jifenService.findUserJifen(userId);
			Map<String, Integer> data = new HashMap<String, Integer>(1);
			data.put("jifen", jifen);
			return buildSuccessResult(data);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
	
	/**
	 * 查看提现手续费
	 */
	@RequestMapping(value = "/queryFee", method = RequestMethod.GET)
	public JsonResult getWithDrawFee() {
		try {
			Config config = configService.get(ConfigService.WITHDRAW_FEE);
			Integer fee = 0;
			if (config != null && StringUtils.isNotEmpty(config.getValue())) {
				fee = Integer.parseInt(config.getValue());
			}
			
			Map<String, Integer> data = new HashMap<String, Integer>(1);
			data.put("drawFee", fee);
			return buildSuccessResult(data);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 积分抽红包
	 */
	@RequestMapping(value = "/draw", method = RequestMethod.POST)
	public JsonResult drawRedPack(@RequestParam("userId") Integer userId) {
		try {
			Integer redPackMoney = jifenService.drawRedPack(userId);
			if (redPackMoney == null) {
				return buildFailureResult(301);
			}
			Map<String, Integer> data = new HashMap<String, Integer>(1);
			data.put("redPackMoney", redPackMoney);
			return buildSuccessResult(data);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
	
	/**
	 * 积分兑换
	 * Created by superT on 16/5/5.
	 */
	@RequestMapping(value="/exchange",method=RequestMethod.GET)
	public JsonResult recordExchange(@RequestParam("userId") Integer userId,@RequestParam("dhjifen") Integer dhjifen) 
	{
		try
		{
			Integer jifen = jifenService.findUserJifen(userId);
			if(jifen<dhjifen)
			{
				return buildFailureResult(301); // 积分不足
			}else
			{
				Integer balance=jifenService.jifenExchange(dhjifen,userId);
				if(balance!=null)
				{
					Map<String, Integer> data = new HashMap<String, Integer>(2);
					data.put("jifen", jifen-dhjifen);
					data.put("balance", balance);
					return buildSuccessResult(data);
				}
				else
				{
					return buildFailureResult(301);//积分不足
				}
			}
			
		}catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
		
	}
	
	/**
	 * 积分提现
	 * Created by superT on 16/5/5.
	 */
	@RequestMapping(value="/withdrawals",method=RequestMethod.GET)
	public JsonResult withdrawals(@RequestParam("userId") Integer userId,@RequestParam("dhjifen") Integer dhjifen) 
	{
		try
		{
			Member member = memberService.findMember(userId);
			if(member!=null)
			{
				if (member.getType() == null) {
					return buildFailureResult(303);//请先绑定银行卡
				}
				
				if(member.getType()==1 || member.getType() == 2)
				{
					Integer jifen = jifenService.findUserJifen(userId);
					if(jifen<dhjifen)
					{
						return buildFailureResult(301); // 积分不足
					}else
					{
						jifen=jifenService.jifenWithdrawals(dhjifen,userId);
						memberService.saveWithdraw(userId, dhjifen);
						Map<String, Integer> data = new HashMap<String, Integer>(1);
						data.put("jifen", jifen);
						return buildSuccessResult(data);
					}
				}else
				{
					return buildFailureResult(303);//请先绑定支付宝
				}
			}else
			{
				return buildFailureResult(301);//积分不足
			}
		}catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
		
}
