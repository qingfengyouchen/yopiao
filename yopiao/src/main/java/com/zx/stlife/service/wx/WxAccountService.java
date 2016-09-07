/*******************************************************************************
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.zx.stlife.service.wx;

import com.alibaba.fastjson.JSONObject;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.constant.ConstWeixinH5;
import com.zx.stlife.entity.wx.WxAccount;
import com.zx.stlife.repository.jpa.wx.WxAccountDao;
import com.zx.stlife.tools.weixin.WeixinUitls;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.javasimon.aop.Monitored;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 用户管理业务类.
 * 
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional
@Monitored
public class WxAccountService {

	private static Logger logger = LoggerFactory.getLogger(WxAccountService.class);

	@Autowired
	private WxAccountDao dao;

	public WxAccount get(){
		WxAccount wxAccount = dao.findByEmail(ConstWeixinH5.ACCOUNT_EMAIL);
		return wxAccount;
	}

	public String getAppId(){
		return get().getAppId();
	}

	public void save(WxAccount entity){
		dao.save(entity);
	}

	public String getValidAccessToken(){
		WxAccount wxAccount = get();
		Date expiresDate = wxAccount.getExpiresDate();
		if(expiresDate == null || new Date().compareTo(expiresDate) >=0 ){
			String url = ConstWeixinH5.ACCESS_TOKEN_URL.replace("APPID", wxAccount.getAppId())
					.replace("APPSECRET", wxAccount.getAppSecret());
			JSONObject jsonObject = WeixinUitls.httpRequest(url, "GET", null);
			String accessToken = null;
			if (null != jsonObject &&
					StringUtils.isNotBlank(accessToken = jsonObject.getString("access_token"))) {
				wxAccount.setAccessToken(accessToken);
				int expiresIn = jsonObject.getIntValue("expires_in");
				Date now = new Date();
				expiresDate = DateUtils.addSeconds(now, expiresIn - 600);
				wxAccount.setExpiresDate(expiresDate);
				wxAccount.setAddTokenTime(now);
				save(wxAccount);
				return accessToken;
			}else{
				logger.error("获取微信access token失败，详情：{}",
						jsonObject == null ? "无返回结果" : jsonObject.toJSONString());
				return null;
			}
		}else{
			return wxAccount.getAccessToken();
		}
	}
}
