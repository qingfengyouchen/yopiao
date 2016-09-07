package com.zx.stlife.controller.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.app.base.BaseRestController;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.entity.sys.ImageSetting;
import com.zx.stlife.service.push.JpushRecordService;
import com.zx.stlife.service.sys.ImageSettingService;

/**
 * Created by micheal on 15/12/5.
 */
@RestController
@RequestMapping("/app/common")
public class CommonRestController extends BaseRestController {

	@Autowired
	private ImageSettingService imageSettingService;

	@Autowired
	private JpushRecordService jpushRecordService;

	@RequestMapping(value = "/getTopSwitchImg", method = RequestMethod.GET)
	private JsonResult getTopSwitchImg(
			@RequestParam(value = "userId", required = false) Integer userId) {
		if (userId != null) {
			jpushRecordService.pushProcess(userId);
		}
		List<ImageSetting> listTopSwitch = 
				imageSettingService.findAllWithCache(Const.ImageCategory.INDEX_TOP_SWITCH);
		return buildSuccessResult(listTopSwitch);
	}
	
	@RequestMapping(value = "/getFindSwitchImg", method = RequestMethod.GET)
	private JsonResult getFindSwitchImg(
			@RequestParam(value = "userId", required = false) Integer userId) {
		if (userId != null) {
			jpushRecordService.pushProcess(userId);
		}
		List<ImageSetting> listFindSwitch = 
				imageSettingService.findAllWithCache(Const.ImageCategory.INDEX_FIND_SWITCH);
		return buildSuccessResult(listFindSwitch);
	}
}
