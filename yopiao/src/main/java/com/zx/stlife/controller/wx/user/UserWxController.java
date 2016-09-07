package com.zx.stlife.controller.wx.user;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.base.modules.util.Encodes;
import com.zx.stlife.base.UserWxUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.constant.ConstWeixinH5;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.member.Member;
import com.zx.stlife.entity.member.MemberLevel;
import com.zx.stlife.entity.sys.Config;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.entity.wx.WxAccount;
import com.zx.stlife.service.member.MemberLevelService;
import com.zx.stlife.service.member.MemberService;
import com.zx.stlife.service.sys.AccountService;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.service.wx.WxAccountService;

@Controller
@RequestMapping(value = "/wx/user")
public class UserWxController extends BaseController {

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private MemberService memberService;

	@Autowired
	private WxAccountService wxAccountService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private MemberLevelService memberLevelService;

	private static final String VIEW_URI_PREFIX = "wx/user/";

	public static String REDIRECT_USER_HOME_URI = "redirect:/wx/user/home";

	@RequestMapping(value = "home")
	public String getHomePage(Model model) {
		User user = accountService.getUser(UserWxUtils.getCurrUserId());
		UserWxUtils.refreshUser(user);
		Member member = memberService.findMember(user.getId());
		model.addAttribute("member", member)
				.addAttribute("user", user);
		return VIEW_URI_PREFIX + "user-home";
	}
	
	
	
	@RequestMapping(value = "account")
	public String getAccount(Model model) {
		User user = UserWxUtils.getCurrUser();
		Member member=memberService.findMember(user.getId());
		String headImg = memberService.getHeadImgByUser(user.getId());
		model.addAttribute("headImg", headImg);	
		model.addAttribute("member", member);
		model.addAttribute("user", user);
		return VIEW_URI_PREFIX + "account";
	}
	
	@RequestMapping(value = "editNickName")
	public String editNickName(Model model) {
		User user = UserWxUtils.getCurrUser();
		model.addAttribute("user", user);
		return VIEW_URI_PREFIX + "account-nick-name";
	}
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(Model model,
			@Valid @ModelAttribute("preloadEntity") User user) {
		accountService.save(user);
		return getRedirectUrl("account");
	}
	
	@ModelAttribute("preloadEntity")
	public User getEntity(
			@RequestParam(value = "id", required = false) Integer id) {
		if (null != id) {
			return accountService.getUser(id);
		}
		return new User();
	}


