package com.zx.stlife.controller.wx.order;

import java.util.Date;

import com.base.jpa.query.Page;
import com.base.modules.util.DateUtilsEx;
import com.zx.stlife.base.UserWxUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.constant.Const.GoodsTimesState;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.controller.common.BizCommon;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.member.MemberService;
import com.zx.stlife.service.order.SnatchNumService;
import com.zx.stlife.service.order.SnatchRecordService;
import com.zx.stlife.service.sys.AccountService;
import com.zx.stlife.tools.DateUtils;
import com.zx.stlife.vo.snatch.SnatchRecordVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/wx/order/snatch/record")
public class SnatchRecordWxController extends BaseController {

	@Autowired
	private SnatchRecordService snatchRecordService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private SnatchNumService snatchNumService;

	
	/**
	 * * 5.9. 查看用户夺宝号码列表
	 */
	@RequestMapping(value="/num/list",method=RequestMethod.GET)
	public String getUserSnatchNum(@RequestParam("goodsTimesId")Integer goodsTimesId,
								   @RequestParam("userId") Integer userId, Model model){
		JsonResult jsonResult=BizCommon.getUserSnatchNum(
				goodsTimesId,userId,snatchRecordService,snatchNumService);
		model.addAttribute("jsonResult", jsonResult);
		return "wx/order/snatch-record-number";
	}
	
	/**
	 * 查询个人夺宝记录
	 */
	@RequestMapping(value = "/personal/list")
	public String getPersonalSnatch(Model model,
			@RequestParam("userId") Integer userId,
			@RequestParam(value = "state", required = false) Byte state,
			Page<SnatchRecordVo> page) {
		Date date = DateUtils.getNow();
		String timestamp = DateUtils.dateToYYYYMMDDHHMMSSSSSString(date);
		page.setPageSize(10);
		snatchRecordService.findPageByUser(page, userId, state, date);
		model.addAttribute("userId", userId);
		model.addAttribute("state", state);
		model.addAttribute("page", page);
		model.addAttribute("timestamp", timestamp);
		if (state == null) {
			if (page.getTotalCount() == 0) {
				return "wx/snatch/personal/snatch-nullrecord";
			}
			return "wx/snatch/personal/snatch-record";
		} else if (state == Const.GoodsTimesState.GOING) {
			if (page.getTotalCount() == 0) {
				return "wx/snatch/personal/snatch-nullrecord-going";
			}
			return "wx/snatch/personal/snatch-record-going";
		} else {
			if (page.getTotalCount() == 0) {
				return "wx/snatch/personal/snatch-nullrecord-over";
			}
			return "wx/snatch/personal/snatch-record-over";
		}
	}

	/**
	 * 查询个人夺宝记录（分页）
	 */
	@RequestMapping("/personal/allRecordByJson")
	@ResponseBody
	public JsonResult allPersonalRecordByJson(Page<SnatchRecordVo> page,
			@RequestParam("userId") Integer userId,
			@RequestParam(value = "state", required = false) Byte state,
			@RequestParam("timestamp") String timestamp) {
		page.setAutoCount(false);
		JsonResult result = BizCommon.getSnatchRecordByAttr(page, userId,
				state, timestamp, snatchRecordService);
		return result;
	}
	
	/**
	 * 查询他人夺宝记录
	 */
	@RequestMapping(value = "/others/list")
	public String getSnatch(Model model,
			@RequestParam("userId") Integer userId, Page<SnatchRecordVo> page) {
		Date date = DateUtils.getNow();
		String timestamp = DateUtils.dateToYYYYMMDDHHMMSSSSSString(date);
		page.setPageSize(10);
		snatchRecordService.findPageByUser(page, userId, null, date);
		User user = accountService.getUser(userId);
		String headImg = memberService.getHeadImgByUser(userId);
		model.addAttribute("headImg", headImg);
		model.addAttribute("user", user);
		model.addAttribute("page", page);
		model.addAttribute("timestamp", timestamp);
		return "wx/snatch/others/snatch-record";
	}

	/**
	 * 查询他人夺宝记录（分页）
	 */
	@RequestMapping("/others/allRecordByJson")
	@ResponseBody
	public JsonResult allRecordByJson(Page<SnatchRecordVo> page,
			@RequestParam("userId") Integer userId,
			@RequestParam("timestamp") String timestamp) {
		page.setAutoCount(false);
		JsonResult result = BizCommon.getSnatchRecordByAttr(page, userId, null,
				timestamp, snatchRecordService);
		return result;
	}
	
	/**
	 * 查看个人中奖记录
	 */
	@RequestMapping("/list/{userId}")
	public String getWinList(@PathVariable("userId") Integer userId,
			Model model, Page<GoodsTimes> page) {
		page.setPageSize(10);
		Date date = DateUtils.getNow();
		String timestamp = DateUtils.dateToYYYYMMDDHHMMSSSSSString(date);
		snatchRecordService.findWinListPageByUser(page, userId, date, null);
		model.addAttribute("page", page)
				.addAttribute("winngStateMap", Const.WinngState.MAP)
				.addAttribute("timestamp", timestamp);
		return "wx/order/win-list";
	}

	/**
	 * 查看个人中奖记录(分页)
	 */
	@RequestMapping("/getWinListByJson")
	@ResponseBody
	public JsonResult getWinListByJson(
			@RequestParam("timestamp") String timestamp,
			@RequestParam(value = "state", required = false) Byte state,
			Page<GoodsTimes> page) {
		Integer userId = UserWxUtils.getCurrUserId();
		return BizCommon.getWinListAttr(page, userId, timestamp, state,
				snatchRecordService);
	}
	
	/**
	 * 查询他人中奖记录
	 */
	@RequestMapping(value = "/others/winngList")
	public String getWinList(Model model,
			@RequestParam("userId") Integer userId, Page<GoodsTimes> page) {
		page.setPageSize(10);
		Date date = DateUtils.getNow();
		String timestamp = DateUtils.dateToYYYYMMDDHHMMSSSSSString(date);
		snatchRecordService.findWinListPageByUser(page, userId, date,
				GoodsTimesState.OVER);
		User user = accountService.getUser(userId);
		String headImg = memberService.getHeadImgByUser(userId);
		model.addAttribute("headImg", headImg);
		model.addAttribute("user", user);
		model.addAttribute("page", page);
		model.addAttribute("timestamp", timestamp);
		return "wx/snatch/others/winner-record";
	}

	/**
	 * 查询他人中奖记录（分页）
	 */
	@RequestMapping("/others/allWinnerRecordByJson")
	@ResponseBody
	public JsonResult allWinnerRecordByJson(Page<GoodsTimes> page,
			@RequestParam("userId") Integer userId,
			@RequestParam("timestamp") String timestamp) {
		Date date = DateUtilsEx.stringToDate(timestamp,
				DateUtilsEx.FORMAT_YYYYMMDDHHMMSS_SSS);
		page.setAutoCount(false);
		JsonResult result = BizCommon.getWinListAttr(page, userId, timestamp,GoodsTimesState.OVER, 
				snatchRecordService);
		return result;
	}
}
