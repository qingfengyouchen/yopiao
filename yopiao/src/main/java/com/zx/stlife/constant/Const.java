package com.zx.stlife.constant;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.base.modules.mapper.JsonMapper;
import com.base.modules.util.FileUtilsEx;
import com.base.modules.util.PropertiesLoader;
import com.base.modules.util.SimpleUtils;

/**
 * Created by micheal on 15/6/25.
 */
public class Const {

    private Const(){}

    /**成功标识*/
    public static final Integer COMMON_RESULT_SUCCESS = 1;

    /**夺宝号码基数*/
    public static final int SNATCH_BASE_NUM = 10_000_001;

    /**晒单缩略图宽度*/
    public static final int SHARE_THUMB_IMAGE_WIDTH = 172;
    /**晒单缩略图高度*/
    public static final int SHARE_THUMB_IMAGE_HEIGHT = 134;

    public static final JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();

    public static PropertiesLoader appPropertiesLoader = new PropertiesLoader("application.properties");

    /** 是否一分钱支付，用于测试*/
    public static final boolean isPay1Fen =
            SimpleUtils.stringToboolean(appPropertiesLoader.getProperty("pay.is.1.fen"));

    /**后台根目录uri*/
    public static final String ROOT_URI = appPropertiesLoader.getProperty("root.uri");

    /**百度编辑器根目录uri*/
    public static final String UEDITOR_ROOT_URI = appPropertiesLoader.getProperty("baidu.ueditor.root.uri");

    /**系统名称*/
    public static final String SYSTEM_NAME = appPropertiesLoader.getPropertyWithChinese("system.name");
    
    public static final String SMS_CODE_TEMPLATE = "{smsCode}（"+ SYSTEM_NAME +"注册验证码，99秒内有效）";

    public static final boolean canPayByWeixin = SimpleUtils.stringToboolean(
            appPropertiesLoader.getProperty("pay.can.weixin"));

    public static final boolean canPayByAlipay = SimpleUtils.stringToboolean(
            appPropertiesLoader.getProperty("pay.can.alipay"));

    /**系统管理员id*/
    public static final String ADMIN_ID = "1";
    /**系统管理员角色id*/
    public static final String ADMIN_ROLE_ID = "1";

    public static final String ROLE_NAME_SYS_ADMIN = "系统管理员";
    
    // ###################### 消息推送  #####################
    /**推送消息类型Key*/
    public static final String PUSH_MESSAGE_TYPE = "message_type";
    
    /**推送中奖消息*/
    public static final String WINNG_TYPE = "winng";
    /**中奖期号*/
    public static final String WINNG_TIMES = "winng_times";
    /**中奖商品名称*/
    public static final String WINNG_GOODS_NAME = "winng_goods_name";
    /**中奖商品期号Id*/
    public static final String WINNG_GOODS_TIMES_ID = "winng_goods_times_id";
    
    /**推送分享消息*/
    public static final String SHARE_TYPE = "share";
    /**分享标题*/
    public static final String SHARE_TITLE = "share_title";
	/**分享描述*/
    public static final String SHARE_DESCRIPTION = "share_description";
	/**分享图片*/
    public static final String SHARE_IMAGE = "share_image";
	/**分享链接*/
    public static final String SHARE_LINK = "share_link";
    
    /**
     * 用户状态
     */
    public static class UserState {
        /**激活*/
        public static final Byte ENABLE = 1;

        /**屏蔽*/
        public static final Byte DISENABLED = 0;

        /**已删除*/
        public static final Byte DELETED = -1;

        public static Map<Byte, String> stateMap = new LinkedHashMap<>(2);
        static {
            stateMap.put(ENABLE, "有效");
            stateMap.put(DISENABLED, "无效");
        }
    }

    /**
     * 用户来源
     */
    public static class UserSource{
        /**注册*/
        public static final Byte REGISTER = 1;
        /**微信登录*/
        public static final Byte WEIXIN_APP = 2;
        /**qq登录*/
        public static final Byte QQ = 3;
        /**微博登录*/
        public static final Byte WEIBO = 4;
        /**微信h5*/
        public static final Byte WEIXIN_H5 = 5;

