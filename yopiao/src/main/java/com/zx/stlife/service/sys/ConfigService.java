package com.zx.stlife.service.sys;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.base.modules.util.DateUtilsEx;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.entity.sys.Config;
import com.zx.stlife.repository.jpa.sys.ConfigDao;

/**
 * 用户管理业务类.
 * 
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class ConfigService {

	private static Logger logger = LoggerFactory.getLogger(ConfigService.class);

	public static Lock LOCK_FOR_USER_ACCOUNT = new ReentrantLock();
	public static Lock LOCK_FOR_STATIC_HTML_NO = new ReentrantLock();
	public static Lock LOCK_FOR_GOODS_TIMES_NO = new ReentrantLock();

	/**用户名*/
	public static final String USER_ACCOUNT = "userAccount";
	/**html名称*/
	public static final String STATIC_HTML_NAME = "staticHtmlName";
	/**商品期号前缀*/
	public static final String GOODS_TIMES_NO_PREFIX = "goodsTimesNo_";
	/**红包活动开始时间*/
	public static final String REDPACK_ACTIVITY_OPENTIME = "redPackActivityOpenTime";
	/**红包活动结束时间*/
	public static final String REDPACK_ACTIVITY_ENDTIME = "redPackActivityEndTime";
	/**红包过期时间*/
	public static final String REDPACK_EXPIRETIME = "redPackExpireTime";
	/**积分提现手续费*/
	public static final String WITHDRAW_FEE = "withDrawFee";

	public static String USER_ACCOUNT_BASE = "61111111";
	public static String STATIC_HTML_BASE_NO = "111111";

	public static String FORMAT_MMDD = "MMdd";

	@Autowired
	private ConfigDao configDao;

	public Config get(String key){
		return configDao.findOne(key);
	}

	@Transactional
	public void update(String key, String value) {
		configDao.update(key, value);
	}

	@Transactional
	public void save(Config entity) {
		entity = configDao.save(entity);
	}

	@Transactional
	public String getStaticHtmlNo(){
		String no = null;
		try {
			LOCK_FOR_STATIC_HTML_NO.lock();
			Config config = get(STATIC_HTML_NAME);
			no = null;
			if (config == null) {
				no = STATIC_HTML_BASE_NO;
				config = new Config(STATIC_HTML_NAME, no);
			} else {
				int num = SimpleUtils.stringToInteger(config.getValue());
				num++;
				no = String.valueOf(num);
				config.setValue(no);
			}
			save(config);
		}finally{
			LOCK_FOR_STATIC_HTML_NO.unlock();
		}
		return no;
	}

	@Transactional
	public String getUserAccount(){
		String account = null;
		try {
			LOCK_FOR_USER_ACCOUNT.lock();
			Config config = get(USER_ACCOUNT);
			if (config == null) {
				account = USER_ACCOUNT_BASE;
				config = new Config(USER_ACCOUNT, account);
			} else {
				int num = SimpleUtils.stringToInteger(config.getValue());
				num++;
				account = String.valueOf(num);
				config.setValue(account);
			}
			save(config);
		}finally {
			LOCK_FOR_USER_ACCOUNT.unlock();
		}
		return account;
	}

	@Transactional
	public Integer getGoodsTimesNo(){
		String goodsTimesNo = null;
		try {
			LOCK_FOR_GOODS_TIMES_NO.lock();
			String MMddDateStr = DateUtilsEx.getDate(FORMAT_MMDD);
			String key = GOODS_TIMES_NO_PREFIX + MMddDateStr;
			Config config = get(key);
			if(config ==  null){
				int value = 1;
				goodsTimesNo = SimpleUtils.formatNumber(value, 4, "0");
				config = new Config(key, String.valueOf(value));

				deleteByLikeKey(GOODS_TIMES_NO_PREFIX);
			}else{
				int value = SimpleUtils.stringToInteger(config.getValue());
				goodsTimesNo = SimpleUtils.formatNumber(++value, 4, "0");
				config.setValue(String.valueOf(value));
			}
			save(config);

			goodsTimesNo = "8" + MMddDateStr + goodsTimesNo;
		}finally {
			LOCK_FOR_GOODS_TIMES_NO.unlock();
		}
		return SimpleUtils.stringToInteger(goodsTimesNo);
	}

	@Transactional
	public Integer getRedpackExpireDay(){
		Config config = get(REDPACK_EXPIRETIME);
		if(config ==  null){
			String defaultDay = "180"; // 默认180天
			config = new Config(REDPACK_EXPIRETIME, defaultDay);
			save(config);
		}
		return Integer.valueOf(config.getValue());
	}
	
	@Transactional
	public void deleteByLikeKey(String likeKey){
		configDao.deleteByLikeKey(likeKey);
	}
	
}
