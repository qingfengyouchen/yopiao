package com.zx.stlife.constant;

import com.base.modules.util.PropertiesLoader;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by micheal on 16/1/23.
 */
public class ConstWeixinH5 {
    /** 首页 */
    public final static String REDIRCT_TO_INDEX = Const.ROOT_URI + "/wx/index";
    /** 登陆 */
    public final static String LOGIN_URL = Const.ROOT_URI + "/wx/login";
    /**微信验证登陆后的重定向url*/
    public static final String REDIRECT_URL = Const.ROOT_URI + "/wx/user/saveUser";

    public static PropertiesLoader wxPropertiesLoader = new PropertiesLoader("weixin.properties");

    public static final String ACCOUNT_EMAIL = wxPropertiesLoader.getProperty("account.email");

    // 获取access_token的接口地址（GET） 已认证的公众号限100000，个人测试的2000（次/天）
    public final static String ACCESS_TOKEN_URL = wxPropertiesLoader.getProperty("access_token_url");

    /**微信用户授权链接*/
    public static final String OAUTH_URL = wxPropertiesLoader.getProperty("oauth_url");
    /**获取用户授权token链接*/
    public static final String GET_OAUTH_ACCESS_TOKEN_URL = wxPropertiesLoader.getProperty("get_oauth_access_token_url");
    /**获取用户授权token链接*/
    public static final String GET_USERINFO_URL = wxPropertiesLoader.getProperty("get_userinfo_url");

    /** 发送微信消息 */
    public static String CUSTOMER_SERVICE_URL = wxPropertiesLoader.getProperty("send_message_urL");

    /**创建微信公众号菜单url*/
    public final static String MENU_CREATE_URL = wxPropertiesLoader.getProperty("menu_create_url");

    /**查询微信公众号菜单url*/
    public final static String MENU_QUERY_URL = wxPropertiesLoader.getProperty("menu_query_url");

    public static class WXMenuType{
        /**消息触发类*/
        public static final String CLICK = "click";

        /**网页链接类*/
        public static final String VIEW = "view";

        public static Map<String, String> wxMenuTypeMap = new LinkedHashMap<>(2);
        static {
            wxMenuTypeMap.put(CLICK, "消息触发类");
            wxMenuTypeMap.put(VIEW, "网页链接类");
        }
    }

    /**
     * 消息类型
     */
    public static class WXMsgType{
        /**扩展*/
        public static final String EXPEND = "expand";

        /**文本*/
        public static final String TEXT = "text";

        /**图文*/
        public static final String NEWS = "news";

        public static Map<String, String> wxMsgTypeMap = new LinkedHashMap<>(3);
        static {
            wxMsgTypeMap.put(EXPEND, "扩展");
            wxMsgTypeMap.put(TEXT, "文本");
            wxMsgTypeMap.put(NEWS, "图文");
        }
    }
}