        public static Map<Byte, String> MAP = new LinkedHashMap<>(5);
        static {
            MAP.put(REGISTER, "注册");
            MAP.put(WEIXIN_APP, "微信登录");
            MAP.put(REGISTER, "qq登录");
            MAP.put(WEIXIN_APP, "微博登录");
            MAP.put(REGISTER, "微信h5");
        }
    }

    /**
     * 性别
     */
    public static class Gender{
        /**男*/
        public static final Byte MALE = 1;

        /**女*/
        public static final Byte FEMALE = 0;

        /**保密*/
        public static final Byte SECRET = 2;

        public static Map<Byte, String> genderMap = new LinkedHashMap<>();
        static {
            genderMap.put(MALE, "男");
            genderMap.put(FEMALE, "女");
            genderMap.put(SECRET, "保密");
        }
    }

    /**
     * 通用的状态
     */
    public static class CommonState{
        /**正常*/
        public static final Byte ENABLE = 1;
        /**删除*/
        public static final Byte DELETED = 0;
    }

    public static Map<Byte, String> commonStateMap = new LinkedHashMap<>();
    static {
        commonStateMap.put(CommonState.ENABLE, "正常");
        commonStateMap.put(CommonState.DELETED, "删除");
    }

    /**
     * 图片类别
     */
    public static class ImageCategory{
        /**介绍图片*/
        public static final Byte INTRODUCE = 1;
        /**首页顶部图片*/
        public static final Byte INDEX_TOP_SWITCH = 2;
        /**发现的公告及活动图片*/
        public static final Byte INDEX_FIND_SWITCH = 3;

        public static Map<Byte, String> MAP = new LinkedHashMap<>(3);
        static {
            MAP.put(INTRODUCE, "介绍图片");
            MAP.put(INDEX_TOP_SWITCH, "首页顶部图片");
            MAP.put(INDEX_FIND_SWITCH, "发现的图片");
        }
    }

    /**
     * 商品图片类型
     */
    public static class GoodsImageCategory{
        /**顶部切换的图片*/
        public static final Byte TOP_SWITCH = 1;
        /**图文详情*/
        public static final Byte DETAILS = 2;
    }

    /**
     * 点击图片动作类型
     */
    public static class ImageActionType{
        /**链接到网页*/
        public static final Byte TO_HTML = 1;
        /**链接到商品详情*/
        public static final Byte TO_GOODS_DETAIL = 2;

        public static Map<Byte, String> MAP = new LinkedHashMap<>(2);
        static {
            MAP.put(TO_HTML, "跳转到网页");
            MAP.put(TO_GOODS_DETAIL, "跳转到商品详情");
        }
    }

    /**
     * 商品状态
     */
    public static class GoodsState extends CommonState{
        /**下架*/
        public static final Byte OUT_SALES = 2;

        public static Map<Byte, String> MAP = new LinkedHashMap<>(2);
        static {
            MAP.put(ENABLE, "在售");
            MAP.put(OUT_SALES, "下架");
        }
    }

    /**
     * 夺宝期号状态
     */
    public static class GoodsTimesState{
        /**进行中*/
        public static final Byte GOING = 1;
        /**等待揭晓，倒计时*/
        public static final Byte WAITING = 2;
        /**已揭晓*/
        public static final Byte OVER = 3;
        /**该商品已被删除*/
        public static final Byte DELETED = 4;

        public static List<Byte> goingStates = Arrays.asList(GOING, WAITING);
        public static Map<Byte, String> SETTING_MAP = new LinkedHashMap<>(2);
        public static Map<Byte, String> MAP = new LinkedHashMap<>(3);
        static {
            SETTING_MAP.put(GOING, "进行中");
            SETTING_MAP.put(WAITING, "倒计时");

            MAP.put(GOING, "进行中");
            MAP.put(WAITING, "倒计时");
            MAP.put(OVER, "已揭晓");
        }
    }
    
