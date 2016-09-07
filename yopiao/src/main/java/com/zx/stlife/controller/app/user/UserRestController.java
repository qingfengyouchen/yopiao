package com.zx.stlife.controller.app.user;

import java.io.File;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.base.modules.util.ConvertUtils;
import com.base.modules.util.FileUtilsEx;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.app.base.BaseRestController;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.entity.goods.GoodsInfo;
import com.zx.stlife.entity.member.Member;
import com.zx.stlife.entity.member.MemberLevel;
import com.zx.stlife.entity.record.RedPack;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.goods.GoodsService;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.goods.ShareGoodsService;
import com.zx.stlife.service.member.MemberLevelService;
import com.zx.stlife.service.member.MemberService;
import com.zx.stlife.service.order.SnatchRecordDetailService;
import com.zx.stlife.service.record.RedPackService;
import com.zx.stlife.service.sms.SmsSendRecordService;
import com.zx.stlife.service.sys.AccountService;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.tools.DateUtils;
import com.zx.stlife.tools.PasswordUtils;

/**
 * 用户服务API
 */

@RestController
@RequestMapping("/app/user")
public class UserRestController extends BaseRestController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberLevelService memberLevelService;

	@Autowired
	private SmsSendRecordService smsSendRecordService;

	@Autowired
	private RedPackService redPackService;

	@Autowired
	private ShareGoodsService shareGoodsService;

	@Autowired
	private GoodsTimesService goodsTimesService;

	@Autowired
	private SnatchRecordDetailService snatchRecordDetailService;

    @Autowired
    private GoodsService goodsService;

	/**
	 * 获取手机验证码
	 */
	@RequestMapping(value = "/sendSmsCode", method = RequestMethod.GET)
	public JsonResult getValidateCode(
			@RequestParam("mobileNo") String mobileNo,
			@RequestParam("type") Integer type, @RequestParam("uid") String uid) {

		if (!SimpleUtils.isMobileNo(mobileNo)) {
			return buildFailureResult(301); // 手机号码无效
		}

		try {
			boolean flag = accountService.isExistsMobileNo(mobileNo, null);
			if (2 == type) { // 忘记密码时发送验证码
				if (flag) {
					smsSendRecordService.sendSms4Code(mobileNo, uid);
					return buildSuccessResult();
				} else {
					return buildFailureResult(303); // 手机号码不存在
				}
			} else if (4 == type) { // 第三方绑定手机号码
				smsSendRecordService.sendSms4Code(mobileNo, uid);
				return buildSuccessResult();
			} else { // 注册、修改手机号码时发送验证码
				if (flag) {
					return buildFailureResult(302); // 该手机号码已被注册
				} else {
					smsSendRecordService.sendSms4Code(mobileNo, uid);
					return buildSuccessResult();
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult(); // 发送验证码异常
		}
	}

	/**
	 * 用户注册
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public JsonResult register(@RequestParam("smsCode") String smsCode,
			@RequestParam("mobileNo") String mobileNo,
			@RequestParam("password") String password,
			@RequestParam("uid") String uid,
			@RequestParam(value = "referralCode", required = false) String referralCode) {

		if (!SimpleUtils.isMobileNo(mobileNo)) {
			return buildFailureResult(301); // 手机号码无效
		}

		if (!PasswordUtils.isValidPassword(password)) {
			return buildFailureResult(304); // 密码格式不正确，长度为8-16位，且必须包含数字、字母、符号两种以上
		}

		if (accountService.isExistsMobileNo(mobileNo, null)) {
			return buildFailureResult(302); // 该手机号码已被注册
		}
		
		User oldUser = null;
		if (StringUtils.isNotBlank(referralCode)) {
			oldUser = accountService.findUserByUserName(referralCode);
			if(oldUser == null){
				return buildFailureResult(305); // 推荐码不存在
			}
		}

		try {
			if (smsSendRecordService.checkValidCode(uid, smsCode, true)) {
				User user = new User();
				user.setMobileNo(mobileNo);
				user.setUserName(configService.getUserAccount());
				user.setTrueName(referralCode);
				user.setPlainPassword(password);
				user.setState(Const.UserState.ENABLE);
				user.setGender(Const.Gender.SECRET);
				accountService.saveUser(user, null);

				MemberLevel memberLevel = memberLevelService.findMemberLevelByMinValue(0);
				Member member = new Member(referralCode, 0, 0, memberLevel, user, true, true,null,null,null,null,null);
				memberService.saveMember(member);
				
				if(oldUser != null){
					redPackService.redPackRewardToUser(oldUser.getId()); // 老用户推荐获得红包奖励
				}

				Map<String, Object> data = new HashMap<String, Object>(1);
				data.put("userId", user.getId());
				return buildSuccessResult(data);
			} else {
				return buildFailureResult(303); // 验证码错误
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 用户登录
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public JsonResult login(@RequestParam("mobileNo") String mobileNo,
			@RequestParam("password") String password) {
		try {
			User user = accountService.findByMobileNoAndSource(mobileNo, Const.UserSource.REGISTER);
			if (user == null) {
				return buildFailureResult(301); // 手机号不存在
			}

			if (user.getState() == Const.UserState.ENABLE) {
				String hexPwd = accountService.encodePassword(user.getSalt(), password);
				if (user.getPassword() != null
						&& StringUtils.equals(user.getPassword(), hexPwd)) {
					Map<String, Object> data = new HashMap<String, Object>(1);
					data.put("userId", user.getId());
					return buildSuccessResult(data);
				} else {
					return buildFailureResult(303); // 密码错误
				}
			} else {
				return buildFailureResult(302); // 账号被屏蔽
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 忘记密码/修改密码
	 */
	@RequestMapping(value = "/modifyPwd", method = RequestMethod.POST)
	public JsonResult modifyPwd(@RequestParam("mobileNo") String mobileNo,
			@RequestParam("password") String password,
			@RequestParam("smsCode") String smsCode,
			@RequestParam("uid") String uid) {

		if (!PasswordUtils.isValidPassword(password)) {
			return buildFailureResult(304); // 密码格式不正确，长度为8-16位，且必须包含数字、字母、符号两种以上
		}

		try {
			if (smsSendRecordService.checkValidCode(uid, smsCode, true)) {
				User user = accountService.findByMobileNoAndSource(mobileNo, Const.UserSource.REGISTER);
				if (user == null) {
					return buildFailureResult(301); // 手机号码不存在
				} else {
					user.setPlainPassword(password);
					accountService.saveUser(user, null);

					Map<String, Object> data = new HashMap<String, Object>(1);
					data.put("userId", user.getId());
					return buildSuccessResult(data);
				}
			} else {
				return buildFailureResult(302); // 验证码不正确
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 获取用户资料
	 */
	@RequestMapping(value = "/getInfo", method = RequestMethod.GET)
	public JsonResult getInfo(@RequestParam("userId") Integer userId) {
		try {
			Map<String, Object> data = memberService.findMemberInfo(userId);
			if (data == null) {
				return buildFailureResult();
			}
			return buildSuccessResult(data);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 修改昵称
	 */
	@RequestMapping(value = "/modifyNickName", method = RequestMethod.POST)
	public JsonResult modifyNickName(@RequestParam("userId") Integer userId,
			@RequestParam("nickName") String nickName) {
		try {
			User user = accountService.getUser(userId);
			if (user == null) {
				return buildFailureResult();
			} else {
				user.setNickName(nickName);
				accountService.save(user);
				return buildSuccessResult();
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 修改性别
	 */
	@RequestMapping(value = "/modifyGender", method = RequestMethod.POST)
	public JsonResult modifyGender(@RequestParam("userId") Integer userId,
			@RequestParam("gender") Byte gender) {
		try {
			User user = accountService.getUser(userId);
			if (user == null) {
				return buildFailureResult();
			} else {
				user.setGender(gender);
				accountService.save(user);
				return buildSuccessResult();
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 修改手机号码
	 */
	@RequestMapping(value = "/modifyMobileNo", method = RequestMethod.POST)
	public JsonResult modifyMobileNo(@RequestParam("userId") Integer userId,
			@RequestParam("smsCode") String smsCode,
			@RequestParam("mobileNo") String mobileNo,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam("uid") String uid) {

		if (!SimpleUtils.isMobileNo(mobileNo)) {
			return buildFailureResult(301); // 手机号码无效
		}

		User user = accountService.getUser(userId);
		if (user != null && mobileNo.equals(user.getMobileNo())) {
			return buildFailureResult(302); // 手机号尚未修改
		}

		if (!user.isFromThirdpart()) {
			if (accountService.isExistsMobileNo(mobileNo, null)) {
				return buildFailureResult(303); // 该手机号码已使用
			}
		} 

		try {
			if (smsSendRecordService.checkValidCode(uid, smsCode, true)) {
				if (user.isFromThirdpart()) {
					user.setMobileNo(mobileNo);
					accountService.save(user);
					return buildSuccessResult();
				} else {
					String hexPwd = accountService.encodePassword(user.getSalt(), password);
					if (user.getPassword() != null
							&& StringUtils.equals(user.getPassword(), hexPwd)) {
						user.setMobileNo(mobileNo);
						accountService.save(user);
						return buildSuccessResult();
					} else {
						return buildFailureResult(305); // 确认密码错误
					}
				}
			} else {
				return buildFailureResult(304); // 验证码错误
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 修改出生日期
	 */
	@RequestMapping(value = "/modifyBirthday", method = RequestMethod.POST)
	public JsonResult modifyBirthday(@RequestParam("userId") Integer userId,
			@RequestParam("birthday") String birthdayStr) {
		try {
			Member member = memberService.findMember(userId);
			if (member == null) {
				return buildFailureResult();
			} else {
				Date birthday = DateUtils
						.YYYYMMDDHHMMSSSSSStringToDate(birthdayStr);
				member.setBirthday(birthday);
				memberService.saveMember(member);
				return buildSuccessResult();
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 修改个人头像
	 */
	@RequestMapping(value = "/modifyHeadImg", method = RequestMethod.POST)
	public JsonResult modifyHeadImg(@RequestParam("userId") Integer userId,
			@RequestParam("file") MultipartFile file) {
		try {
			Member member = memberService.findMember(userId);
			if (member == null) {
				return buildFailureResult();
			} else {
				String headImg = member.getHeadImg();
				headImg = uploadFile(file, headImg);
				member.setHeadImg(headImg);
				memberService.saveMember(member);

				shareGoodsService.updateUserHeadImg(headImg, userId);
				goodsTimesService.updateUserHeadImg(headImg, userId);
				snatchRecordDetailService.updateUserHeadImg(headImg, userId);

				Map<String, Object> data = new HashMap<String, Object>(1);
				data.put("headImg", headImg);
				return buildSuccessResult(data);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param file
	 * @param headImg
	 * @return
	 */
	private String uploadFile(MultipartFile file, String headImg) {
		try {
			if (headImg != null || !"".equals(headImg)) {
				FileUtilsEx.deleteFile(Const.USER_HEADIMG_IMG_ROOT_PATH,
						headImg);
			}
			String fileName = SimpleUtils.getHibernateUUID();
			String extensionName = FileUtilsEx.getFileExtensionWithDot(file
					.getOriginalFilename());
			headImg = fileName + extensionName;

			String sourceImagePath = FileUtilsEx.joinPaths(
					Const.USER_HEADIMG_IMG_ROOT_PATH, headImg);
			File sourceImageFile = new File(sourceImagePath);
			FileUtilsEx.writeByteArrayToFile(sourceImageFile, file.getBytes());
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return headImg;
	}

	/**
	 * 查看他人个人信息
	 */
	@RequestMapping(value = "/getShort/{id}", method = RequestMethod.GET)
	public JsonResult getShortInfo(@PathVariable("id") Integer userId) {
		try {
			Member member = memberService.findMember(userId);
			if (member == null) {
				return buildFailureResult();
			} else {
				Map<String, Object> data = new HashMap<String, Object>(3);
				data.put("nickName", member.getUser().getNickName());
				data.put("userName", member.getUser().getUserName());
				data.put("headImg", member.getHeadImg());
				return buildSuccessResult(data);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 第三方帐号登录
	 */
	@RequestMapping(value = "/login4Other", method = RequestMethod.POST)
	public JsonResult loginOther(@RequestParam("openId") String openId,
			@RequestParam("nickName") String nickName,
			@RequestParam("gender") Byte gender,
			@RequestParam(value = "headImg", required = false) String headImg,
			@RequestParam(value = "source", required = false) Byte source) {
		try {
			User user = accountService.findUserByOpenId(openId);
			if (user == null) {
				user = new User();
				user.setUserName(configService.getUserAccount());
				user.setState(Const.UserState.ENABLE);
				user.setOpenId(openId);
				user.setNickName(nickName);
				user.setGender(gender);
				user.setSource(source == null ? Const.UserSource.WEIXIN_APP : source);
				accountService.save(user);
				
				MemberLevel memberLevel = memberLevelService.findMemberLevelByMinValue(0);
				Member member = new Member(null, 0, 0, memberLevel, user, true, true,null,null,null,null,null);
				member.setHeadImg(headImg);
				memberService.saveMember(member);
			}
			
			Boolean hasBind = true;
			if (StringUtils.isBlank(user.getMobileNo())) {
				hasBind = false;
			}
			Map<String, Object> data = new HashMap<String, Object>(2);
			data.put("userId", user.getId());
			data.put("hasBind", hasBind);
			return buildSuccessResult(data);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
	
	/**
	 * 第三方绑定手机号码
	 */
	@RequestMapping(value = "/bindMobileNo", method = RequestMethod.POST)
	public JsonResult bindMobileNo(@RequestParam("userId") Integer userId,
			@RequestParam("smsCode") String smsCode,
			@RequestParam("mobileNo") String mobileNo,
			@RequestParam("uid") String uid) {

		if (!SimpleUtils.isMobileNo(mobileNo)) {
			return buildFailureResult(301); // 手机号码无效
		}

		try {
			if (smsSendRecordService.checkValidCode(uid, smsCode, true)) {
				User user = accountService.getUser(userId);
				user.setMobileNo(mobileNo);
				accountService.save(user);
				return buildSuccessResult();
			} else {
				return buildFailureResult(304); // 验证码错误
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 获取用户的余额和红包
	 */
	@RequestMapping(value = "/balance", method = RequestMethod.GET)
	public JsonResult getBalance(@RequestParam("userId") Integer userId) {
		try {
			Integer balance = memberService.getBalanceByUser(userId);
			if (balance == null)
				balance = 0;

			Map<String, Object> data = new HashMap<String, Object>(2);
			data.put("balance", balance);

			List<RedPack> redPackList = redPackService.findCanUseByUser(userId, Const.RedPackCategory.ADMIN);
			List<Map<String, Object>> mapList = ConvertUtils
					.convertCollectionToListMap(redPackList,
							new String[] { "id", "redPackId" },
							new String[] { "total" },
							new String[] { "balance" },
							new String[] { "sourceType" },
							new String[] { "expireTimeStr" });
			data.put("redPacks", mapList);

			return buildSuccessResult(data);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
	
	/**
	 * created by superT 2016/5/6
	 * 绑定qq 
	 */
	@RequestMapping(value = "/bindQQ", method = RequestMethod.GET)
	public JsonResult bindQQ(@RequestParam("userId") Integer userId,@RequestParam("qq") long qq) {
		try {
			if (memberService.isExistsQQ(qq)) {
				return buildFailureResult(303); // 该qq号码已使用
			}else
			{
				Member member = memberService.findMember(userId);
				if (member == null) {
					return buildFailureResult();
				} else {
					member.setQq(qq);
					memberService.saveMember(member);
					Map<String, Object> data = new HashMap<String, Object>(3);
					data.put("qq", member.getQq());
					return buildSuccessResult(data);
				}
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
	
	/**
	 * created by superT 2016/5/6
	 * 绑定bindAlipayOrBank
	 */
	@RequestMapping(value = "/bindAlipayOrBank", method = RequestMethod.GET)
	public JsonResult bindAlipayOrBank(@RequestParam("userId") Integer userId,@RequestParam("type") Integer type,
			@RequestParam("accountName") String accountName,@RequestParam("accountCode") String accountCode,@RequestParam("accountBank") String accountBank) {
		try {
			accountName = URLDecoder.decode(accountName, "UTF-8");
			accountCode = URLDecoder.decode(accountCode, "UTF-8");
			accountBank = URLDecoder.decode(accountBank, "UTF-8");
			if (memberService.isExistsAlipay(accountCode, type)) {
				return buildFailureResult(303); // 该账户已使用
			}else
			{
				Member member = memberService.findMember(userId);
				if (member == null) {
					return buildFailureResult();
				} else {
					member.setType(type);
					member.setAccountName(accountName);
					member.setAccountCode(accountCode);
					member.setAccountBank(accountBank);
					memberService.saveMember(member);
					Map<String, Object> data = new HashMap<String, Object>(3);
					data.put("type", member.getType());
					data.put("accountCode", member.getAccountCode());
					return buildSuccessResult(data);
				}
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
	
	/**
	 * 获取用户资料
	 */
	@RequestMapping(value = "/addPoints", method = RequestMethod.GET)
	public JsonResult addPoints(
			@RequestParam("userId") Integer userId,
			@RequestParam("goodId") Integer goodId,
			@RequestParam("points") Integer points
			) {
		try {
			Member member = memberService.findMember(userId);
			// 会员不存在
			if (member == null) {
				return buildFailureResult(301);
			}
			
	        GoodsInfo goodsInfo = goodsService.get(goodId);
	        if(goodsInfo == null){
	            return buildFailureResult(301); // 商品期号不存在
	        }

			memberService.recordJifen(userId, points,
					"id为" + goodId + "的商品兑换" + points + "积分");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("result", "OK");
			return buildSuccessResult(result);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
	
	/**
	 * 获取一个随机数字
	 */
	@RequestMapping(value = "/getRandomNum", method = RequestMethod.GET)
	public JsonResult getRandomNum() {
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			java.util.Random random = new java.util.Random();
			int num = random.nextInt(100);
			result.put("randomNum", num);
			result.put("result", "OK");
			return buildSuccessResult(result);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
	
}
