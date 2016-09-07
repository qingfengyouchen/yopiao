package com.zx.stlife.controller.app.record;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zx.stlife.tools.DateUtils;
import com.zx.stlife.tools.PageUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.jpa.query.Page;
import com.base.modules.util.ConvertUtils;
import com.base.modules.util.DateUtilsEx;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.controller.app.base.BaseRestController;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.entity.record.RechargeRecord;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.member.MemberService;
import com.zx.stlife.service.record.RechargeRecordService;

/**
 * 充值记录服务API
 */

@RestController
@RequestMapping("/app/record/recharge")
public class RechargeRecordRestController extends BaseRestController {

	@Autowired
	private RechargeRecordService rechargeRecordService;

	
	/**
	 * 获取充值记录列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JsonResult getRechargeRecords(
			@RequestParam("userId") Integer userId,
			@RequestParam(value = "timestamp", required = false) String timestamp,
			Page<RechargeRecord> page) {

		try {
			Date date = DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);
			rechargeRecordService.findRechargeRecords(userId, date, page);
			return PageUtils.buildPage(page, "payWay", "money", "payTimeStr");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
	
	/**
	 * 获取所有用户充值记录列表
	 */
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public JsonResult getRechargeRecordsAll(
			@RequestParam(value = "timestamp", required = false) String timestamp,
			Page<RechargeRecord> page) {

		try {
			Date date = DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);
			rechargeRecordService.findRechargeRecordsAll(date, page);
			return PageUtils.buildPage(page, "payWay", "money", "payTimeStr");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	@RequestMapping(value = "/result", method = RequestMethod.GET)
	public JsonResult getResult(
			@RequestParam("outTradeNo") String outTradeNo) {
		try {
			RechargeRecord rechargeRecord = rechargeRecordService.getByOutTradeNo(outTradeNo);
			if(rechargeRecord == null ){
				return buildFailureResult(301);
			}

			Map<String, Object> data = ConvertUtils.convertEntityToMap(rechargeRecord,
					new String[]{"state", "result"},
					new String[]{"money"});
			return buildSuccessResult(data);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
	
	

}
