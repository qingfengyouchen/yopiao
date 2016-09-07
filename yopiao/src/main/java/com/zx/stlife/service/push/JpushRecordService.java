package com.zx.stlife.service.push;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zx.stlife.entity.sys.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.jpush.api.push.PushResult;

import com.base.modules.util.SimpleUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.push.JpushRecord;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.push.JpushRecordDao;
import com.zx.stlife.service.sys.AccountService;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.tools.DateUtils;
import com.zx.stlife.tools.jpush.JPushUtils;

/**
 * 推送记录管理业务类
 */

@Component
@Transactional(readOnly = true)
public class JpushRecordService {
	
	private static Logger logger = LoggerFactory.getLogger(JpushRecordService.class);
	
	@Autowired
	private JpushRecordDao jpushRecordDao;
	
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	private static final String DEFAULT_SENDER = "系统";
	/**分享标题 */
	private static final String shareTitle = Const.appPropertiesLoader.getPropertyWithChinese("share.title");
	/**分享描述 */
	private static final String shareDescription = Const.appPropertiesLoader.getPropertyWithChinese("share.description");
	/**分享图片 */
	private static final String shareImage = Const.ROOT_URI + Const.appPropertiesLoader.getProperty("share.image");
	/**分享链接 */
	private static final String shareLink = Const.ROOT_URI + Const.appPropertiesLoader.getProperty("share.link");
	
	/**
	 * 后台处理推送
	 * 
	 * @param userId
	 */
	public void pushProcess(final Integer userId) {
		threadPoolTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					pushShareMessage(userId);
				} catch (Exception ex){
					logger.error(ex.getMessage(), ex);
				}
			}
		});
	}
	
	/**
	 * 推送红包活动分享消息
	 * 
	 * @param userId
	 */
	private void pushShareMessage(Integer userId) {
		Date currentDate = DateUtils.getNow();
		Date startDate = DateUtils.resetDateFields(currentDate, 
				new int[] { Calendar.HOUR_OF_DAY, 0 },
				new int[] { Calendar.MINUTE, 0 },
				new int[] { Calendar.SECOND, 0 },
				new int[] { Calendar.MILLISECOND, 0 });
		Date endDate = DateUtils.resetDateFields(currentDate,
				new int[]{Calendar.HOUR_OF_DAY, 23},
				new int[]{Calendar.MINUTE, 59},
				new int[]{Calendar.SECOND, 59},
				new int[]{Calendar.MILLISECOND, 999});

		Config openTimeConfig = configService.get(ConfigService.REDPACK_ACTIVITY_OPENTIME);
		Config endTimeConfig = configService.get(ConfigService.REDPACK_ACTIVITY_ENDTIME);
		if ((openTimeConfig != null) && (endTimeConfig != null)) {
			Date openTime = SimpleUtils.stringToDate(openTimeConfig.getValue());
			Date endTime = SimpleUtils.stringToDate(endTimeConfig.getValue());
			if (currentDate.before(openTime)) {
				return ; // 活动未开始
			}
			
			User user = accountService.getUser(userId);
			if (user != null) {
				Date registerTime = user.getCreateTime();
				if (registerTime.before(openTime) || registerTime.after(endTime)) {
					return ; // 不是活动期间注册的新用户
				}
				
				Long pushCount = jpushRecordDao.findJpushCountByUser(userId, registerTime, true);
				if (pushCount > 7){
					return ; // 新用户已领取7天红包
				}
				
				List<Integer> userIds = jpushRecordDao.findJpushUserByDate(startDate, endDate, true);
				if (SimpleUtils.isNotNullList(userIds)) {
					List<String> mobileNoList = accountService.getMobileNo(userIds);
					if (mobileNoList.contains(user.getMobileNo())) {
						return ; // 手机号码相同视为同一用户
					}
				}
			}
		}
		
		JpushRecord jpushRecord = jpushRecordDao.findJpushRecordByUser(userId, startDate, endDate, true);
		if (jpushRecord == null) {
			List<String> alias = Arrays.asList(String.valueOf(userId));
			
			Map<String, String> extras = new HashMap<String, String>(5);
			extras.put(Const.PUSH_MESSAGE_TYPE, Const.SHARE_TYPE);
			extras.put(Const.SHARE_TITLE, shareTitle);
			extras.put(Const.SHARE_DESCRIPTION, shareDescription);
			extras.put(Const.SHARE_IMAGE, shareImage);
			extras.put(Const.SHARE_LINK, shareLink);
	
			PushResult result = JPushUtils.sendPushMessage(alias, extras);
			if (result != null) {
				jpushRecord = new JpushRecord(DEFAULT_SENDER, userId, result.msg_id, result.isResultOK());
				save(jpushRecord);
			}
		}
	}
	
	/**
	 * 保存推送记录
	 * 
	 * @param jpushRecord
	 */
	@Transactional
	public void save(JpushRecord jpushRecord) {
		jpushRecordDao.save(jpushRecord);
	}

}
