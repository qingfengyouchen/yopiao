package com.zx.stlife.service.record;

import com.base.jpa.query.Page;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.order.SnatchRecordDetail;
import com.zx.stlife.entity.record.BuyRecord;
import com.zx.stlife.entity.record.Jifen;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.record.BuyRecordDao;
import com.zx.stlife.repository.jpa.record.JifenDao;
import com.zx.stlife.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class BuyRecordService {

	private static Logger logger = LoggerFactory.getLogger(BuyRecordService.class);

	private static String QL_QUERY_RANDOM1_BY_GOODS_TIMES =
			"select t from BuyRecord t where t.goodsTimes.id=?1 and t.state=?2 order by rand()";

	@Autowired
	private BuyRecordDao buyRecordDao;

	public BuyRecord get(Integer id){
		return buyRecordDao.findOne(id);
	}

	@Transactional
	public void save(BuyRecord entity){
		entity = buyRecordDao.save(entity);
	}

	@Transactional
	public void save(GoodsTimes goodsTimes, SnatchRecordDetail snatchRecordDetail, User user){
		BuyRecord buyRecord = new BuyRecord(goodsTimes, snatchRecordDetail, user);
		save(buyRecord);
	}

	public Long sumTimeValue(Integer goodsTimesId){
		Long sumValue = buyRecordDao.sumTimeValue(goodsTimesId, Const.CommonState.ENABLE);
		return sumValue;
	}

	public List<BuyRecord> findByGoodsTimes(Integer goodsTimesId){
		return buyRecordDao.findByGoodsTimes(goodsTimesId, Const.CommonState.ENABLE);
	}

	public BuyRecord getRandom1ByGoodsTimes(Integer goodsTimesId){
		List<BuyRecord> buyRecordList = buyRecordDao.findTop(1,
				QL_QUERY_RANDOM1_BY_GOODS_TIMES, goodsTimesId, Const.CommonState.ENABLE);

		return SimpleUtils.isNullList(buyRecordList) ? null : buyRecordList.get(0);
	}

	/**
	 * 修改购买记录
	 * @param buyRecord  购买记录
	 * @param user		 用来替换的用户
	 * @param snatchDate 夺宝时间
	 */
	@Transactional
	public void changeUserAndSnatchTime(BuyRecord buyRecord, User user, Date snatchDate){
		buyRecord.setUser(user);
		buyRecord.setUserNickName(user.getNickName());
		buyRecord.setSnatchTime(snatchDate.getTime());
		buyRecord.setTimeValue(DateUtils.millisecondsToHHMMSSSSS(buyRecord.getSnatchTime()));
		save(buyRecord);

		logger.info("购买记录id【{}】被修改", buyRecord.getId());
	}

}
