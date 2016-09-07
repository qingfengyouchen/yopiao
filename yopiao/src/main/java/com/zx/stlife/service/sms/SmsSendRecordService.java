package com.zx.stlife.service.sms;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.base.modules.cache.memcached.SpyMemcachedClient;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.sms.SmsSendRecord;
import com.zx.stlife.repository.jpa.sms.SmsSendRecordDao;
import com.zx.stlife.tools.memcached.MemcachedObjectType;
import com.zx.stlife.tools.sms.AlidayuSmsUtil;
import com.zx.stlife.tools.sms.SmsUtil;

@Component
@Transactional
public class SmsSendRecordService {

	@Autowired
	private SmsSendRecordDao smsSendRecordDao;

	@Autowired
	private SpyMemcachedClient spyMemcachedClient;

	/**中奖短信通知*/
	private final static String TEMPLATE_WINNG_NOTICE =
			Const.appPropertiesLoader.getPropertyWithChinese("sms.template.winng.notice");
	/**派发商品提示1*/
	private final static String TEMPLATE_DISPATCH_GOODS_TIPS1 =
			Const.appPropertiesLoader.getPropertyWithChinese("sms.template.dispatch.goods.tips1");
	/**派发商品提示2*/
	private final static String TEMPLATE_DISPATCH_GOODS_TIPS2 =
			Const.appPropertiesLoader.getPropertyWithChinese("sms.template.dispatch.goods.tips2");

	private final static String DEFAULT_SENDER = "系统";
	
    /** 验证码模板ID */
    private final static String MSGCODE_TPLID = Const.appPropertiesLoader
            .getProperty("ali.sms.msgcode.templateid");
    /** 中奖模板ID */
    private final static String WINNG_TPLID = Const.appPropertiesLoader
            .getProperty("ali.sms.winng.templateid");
    /** 发货通知模板ID */
    private final static String SENDGOOD_TPLID = Const.appPropertiesLoader
            .getProperty("ali.sms.sendgood.templateid");
    /**发货通知内容*/
	private final static String SENDGOOD_MSG =
			Const.appPropertiesLoader.getPropertyWithChinese("ali.sms.sendgood.msg");
    /**中奖通知内容*/
	private final static String WINNG_MSG =
			Const.appPropertiesLoader.getPropertyWithChinese("ali.sms.winng.msg");

	/**
	 * 发送通知中奖用户短信
	 * @param mobileNo
	 */
	public void sendSms4NoticeWinng(String mobileNo){
		if(StringUtils.isNotBlank(mobileNo)) {
//			sendSms(mobileNo, TEMPLATE_WINNG_NOTICE, DEFAULT_SENDER);
			sendSms(mobileNo, WINNG_MSG, DEFAULT_SENDER, WINNG_TPLID, null);
		}
	}

	/**
	 * 发送派发商品通知短信
	 * @param mobileNo
	 */
	public void sendSms4DispatchGoods(String mobileNo, String logisticsName, String logisticsNo){
//		sendSms(mobileNo, TEMPLATE_DISPATCH_GOODS_TIPS1, DEFAULT_SENDER);
//
//		String content = TEMPLATE_DISPATCH_GOODS_TIPS2
//				.replace("#expressName#", logisticsName)
//				.replace("#expressNo#", logisticsNo);
//		sendSms(mobileNo, content, DEFAULT_SENDER);
		sendSms(mobileNo, SENDGOOD_MSG, DEFAULT_SENDER, SENDGOOD_TPLID, null);
	}

	/**
	 * 发送短信
	 * 
	 * @param mobileNo
	 *            手机号码
	 * @param content
	 *            短信内容
	 * @param sender
	 *            发送者
	 */
	public void sendSms(String mobileNo, String content, String sender, String tplid, String paramString) {
		String resultCode = AlidayuSmsUtil.send(mobileNo, tplid, paramString);
		SmsSendRecord smsSendRecord = new SmsSendRecord(sender, mobileNo,
				content, resultCode);
		smsSendRecordDao.save(smsSendRecord);
	}
	
	/**
	 * 发送短信
	 * 
	 * @param mobileNo
	 *            手机号码
	 * @param content
	 *            短信内容
	 * @param sender
	 *            发送者
	 */
	public void sendSms(String mobileNo, String content, String sender) {
		String resultCode = SmsUtil.send(mobileNo, content, true, null, null);
		SmsSendRecord smsSendRecord = new SmsSendRecord(sender, mobileNo,
				content, resultCode);
		smsSendRecordDao.save(smsSendRecord);
	}

	/**
	 * 发送短信验证
	 * 
	 * @param mobileNo
	 *            手机号码
	 */
	public void sendSms4Code(String mobileNo, String id) {
		String code = RandomStringUtils.random(6, false, true);
//		String content = Const.SMS_CODE_TEMPLATE.replace("{smsCode}", code);
//		sendSms(mobileNo, content, "系统");
		String param = "{\"code\":\""
				+ code
				+ "\",\"product\":\"乐夺宝\"}";
		String content = "验证码" + code + "，您正在进行乐夺宝身份验证，打死不要告诉别人哦！";
		sendSms(mobileNo, content, "系统", MSGCODE_TPLID, param);

		String key = MemcachedObjectType.SMS_CODE.getPrefix() + id;
		spyMemcachedClient.safeSet(key,
				MemcachedObjectType.SMS_CODE.getExpiredTime(), code);
	}

	/**
	 * 验证短信验证码
	 * 
	 * @param id
	 * @param newCode
	 * @return boolean
	 */
	public boolean checkValidCode(String id, String newCode,
			boolean isClearCodeInCach) {
		String key = MemcachedObjectType.SMS_CODE.getPrefix() + id;
		String code = spyMemcachedClient.get(key);

		boolean isRight = StringUtils.isNotBlank(code)
				&& StringUtils.equals(code, newCode);
		if (isRight && isClearCodeInCach) {// 验证正确后就删除
			spyMemcachedClient.safeDelete(key);
		}
		return isRight;
	}

}