    /**
     * 状态 liyf
     */
    public static class exchangeState{
        /**未兑换*/
        public static final Byte NO_EXCHANGE = 0;
        /**已兑换积分*/
        public static final Byte EXCHANGE_INTEGRAL = 1;
        /**未确认地址（已选择兑换商品）*/
        public static final Byte ADDRESS_NO_CONFIRMED = 2;
        /**未发货（已确认地址）*/
        public static final Byte NO_SHIPPED = 3;
        /**已发货*/
        public static final Byte DELIVER_GOODS = 4;
        /**已收货（App端确认）*/
        public static final Byte RECEIVED_GOODS = 5;
        /**已晒单*/
        public static final Byte SUN_SHEET = 6;

        public static Map<Byte, String> MAP = new LinkedHashMap<>(4);
        static {
            MAP.put(NO_EXCHANGE, "未兑换");
            MAP.put(EXCHANGE_INTEGRAL, "已兑换积分");
            MAP.put(ADDRESS_NO_CONFIRMED, "未确认地址");
            MAP.put(NO_SHIPPED, "未发货");
            MAP.put(DELIVER_GOODS, "已发货");
            MAP.put(RECEIVED_GOODS, "已收货");
        }
    }

    /**
     * 中奖状态
     */
    public static class WinngState{
        /**确认收货地址*/
        public static final Byte CONFIRM_ADDRESS = 2;
        /**等待商品派发*/
        public static final Byte WAITING_DISPATCH = 3;
        /**确认收货*/
        public static final Byte CONFIRM_RECEIVE = 4;
        /**已签收*/
        public static final Byte OVER = 5;

        public static Map<Byte, String> MAP = new LinkedHashMap<>(4);
        static {
            MAP.put(CONFIRM_ADDRESS, "确认收货地址");
            MAP.put(WAITING_DISPATCH, "等待商品派发");
            MAP.put(CONFIRM_RECEIVE, "确认收货");
            MAP.put(OVER, "完成");
        }
    }

    /**
     * 中奖商品状态
     */
    public static class WinngGoodsState{
        /**获取商品*/
        public static final Byte GET = 1;
        /**确认收货地址*/
        public static final Byte CONFIRM_ADDRESS = 2;
        /**商品派发*/
        public static final Byte DISPATCH = 3;
        /**确认收货*/
        public static final Byte CONFIRM_RECEIVE = 4;
        /**完成*/
        public static final Byte OVER = 5;

        public static Map<Byte, String> MAP = new LinkedHashMap<>(5);
        static {
            MAP.put(GET, "获取商品");
            MAP.put(CONFIRM_ADDRESS, "确认收货地址");
            MAP.put(DISPATCH, "商品派发");
            MAP.put(CONFIRM_RECEIVE, "确认收货");
            MAP.put(OVER, "完成");
        }
    }

    /**
     * 微信端用中奖商品状态
     */
    public static class WxWinngGoodsState{
        /**获取商品*/
        public static final Byte GET = 1;
        /**选择领取方式*/
        public static final Byte CONFIRM_ADDRESS = 2;
        /**确认收货地址*/
        public static final Byte CONFIRM_RECEIVE = 3;
        /**商品派发*/
        public static final Byte DISPATCH = 4;
        /**完成*/
        public static final Byte OVER = 5;

        public static Map<Byte, String> MAP = new LinkedHashMap<>(5);
        static {
            MAP.put(GET, "恭喜您中奖了");
            MAP.put(CONFIRM_ADDRESS, "选择领取方式");
            MAP.put(CONFIRM_RECEIVE, "确认收货地址");
            MAP.put(DISPATCH, "等待商品派发");
            MAP.put(OVER, "完成");
        }
    }

    /**
     * 支付方式
     */
    public static class PayWayType {
        /**支付宝*/
        public static final Byte ALIPAY = 1;
        /**微信支付*/
        public static final Byte WEIXIN = 2;
        /**红包*/
        public static final Byte REDPACKET = 3;
        /**生活币*/
        public static final Byte CURRENCY = 4;

