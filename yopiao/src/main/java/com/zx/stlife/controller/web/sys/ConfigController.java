package com.zx.stlife.controller.web.sys;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.jpa.query.Page;
import com.zx.stlife.base.ParamsEntity;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.record.RechargeRecord;
import com.zx.stlife.entity.sys.Config;
import com.zx.stlife.service.record.RechargeRecordService;
import com.zx.stlife.service.record.RedPackService;
import com.zx.stlife.service.sys.AccountService;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.tools.DateUtils;
import com.zx.stlife.tools.PageUtils;

@Controller
@RequestMapping(value = "/sys/config")
public class ConfigController extends BaseController {

	@Autowired
	private ConfigService configService;

	@Autowired
	private RedPackService redPackService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private RechargeRecordService rechargeRecordService;
	
	private static final String MODULE = "sys:config";
	private static final String URI_PREFIX = "sys/config/config-";
	
	@RequiresPermissions(MODULE + PERMISSION_VIEW)
	@RequestMapping(value = "/list")
	public String list(Model model, ParamsEntity paramsEntity) {
		return URI_PREFIX + "list";
	}
	
	@RequiresPermissions(MODULE + PERMISSION_EDIT)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public String save(ParamsEntity paramsEntity) {
		try {
			String withDrawFee = paramsEntity.get(ConfigService.WITHDRAW_FEE);
			String expireTime = paramsEntity.get(ConfigService.REDPACK_EXPIRETIME);
			String key = null;
			String value = null;
			if (StringUtils.isNotEmpty(withDrawFee)) {
				key = ConfigService.WITHDRAW_FEE;
				value = withDrawFee;
			} else if (StringUtils.isNotEmpty(expireTime)) {
				key = ConfigService.REDPACK_EXPIRETIME;
				value = expireTime;
			}
			if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
				return "0";
			}
			Config config = configService.get(key);
			if (config == null) {
				config = new Config(key, value);
				configService.save(config);
			} else {
				configService.update(key, value);
			}
			return "1";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "-1";
		}
	}
	
	 // #################红包活动时间设置#################
	
	@RequiresPermissions(MODULE + PERMISSION_VIEW)
	@RequestMapping(value = "/redpackactivitytime/list")
	public String redpackList(Model model, ParamsEntity paramsEntity) {
		return URI_PREFIX + "redpack-activity-time-list";
	}

	@RequiresPermissions(MODULE + PERMISSION_EDIT)
	@RequestMapping(value = "/redpackactivitytime/save", method = RequestMethod.POST)
	@ResponseBody
	public String redpackSave(ParamsEntity paramsEntity) {
		try {
			String openTimeValue = paramsEntity.get(ConfigService.REDPACK_ACTIVITY_OPENTIME);
			Config openConfig = configService.get(ConfigService.REDPACK_ACTIVITY_OPENTIME);
			if (openConfig == null) {
				openConfig = new Config(ConfigService.REDPACK_ACTIVITY_OPENTIME, openTimeValue);
				configService.save(openConfig);
			} else {
				configService.update(ConfigService.REDPACK_ACTIVITY_OPENTIME, openTimeValue);
			}

			String endTimeValue = paramsEntity.get(ConfigService.REDPACK_ACTIVITY_ENDTIME);
			Config endConfig = configService.get(ConfigService.REDPACK_ACTIVITY_ENDTIME);
			if (endConfig == null) {
				endConfig = new Config(ConfigService.REDPACK_ACTIVITY_ENDTIME, endTimeValue);
				configService.save(endConfig);
			} else {
				configService.update(ConfigService.REDPACK_ACTIVITY_ENDTIME, endTimeValue);
			}
			return "1";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "-1";
		}
	}
	
	/**
	 * 后台发红包
	 */
	@RequestMapping(value = "/drawByAdmin", method = RequestMethod.POST)
    @ResponseBody
	public String drawByAdmin(ParamsEntity paramsEntity) {
		try {
			String redUserId = paramsEntity.get("redUserId");
			System.out.println(redUserId);
			redUserId = accountService.findUserByUserName(redUserId).getId().toString();
			System.out.println(redUserId);
			String redPackValue = paramsEntity.get("redPackValue");
			if (StringUtils.isEmpty(redUserId) || StringUtils.isEmpty(redPackValue)) {
				return "-1";
			}
			redPackService.drawRedPackByAdmin(
					Integer.parseInt(redUserId), Integer.parseInt(redPackValue));
			return "1";
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return "-1";
		}
	}
}
