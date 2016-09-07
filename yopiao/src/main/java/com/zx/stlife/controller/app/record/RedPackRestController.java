package com.zx.stlife.controller.app.record;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.jpa.query.Page;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.controller.app.base.BaseRestController;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.entity.record.RedPack;
import com.zx.stlife.service.record.RedPackService;
import com.zx.stlife.tools.DateUtils;
import com.zx.stlife.tools.PageUtils;

/**
 * 红包服务API
 */

@RestController
@RequestMapping("/app/record/redPack")
public class RedPackRestController extends BaseRestController {

	@Autowired
	private RedPackService redPackService;

	/**
	 * 查看红包列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JsonResult list(@RequestParam("userId") Integer userId,
			@RequestParam("timestamp") String timestamp,
			@RequestParam("type") Byte type, Page<RedPack> page) {
		try {
			Date date = DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);
			redPackService.findList(userId, date, page, type);
			if (SimpleUtils.isNullList(page.getResult())) {
				return buildSuccessResult();
			}
			return PageUtils.buildPage(page,
					new String[] { "id", "redPackId" },
					new String[] { "total" },
					new String[] { "balance" },
					new String[] { "category" },
					new String[] { "sourceType" },
					new String[] { "state" },
					new String[] { "expireTimeStr" });
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return buildFailureResult();
		}
	}

	/**
	 * 分享抽夺宝红包
	 */
	@RequestMapping(value = "/drawByShare", method = RequestMethod.POST)
	public JsonResult drawByShare(@RequestParam("userId") Integer userId) {
		try {
			return redPackService.drawRedPackByShare(userId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 推荐码抽夺宝红包
	 */
	@RequestMapping(value = "/drawByReferral", method = RequestMethod.POST)
	public JsonResult drawByReferral(@RequestParam("userId") Integer userId,
			@RequestParam("redPackId") Integer redPackId) {
		try {
			return redPackService.drawRedPackByReferral(userId, redPackId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 获取生活类红包
	 */
	@RequestMapping(value = "/drawByLife", method = RequestMethod.POST)
	public JsonResult drawByLife(@RequestParam("userId") Integer userId) {
		try {
			return redPackService.drawRedPackByLife(userId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
	
//	/**
//	 * 后台发红包
//	 */
//	@RequestMapping(value = "/drawByAdmin", method = RequestMethod.POST)
//	public JsonResult drawByAdmin(@RequestParam("userId") Integer userId,
//			@RequestParam("redPackValue") Integer redPackValue) {
//		try {
//			return redPackService.drawRedPackByAdmin(userId, redPackValue);
//		} catch (Exception ex) {
//			logger.error(ex.getMessage(), ex);
//			return buildFailureResult();
//		}
//	}

	/**
	 * 推荐码页面列表
	 */
	@RequestMapping(value = "/referralList", method = RequestMethod.GET)
	public JsonResult referralList(@RequestParam("userId") Integer userId) {
		try {
			return redPackService.referralList(userId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

}
