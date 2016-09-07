package com.zx.stlife.controller.wx.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.base.jpa.query.Page;
import com.zx.stlife.base.UserWxUtils;
import com.zx.stlife.controller.app.base.BaseRestController;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.controller.common.BizCommon;
import com.zx.stlife.entity.member.Member;
import com.zx.stlife.entity.service.Activity;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.service.ActivityService;

@Controller
@RequestMapping("/wx/service/activity")
public class ActivityFindController extends BaseRestController {

	@Autowired
	private ActivityService activityService;

	@RequestMapping(value = "/find")
	public String find(Model model) {
		
		return "wx/service/find";
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult listAll(
			@RequestParam(value = "title", required = false) String title,
			Page<Activity> page) {
		
		return BizCommon.findActivityRecords(page, title, activityService);
	}
}