        public static Map<Byte, String> payWayMap = new LinkedHashMap<>(4);
        static {
        	payWayMap.put(ALIPAY, "支付宝");
        	payWayMap.put(WEIXIN, "微信支付");
        	payWayMap.put(REDPACKET, "红包");
        	payWayMap.put(CURRENCY, "生活币");
        }
    }

    /**
     * 支付结果
     */
    public static class PayResult{
        /**等待支付*/
        public static final Byte UNPAY = 1;
        /**支付成功*/
        public static final Byte PAY_SUCCESS = 2;
        /**支付失败*/
        public static final Byte PAY_FAIL = 3;
        /**取消支付*/
        public static final Byte CANCEL_PAY = 4;
        /**支付成功但未能夺宝*/
        public static final Byte PAY_SUCCESS_BUT_CANNOT_SNATCH = 5;

        public static Map<Byte, String> MAP = new LinkedHashMap<>(5);
        static {
            MAP.put(UNPAY, "等待支付");
            MAP.put(PAY_SUCCESS, "支付成功");
            MAP.put(PAY_FAIL, "支付失败");
            MAP.put(CANCEL_PAY, "取消支付");
            MAP.put(PAY_SUCCESS_BUT_CANNOT_SNATCH, "支付成功但未能夺宝");
        }
    }

    /**
     * 充值状态
     */
    public static class RechargeState{
        /**等待充值*/
        public static final Byte UNRECHARGE = 1;
        /**充值成功*/
        public static final Byte RECHARGE_SUCCESS = 2;
        /**充值失败*/
        public static final Byte RECHARGE_FAIL = 3;
        /**取消充值*/
        public static final Byte CANCEL_RECHARGE = 4;

        public static Map<Byte, String> MAP = new LinkedHashMap<>(4);
        static {
            MAP.put(UNRECHARGE, "等待支付");
            MAP.put(RECHARGE_SUCCESS, "支付成功");
            MAP.put(RECHARGE_FAIL, "支付失败");
            MAP.put(CANCEL_RECHARGE, "取消支付");
        }
    }

    /**
     * 红包状态
     */
    public static class RedPackState{
        /**可使用*/
        public static final Byte CAN_USE = 1;
        /**已使用*/
        public static final Byte USED = 2;
        /**已过期*/
        public static final Byte EXPIRED = 3;

        public static List<Byte> usedAndExpiredStates = Arrays.asList(USED, EXPIRED);
        public static Map<Byte, String> map = new LinkedHashMap<>(3);
        static {
            map.put(CAN_USE, "可使用");
            map.put(USED, "已使用");
            map.put(EXPIRED, "已过期");
        }
    }

    /**
     * 红包来源方式
     */
    public static class RedPackSourceType{
        /**系统赠送*/
        public static final Byte FROM_SYSTEM = 1;
        /**积分抽红包*/
        public static final Byte FROM_JIFEN = 2;
        /**好友赠送*/
        public static final Byte FORM_FRIEND = 3;
        /**活动分享抽红包*/
        public static final Byte FROM_SHARE = 4;
        /**推荐码抽红包*/
        public static final Byte FORM_RECOMMEND = 5;
        /**生活类送红包*/
        public static final Byte FORM_LIEF_SERVICE = 6;
        /**后台发红包*/
        public static final Byte FROM_ADMIN = 7;

        public static Map<Byte, String> map = new LinkedHashMap<>(7);
        static {
            map.put(FROM_SYSTEM, "系统赠送");
            map.put(FROM_JIFEN, "积分抽红包");
            map.put(FORM_FRIEND, "好友赠送");
            map.put(FROM_SHARE, "活动分享抽红包");
            map.put(FORM_RECOMMEND, "推荐码抽红包");
            map.put(FORM_LIEF_SERVICE, "生活类送红包");
            map.put(FROM_ADMIN, "后台发红包");
        }
    }

    /**
     * 红包类别
     */
    public static class RedPackCategory{
        /**夺宝红包*/
        public static final Byte SNATCH = 1;
        /**生活红包*/
        public static final Byte LIFE = 2;
        /**后台红包*/
        public static final Byte ADMIN = 3;

