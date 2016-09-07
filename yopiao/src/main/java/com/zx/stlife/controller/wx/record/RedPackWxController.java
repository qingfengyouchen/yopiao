package com.zx.stlife.controller.wx.record;

import java.util.Date;

import com.zx.stlife.base.UserWxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.jpa.query.Page;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.controller.common.BizCommon;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.record.RedPack;
import com.zx.stlife.service.record.RedPackService;
import com.zx.stlife.tools.DateUtils;

@Controller
@RequestMapping(value = "/wx/record/redPack")
public class RedPackWxController extends BaseController{
	@Autowired
	private RedPackService redPackService;
	
	private static final String VIEW_URI_PREFIX = "wx/record/redPack/";
	
	/**
	 * 查看红包列表
	 * type:1可使用，2已使用、已过期 不填默认1
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model,
			@RequestParam(value="type",required=false) Byte type, Page<RedPack> page){
		Date date = DateUtils.getNow();
		if(type == null){
			type = Const.RedPackState.CAN_USE;
		}
		String timestamp = DateUtils.dateToYYYYMMDDHHMMSSSSSString(date);
		page.setPageSize(10);
		Integer userId = UserWxUtils.getCurrUserId();
		redPackService.findList(userId, date, page, type);
		model.addAttribute("userId", userId);
		model.addAttribute("type", type);
		model.addAttribute("page", page);
		model.addAttribute("timestamp", timestamp);
		if(page.getTotalCount()>0){
			if(type == Const.RedPackState.CAN_USE){
				return VIEW_URI_PREFIX + "red-list-canuse";
			}else{
				return VIEW_URI_PREFIX + "red-list-over";
			}
		}else{
			if(type == Const.RedPackState.CAN_USE){
				return VIEW_URI_PREFIX + "red-null-canuse";
			}else{
				return VIEW_URI_PREFIX + "red-null-over";
			}
		}
	}
	
	/**
	 * 查看红包列表 分页查询
	 */
	@RequestMapping("/listByJson")
	@ResponseBody
	public JsonResult allRecordByJson(Page<RedPack> page,
			@RequestParam("type") Byte type,
			@RequestParam("timestamp") String timestamp) {
		page.setAutoCount(false);
		Integer userId = UserWxUtils.getCurrUserId();
		JsonResult result = BizCommon.getRedPackListByAttr(page, userId, type, timestamp,
				redPackService);
		return result;
	}
	
}
