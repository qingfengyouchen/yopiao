package com.zx.stlife.service.order;

import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.order.SnatchRecordDetail;
import com.zx.stlife.repository.jpa.order.SnatchRecordDetailDao;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class SnatchRecordDetailService {

	private static Logger logger = LoggerFactory.getLogger(SnatchRecordDetailService.class);

	private static String HQL_FIND_SNATCHRECORDDETAIL =
			"select s from SnatchRecordDetail s where s.snatchRecord.id=?1 and s.state=?2";

	private static String QL_QUERY_TOP =
			"select t from SnatchRecordDetail t where t.state=?1 " +
				"and t.snatchTime<=?2 order by t.snatchTime desc";

	private static String QL_QUERY_BY_GOODS_TIMES =
			"select s from SnatchRecordDetail s where s.goodsTimes.id=?1 " +
					"and s.createTime<=?2 and s.state=?3 order by s.snatchTime desc";

	@Autowired
	private SnatchRecordDetailDao snatchRecordDetailDao;

	@Transactional
	public void save(SnatchRecordDetail entity){
		if(entity.getSnatchTime() ==  null){
			entity.setSnatchTime(entity.getCreateTime().getTime());
		}
		entity = snatchRecordDetailDao.save(entity);
	}

	/**
	 * 夺宝记录
	 * @param date
	 * @param page
	 * @param goodsTimesId
	 */
	public void findList(Page<SnatchRecordDetail> page, Integer goodsTimesId, Date date){
		snatchRecordDetailDao.queryPage(page, QL_QUERY_BY_GOODS_TIMES,
				goodsTimesId, date, Const.PayResult.PAY_SUCCESS);
	}
	
	
	/**
	 * @param snatchRecordId 夺宝记录id
	 * @return
	 */
	public List<SnatchRecordDetail> findSnatchRecordDetail(Integer snatchRecordId){
		List<SnatchRecordDetail> recordDetails  = snatchRecordDetailDao
				.find(HQL_FIND_SNATCHRECORDDETAIL, snatchRecordId, Const.PayResult.PAY_SUCCESS);
		return recordDetails;
	}

	public List<SnatchRecordDetail> findByPayRecord(Integer payRecordId){
		return snatchRecordDetailDao.findByPayRecord(payRecordId);
	}
	
	/**
	 * 修改用户头像
	 * 
	 * @param userHeadImg
	 * @param userId
	 */
	@Transactional
	public void updateUserHeadImg(String userHeadImg, Integer userId) {
		snatchRecordDetailDao.updateUserHeadImg(userHeadImg, userId);
	}

	/**
	 * 查询全站前N条数据
	 * @param top
	 * @return
	 */
	public List<SnatchRecordDetail> findTop(int top, Long maxSnatchTime){
		return snatchRecordDetailDao
				.findTop(top, QL_QUERY_TOP, Const.PayResult.PAY_SUCCESS, maxSnatchTime);
	}
}
