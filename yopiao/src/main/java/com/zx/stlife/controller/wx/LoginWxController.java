/*******************************************************************************
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.zx.stlife.controller.wx;

import com.base.modules.util.Encodes;
import com.zx.stlife.constant.ConstWeixinH5;
import com.zx.stlife.entity.wx.WxAccount;
import com.zx.stlife.service.wx.WxAccountService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author micheal cao
 */
@Controller
@RequestMapping(value = "/wx")
public class LoginWxController {

    public org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WxAccountService wxAccountService;

    public static String oauthUrlWithNotBackUrl = null;

    @RequestMapping(value = "/login" , method = RequestMethod.GET)
    public String login(HttpServletRequest request) {
        String backUrl = request.getParameter("backUrl");
        String url = null;
        if(StringUtils.isBlank(backUrl)){
            if(oauthUrlWithNotBackUrl == null) {
                WxAccount wxAccount = wxAccountService.get();
                oauthUrlWithNotBackUrl = "redirect:" + ConstWeixinH5.OAUTH_URL.replace("APPID", wxAccount.getAppId())
                        .replace("REDIRECT_URL",  Encodes.urlEncode(ConstWeixinH5.REDIRECT_URL));
            }
            url = oauthUrlWithNotBackUrl;
        }else{
            String redirectUrl= Encodes.urlEncode(ConstWeixinH5.REDIRECT_URL + "?backUrl=" + backUrl);
            WxAccount wxAccount = wxAccountService.get();
            // 用户授权链接
            url  = "redirect:" + ConstWeixinH5.OAUTH_URL.replace("APPID", wxAccount.getAppId())
                    .replace("REDIRECT_URL",  redirectUrl);
        }

        return url;
    }

}