	/**
	 * 登陆，若第一次则新增用户，否则不修改用户信息
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/saveUser", method = RequestMethod.GET)
	public String saveUser(HttpServletRequest request, Model model) {
		String code = request.getParameter("code");
		User user = null;
		if (StringUtils.isNotBlank(code)) {
			WxAccount account = wxAccountService.get();
			try {
				JSONObject userJson = getUserInfoFromWeixin(request, account, code);
				user = saveUserInfo(userJson);
			}
			catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("message", "访问出错，请重试！");
				return "/error/500";
			}
		}

		UserWxUtils.refreshUser(user);

		String backUrl = request.getParameter("backUrl");
		if(StringUtils.isBlank(backUrl))
			return REDIRECT_USER_HOME_URI;

		String url = "redirect:" + new String(Encodes.decodeBase64(backUrl));
		return url;
	}

	/**
	 * 获取微信的用户信息
	 * @param request
	 * @param account
	 * @param code
	 * @return
	 */
	private JSONObject getUserInfoFromWeixin(HttpServletRequest request, WxAccount account, String code) {
		String get_access_token_url = ConstWeixinH5.GET_OAUTH_ACCESS_TOKEN_URL
				.replace("APPID", account.getAppId())
				.replace("SECRET", account.getAppSecret())
				.replace("CODE", code);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(get_access_token_url);
		JSONObject userJson = null;
		try {
			HttpResponse httResponse = httpClient.execute(httpGet);
			String json = EntityUtils.toString(httResponse.getEntity(), "utf-8");
			JSONObject jsonObject = JSONObject.parseObject(json);

			//没有错误，说明成功了
			if(StringUtils.isBlank(jsonObject.getString("errcode"))) {
				String access_token = jsonObject.getString("access_token");
				String openid = jsonObject.getString("openid");
				String getUserinfoUrl = ConstWeixinH5.GET_USERINFO_URL
						.replace("ACCESS_TOKEN", access_token)
						.replace("OPENID", openid);

				HttpGet httpGet2 = new HttpGet(getUserinfoUrl);
				HttpResponse httResponse2 = httpClient.execute(httpGet2);
				String userInfoJson = EntityUtils.toString(httResponse2.getEntity(), "utf-8");

				userJson = JSONObject.parseObject(userInfoJson);
			}
		}
		catch (ClientProtocolException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return userJson;
	}

	/**
	 * 保存用户信息
	 * @param userJson
	 */
	private User saveUserInfo(JSONObject userJson) {
		if(userJson == null)
			return null;

		String openid = userJson.getString("openid");
		User user = accountService.findUserByOpenId(openid);
		if (user ==  null) {// 判断微信用户是否已经存在，如果存在，则更新用户资料，不存在则创建用户并保存
			user = new User();
		}else{
			return user;
		}
		// 获取微信用户的基本信息，并保存到数据
		Member member = fillUser(userJson, user);
		if(StringUtils.isBlank(user.getUserName())){
			user.setUserName(configService.getUserAccount());
		}

		if(user.getState() == null){
			user.setState(Const.UserState.ENABLE);
		}

		accountService.save(user);

		memberService.saveMember(member);
		return user;
	}

	/**
	 *
	 * @Title: fillUser
	 * @Description: TODO(保存微信中的用户信息)
	 * @param userJson
	 * @param user
	 * @throws
	 * @date 2015-5-12 下午11:29:12
	 */
	private Member fillUser(JSONObject userJson, User user) {
		user.setOpenId(userJson.getString("openid"));
		String nickname = userJson.getString("nickname");
//		user.setNickName(StringUtilsEx.transferString(nickname));
		user.setNickName(nickname);
		user.setSource(Const.UserSource.WEIXIN_H5);

		MemberLevel memberLevel = memberLevelService.findMemberLevelByMinValue(0);
		Member member = new Member(null, 0, 0, memberLevel, user, true, true,null,null,null,null,null);
		member.setHeadImg(userJson.getString("headimgurl"));

		Date now = new Date();
		//user.setLoginTime(now);
		if(!user.hasId()){
			user.setCreateTime(now);
		}
		user.setEditTime(now);

		return member;
	}
	
	
	/**
	 * 关于我们
	 * create superT 2016-5-19
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value = "aboutus")
	public String aboutUs(Model model) {
		return VIEW_URI_PREFIX + "about_us";
	}
	
	/**
	 * 联系我们
	 * create superT 2016-5-19
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "contactus")
	public String contactUs(Model model) {
		return VIEW_URI_PREFIX + "contactus";
	}
	
	/**
	 * 充值
	 * create superT 2016-5-19
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value = "rechargeWX")
	public String rechargeWX(Model model) {
		User user = UserWxUtils.getCurrUser();
		model.addAttribute("userId", user.getId());
		return VIEW_URI_PREFIX + "recharge";
	}
	
	/**
	 * 积分兑换
	 * create superT 2016-5-19
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value = "exchangeWX")
	public String exchangeWX(Model model) {
		User user = UserWxUtils.getCurrUser();
		Member member=memberService.findMember(user.getId());
		model.addAttribute("member", member);
		model.addAttribute("userId", user.getId());
		return "wx/record/jifen/jifen-exchange";
	}
	
	/**
	 * 积分提现
	 * create superT 2016-5-19
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value = "withdrawalsWX")
	public String withdrawalsWX(Model model) {
		User user = UserWxUtils.getCurrUser();
		Member member=memberService.findMember(user.getId());
		model.addAttribute("member", member);
		model.addAttribute("userId", user.getId());
		String fee = "5";
		Config config = configService.get(ConfigService.WITHDRAW_FEE);
		if (config != null && StringUtils.isNotEmpty(config.getValue())) {
			fee = config.getValue();
		}
		model.addAttribute("withDrawFee", fee);
		return "wx/record/jifen/jifen-withdrawals";
	}
	
	/**
	 * 绑定QQ
	 * create superT 2016-5-19
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value = "bindQQWX")
	public String bindQQWX(Model model) {
		User user = UserWxUtils.getCurrUser();
		model.addAttribute("userId", user.getId());
		return VIEW_URI_PREFIX + "about_us";
	}
	
	/**
	 * 绑定手机
	 * create superT 2016-5-19
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value = "bindPhoneWX")
	public String bindPhoneWX(Model model) {
		User user = UserWxUtils.getCurrUser();
		model.addAttribute("userId", user.getId());
		return VIEW_URI_PREFIX + "phone-verification";
	}
	
	/**
	 * 绑定卡号
	 * create superT 2016-5-19
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value = "bindAccountWX")
	public String bindAccountWX(Model model,@RequestParam(value = "type", required = false) Integer type) {
		User user = UserWxUtils.getCurrUser();
		model.addAttribute("userId", user.getId());
		if(type==1)
		{
			return VIEW_URI_PREFIX + "bind-card";
		}else
		{
			return VIEW_URI_PREFIX + "bind-alipay";
		}
	}
}