        public static Map<Byte, String> map = new LinkedHashMap<>(3);
        static {
            map.put(SNATCH, "夺宝红包");
            map.put(LIFE, "生活红包");
            map.put(ADMIN, "后台红包");
        }
    }

    /***
     * 晒单状态
     */
    public static class ShareGoodsState{
        /**待审核*/
        public static final Byte UNAUDIT = 1;
        /**审核通过*/
        public static final Byte AUDIT_PASS = 2;
        /**审核不通过*/
        public static final Byte AUDIT_NOT_PASS = 3;
        /**已删除*/
        public static final Byte DELETEED = 4;

        public static Map<Byte, String> MAP = new LinkedHashMap<>(4);
        static {
            MAP.put(UNAUDIT, "待审核");
            MAP.put(AUDIT_PASS, "审核通过");
            MAP.put(AUDIT_NOT_PASS, "审核不通过");
            MAP.put(DELETEED, "已删除");
        }
    }

    /***
     * E购商业类别
     */
    public static class BusinessInfoState{
        /**出售*/
        public static final Byte SELL = 1;
        /**出租*/
        public static final Byte LEASE = 2;

        public static Map<Byte, String> MAP = new LinkedHashMap<>(2);
        static {
            MAP.put(SELL, "出售");
            MAP.put(LEASE, "出租");
        }
    }
    
    /***
     * 私教、补习类别
     */
    public static class TutoringState{
        /**私教*/
        public static final Byte SIJIAO = 1;
        /**补习*/
        public static final Byte BUXI = 2;

        public static Map<Byte, String> MAP = new LinkedHashMap<>(2);
        static {
            MAP.put(SIJIAO, "家教");
            MAP.put(BUXI, "补习");
        }
    }
    
    /**
     *E购活动
     */
    public static class ActivityState{
        /**正常*/
        public static final Byte ENABLE = 1;
        /**删除*/
        public static final Byte DELETED = 0;
        /**结束**/
        public static final Byte END=2;
        
        public static Map<Byte,String> MAP =new LinkedHashMap<Byte,String>(3);
        static{
        	MAP.put(ENABLE, "进行中");
        	MAP.put(END, "以结束");
        	MAP.put(DELETED, "以删除");
        }
    }

    /**模板的根目录uri*/
    public static final String TEMPLATE_ROOT_PATH = appPropertiesLoader.getProperty("template.root.path");
    /**静态页面的根目录路径*/
    public static final String STATIC_HTML_ROOT_PATH = appPropertiesLoader.getProperty("static.html.root.path");
    /**静态页面的根目录uri*/
    public static final String STATIC_HTML_ROOT_URI = appPropertiesLoader.getProperty("static.html.root.uri");

    /**图片的根目录*/
    public static final String IMG_ROOT_PATH = appPropertiesLoader.getProperty("img.root.path");
    /**访问图片的根目录uri*/
    public static final String IMG_ROOT_URI = appPropertiesLoader.getProperty("img.root.uri");

    /**临时图片的根目录*/
    public static final String TMP_IMG_ROOT_PATH = IMG_ROOT_PATH + "/tmp";

    /**顶部图片切换*/
    public static final String NODE_TOP_SWITCH = "topSwitch";
    /**顶部图片切换的图片根目录*/
    public static final String TOP_SWITCH_IMG_ROOT_PATH = IMG_ROOT_PATH + "/" + NODE_TOP_SWITCH;
    /**顶部图片切换的图片的uri*/
    public static final String TOP_SWITCH_IMG_ROOT_URI = IMG_ROOT_URI + "/" + NODE_TOP_SWITCH;

    /**商品*/
    public static final String NODE_GOODS = "goods";
    /**商品的图片根目录*/
    public static final String GOODS_IMG_ROOT_PATH = IMG_ROOT_PATH + "/" + NODE_GOODS;
    /**商品的图片的uri*/
    public static final String GOODS_IMG_ROOT_URI = IMG_ROOT_URI + "/" + NODE_GOODS;
    /**商品的静态页面根目录*/
    public static final String GOODS_HTML_ROOT_PATH = STATIC_HTML_ROOT_PATH + "/" + NODE_GOODS;

