package com.zx.stlife.controller.wx.record;

import com.zx.stlife.base.UserWxUtils;
import com.zx.stlife.controller.app.base.JsonResultUtils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.service.record.JifenService;

@Controller
@RequestMapping(value = "/wx/record/jifen")
public class JifenWxController {

	@Autowired
	private JifenService jifenService;
	
	private static final String VIEW_URI_PREFIX = "wx/record/jifen/";
	
	/**
	 * 查看个人积分
	 */
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public String list(Model model){
		Integer userId = UserWxUtils.getCurrUserId();
		Integer jifen = jifenService.findUserJifen(userId);
		model.addAttribute("userId", userId);
		model.addAttribute("jifen", jifen);
		return VIEW_URI_PREFIX + "jifen-query";
	}
	
	/**
	 * 积分抽红包
	 */
	@RequestMapping(value = "/redPack")
	@ResponseBody
	public JsonResult drawRedPack() {
		Integer userId = UserWxUtils.getCurrUserId();
		try {
			Integer redPackMoney = jifenService.drawRedPack(userId);
			if (redPackMoney == null) {
				return JsonResultUtils.buildFailureResult(301);
			}
			Map<String, Integer> data = new HashMap<String, Integer>(1);
			data.put("redPackMoney", redPackMoney);
			return JsonResultUtils.buildSuccessResult(data);
		} catch (Exception ex) {
			return JsonResultUtils.buildFailureResult();
		}
	}
}
