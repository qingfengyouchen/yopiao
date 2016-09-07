package com.zx.stlife.controller.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.jpa.query.Page;
import com.zx.stlife.controller.app.base.BaseRestController;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.controller.common.BizCommon;
import com.zx.stlife.entity.service.Activity;
import com.zx.stlife.service.service.ActivityService;

@RestController
@RequestMapping("/app/service/activity")
public class ActivityRestController extends BaseRestController {

	@Autowired
	private ActivityService activityService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JsonResult listAll(
			@RequestParam(value = "title", required = false) String title,
			Page<Activity> page) {
		
		return BizCommon.findActivityRecords(page, title, activityService);
	}
}