    /**商品期号*/
    public static final String NODE_GOODS_TIMES = "goodsTimes";
    /**计算详情静态页面根目录*/
    public static final String GOODS_TIMES_HTML_ROOT_PATH = STATIC_HTML_ROOT_PATH + "/" + NODE_GOODS_TIMES;

    /**商品类别*/
    public static final String NODE_GOODS_CATEGORY = "goodsCategory";
    /**商品的图片根目录*/
    public static final String GOODS_CATEGORY_IMG_ROOT_PATH = IMG_ROOT_PATH + "/" + NODE_GOODS_CATEGORY;
    /**商品的图片的uri*/
    public static final String GOODS_CATEGORY_IMG_ROOT_URI = IMG_ROOT_URI + "/" + NODE_GOODS_CATEGORY;
    
    /**用户头像*/
    public static final String NODE_USER = "user";
    /**用户头像图片根目录*/
    public static final String USER_HEADIMG_IMG_ROOT_PATH = IMG_ROOT_PATH + "/" + NODE_USER;
    /**用户头像图片的uri*/
    public static final String USER_HEADIMG_IMG_ROOT_URI = IMG_ROOT_URI + "/" + NODE_USER;

    /**晒单图片*/
    public static final String SHARE_GOODS = "shareGoods";
    /**晒单图片根目录*/
    public static final String SHARE_GOODS_IMG_ROOT_PATH = IMG_ROOT_PATH + "/" + SHARE_GOODS;
    /**晒单图片的uri*/
    public static final String SHARE_GOODS_IMG_ROOT_URI = IMG_ROOT_URI + "/" + SHARE_GOODS;
    
    /**鲜花速递**/
    public static final String NODE_FLOWERARTIST="flowerartist";
    /**鲜花速递的静态页面根目录*/
    public static final String FLOWERARTIST_HTML_ROOT_PATH = STATIC_HTML_ROOT_PATH + "/" + NODE_FLOWERARTIST;
    
    /**E购活动图片**/
    public static final String NODE_ACTIVITY="activity";
    /**E购活动图片根目录**/
    public static final String ACTIVITY_IMG_ROOT_PATH=IMG_ROOT_PATH+"/"+NODE_ACTIVITY;
    /**E购活动图片URL**/
    public static final String ACTIVITY_IMG_ROOT_URL=IMG_ROOT_URI+"/"+NODE_ACTIVITY;
    /**E购活动图片的静态页面根目录*/
    public static final String ACTIVITY_HTML_ROOT_PATH = STATIC_HTML_ROOT_PATH + "/" + NODE_ACTIVITY;
    
    /**顺风代驾**/
    public static final String NODE_DelegateDrive="delegateDrive";
    /**顺风代驾的静态页面根目录**/
    public static final String DelegateDrive_HTML_ROOT_PATH = STATIC_HTML_ROOT_PATH + "/" + NODE_DelegateDrive;

    /**E购商业*/
    public static final String BUSINESSINFO ="businessinfo";
    /**E购商业的图片根目录*/
    public static final String BUSINESS_IMG_ROOT_PATH = IMG_ROOT_PATH + "/"  + BUSINESSINFO;
    /**E购商业的图片的uri*/
    public static final String BUSINESS_IMG_ROOT_URI = IMG_ROOT_URI + "/" + BUSINESSINFO;
    /**E购商业的静态页面根目录*/
    public static final String BUSINESS_HTML_ROOT_PATH = STATIC_HTML_ROOT_PATH + "/" + BUSINESSINFO;
    
    /**便民服务*/
    public static final String CONVENIENCE ="convenience";
    /**便民服务的图片根目录*/
    public static final String CONVENIENCE_IMG_ROOT_PATH = IMG_ROOT_PATH + "/" + CONVENIENCE;
    /**便民服务的图片的uri*/
    public static final String CONVENIENCE_IMG_ROOT_URI = IMG_ROOT_URI + "/" + CONVENIENCE;
    /**便民服务的静态页面根目录*/
    public static final String CONVENIENCE_HTML_ROOT_PATH = STATIC_HTML_ROOT_PATH + "/" + CONVENIENCE;
    
    /**休闲美食*/
    public static final String  FUNFOOD= "funfood";
    /**休闲美食的图片根目录*/
    public static final String FUNFOOD_IMG_ROOT_PATH = IMG_ROOT_PATH + "/" + FUNFOOD;
    /**休闲美食的图片的uri*/
    public static final String FUNFOOD_IMG_ROOT_URI = IMG_ROOT_URI + "/" + FUNFOOD;
    /**休闲美食的静态页面根目录*/
    public static final String FUNFOOD_HTML_ROOT_PATH = STATIC_HTML_ROOT_PATH + "/" + FUNFOOD;
    
    
    /**E购微商*/
    public static final String  MICROBUSINESS= "microbusiness";
    /**E购微商的图片根目录*/
    public static final String MICROBUSINESS_IMG_ROOT_PATH = IMG_ROOT_PATH + "/" + MICROBUSINESS;
    /**E购微商的图片的uri*/
    public static final String MICROBUSINESS_IMG_ROOT_URI = IMG_ROOT_URI + "/" + MICROBUSINESS;
    /**E购微商的静态页面根目录*/
    public static final String MICROBUSINESS_HTML_ROOT_PATH = STATIC_HTML_ROOT_PATH + "/" + MICROBUSINESS;
    
    
    /**E购八卦*/
    public static final String  ROOFTOPGOSSIP="rooftopgossip";
    /**E购八卦的图片根目录*/
    public static final String ROOFTOPGOSSIP_IMG_ROOT_PATH = IMG_ROOT_PATH + "/" + ROOFTOPGOSSIP;
    /**E购八卦的图片的uri*/
    public static final String ROOFTOPGOSSIP_IMG_ROOT_URI = IMG_ROOT_URI + "/" + ROOFTOPGOSSIP;
    /**E购八卦的静态页面根目录*/
    public static final String ROOFTOPGOSSIP_HTML_ROOT_PATH = STATIC_HTML_ROOT_PATH + "/" + ROOFTOPGOSSIP;
    
    /**私教补习*/
    public static final String  TUTORING="tutoring";
    /**私教补习的图片根目录*/
    public static final String TUTORING_IMG_ROOT_PATH = IMG_ROOT_PATH + "/" + TUTORING;
    /**私教补习的图片的uri*/
    public static final String TUTORING_IMG_ROOT_URI = IMG_ROOT_URI + "/" + TUTORING;
    /**私教补习的静态页面根目录*/
    public static final String TUTORING_HTML_ROOT_PATH = STATIC_HTML_ROOT_PATH + "/" + TUTORING;
    
    
    
    /**求职工作**/
    public static final String NODE_Job="job";
    /**求职工作的静态页面根目录**/
    public static final String JOB_HTML_ROOT_PATH = STATIC_HTML_ROOT_PATH + "/" + NODE_Job;
    
    /**招聘工作**/
    public static final String NODE_RECRUITMENT="Recruitment";
    /**招聘工作的静态页面根目录**/
    public static final String RECRUITMENT_HTML_ROOT_PATH = STATIC_HTML_ROOT_PATH + "/" + NODE_RECRUITMENT;
    
    static {
        FileUtilsEx.createFold(TMP_IMG_ROOT_PATH);
    }

    public static final Map<Boolean, String> booleanMap = new LinkedHashMap<>();
    static{
        booleanMap.put(true, "是");
        booleanMap.put(false, "否");
    }

    // ######################微信支付#####################
    /**微信商户号*/
    public static String WX_MCH_ID = appPropertiesLoader.getProperty("wx.mch.id");
    /**公众账号ID*/
    public static String WX_APP_ID = appPropertiesLoader.getProperty("wx.app.id");
    public static String WX_APP_SECRET = appPropertiesLoader.getProperty("wx.app.secret");
    /**微信商户密钥*/
    public static String WX_API_KEY = appPropertiesLoader.getProperty("wx.api.key");
    /**微信下单url*/
    public static String WX_UNIFIED_ORDER_URL = appPropertiesLoader.getProperty("wx.unified.order.url");
    /**微信支付回调url*/
    public static String WX_PAY_NOTIFY_URL = appPropertiesLoader.getProperty("wx.pay.notify.url");
    /**微信充值回调url*/
    public static String WX_RECHARGE_NOTIFY_URL = appPropertiesLoader.getProperty("wx.recharge.notify.url");
    /**微信查询订单url*/
    public static String WX_QUERY_ORDER_URL = appPropertiesLoader.getProperty("wx.query.order.url");
    /** 签名加密方式 */
    public final static String WX_SIGN_TYPE = appPropertiesLoader.getProperty("wx.sign.type");
    /** 商户证书私钥 */
    public final static String WX_CERT_KEY = appPropertiesLoader.getProperty("wx.cert.key");
    /** 商户证书存放的位置 */
    public final static String WX_CERT_PATH = appPropertiesLoader.getProperty("wx.cert.path");

    /**微信商户号*/
    public static String WX_H5_MCH_ID = appPropertiesLoader.getProperty("wx.h5.mch.id");
    /**公众账号ID*/
    public static String WX_H5_APP_ID = appPropertiesLoader.getProperty("wx.h5.app.id");

    public static String WX_H5_APP_SECRET = appPropertiesLoader.getProperty("wx.h5.secret.id");
    /**微信商户密钥*/
    public static String WX_H5_API_KEY = appPropertiesLoader.getProperty("wx.h5.api.key");
    /** 商户证书私钥 */
    public final static String WX_H5_CERT_KEY = appPropertiesLoader.getProperty("wx.h5.cert.key");

    // ######################支付宝支付#####################
    /** 接口名称 */
    public final static String ALIPAY_SERVICE = appPropertiesLoader.getProperty("alipay.service");
    /** H5接口名称 */
    public final static String ALIPAY_SERVICE_H5 = appPropertiesLoader.getProperty("alipay.service.h5");
    /** 支付宝H5网关 */
    public final static String ALIPAY_GATEWAY_H5 = appPropertiesLoader.getProperty("alipay.gateway.h5");
    /** 合作者身份ID */
    public final static String ALIPAY_PARTNER = appPropertiesLoader.getProperty("alipay.partner");
    /** 参数编码字符集 */
    public final static String ALIPAY_INPUT_CHARSET = appPropertiesLoader.getProperty("alipay.input.charset");
    /** 签名方式 */
    public final static String ALIPAY_SIGN_TYPE = appPropertiesLoader.getProperty("alipay.sign_type");
    /** 服务器异步通知页面路径 */
    public final static String ALIPAY_PAY_NOTIFY_URL = appPropertiesLoader.getProperty("alipay.pay.notify.url");
    /** 服务器异步通知页面路径 */
    public final static String ALIPAY_RECHARGE_NOTIFY_URL = appPropertiesLoader.getProperty("alipay.recharge.notify.url");
    /** 支付类型 */
    public final static String ALIPAY_SELLER_ID = appPropertiesLoader.getProperty("alipay.seller_id");
    /** 卖家支付宝账号 */
    public final static String ALIPAY_PAYMENT_TYPE = appPropertiesLoader.getProperty("alipay.payment.type");
    /** 有效时间 10分钟 */
    public final static String ALIPAY_IT_B_PAY = appPropertiesLoader.getProperty("alipay.it.b.pay");
    /** 支付宝RSA私钥 */
    public final static String ALIPAY_RSA_PRIVATE_KEY = appPropertiesLoader.getProperty("alipay.rsa.private.key");

    /** 支付宝公匙 */
    public final static String ALIPAY_PUBLIC_KEY = appPropertiesLoader.getProperty("alipay.public.key");

    /** 支付内容备注 */
    public final static String PAY_SUBJECT = appPropertiesLoader.getPropertyWithChinese("pay.subject");
}
