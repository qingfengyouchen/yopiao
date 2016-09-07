package com.zx.stlife.service.order;

import java.util.List;

import com.base.modules.util.SimpleUtils;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.zx.stlife.entity.order.SnatchRecordDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.order.SnatchNum;
import com.zx.stlife.repository.jpa.order.SnatchNumDao;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class SnatchNumService {

	private static Logger logger = LoggerFactory.getLogger(SnatchNumService.class);

	private static String HQL_QUERY_NUM_BY_GOODS_TIMES_AND_USER ="select s.num from SnatchNum s where s.goodsTimes.id=?1 and s.user.id=?2";

	private static String HQL_FIND_SNATCHNUM="select s from SnatchNum s where s.snatchRecordDetail.id=?1";

	private static String HQL_SEL_SNATCHNUM="select s from SnatchNum s where s.goodsTimes.id=?1 and s.user.id=?2";

	private static String HQL_TOP_NUMS_BY_SNATCH_RECORD_DETAIL =
			"select t.num from SnatchNum t where t.snatchRecordDetail.id=?1";

	private static String QL_QUERY_RANDOM_1_BY_GOODS_TIMES =
			"select t from SnatchNum t where t.goodsTimes.id=?1 order by rand()";

	private static String QL_QUERY_BY_GOODS_TIMES_AND_NUM =
			"select t from SnatchNum t where t.goodsTimes.id=?1 and t.num=?2";

	private static String QL_QUERY_RANDOM1_BY_GOODS_TIMES_AND_USER =
			"select t.num from SnatchNum t where t.goodsTimes.id=?1 and t.user.id=?2 order by rand()";

	@Autowired
	private SnatchNumDao snatchNumDao;
	
	
	/**
	 * 查询前N个夺宝号码
	 * @param goodsTimesId
	 * @param userId
	 * @return
	 */
	public List<Integer> findTopNumsByGoodsTimesAndUser(int top, Integer goodsTimesId,Integer userId){
		List<Integer> snatchNums= snatchNumDao.findTop(
				top, HQL_QUERY_NUM_BY_GOODS_TIMES_AND_USER, goodsTimesId,userId);
		return snatchNums;
	}

	/**
	 * 查询前N个夺宝号码
	 * @param goodsTimesId
	 * @param userId
	 * @return
	 */
	public List<Integer> findTopNumsByGoodsTimesAndUserBuyTimes(int buyTimes, Integer goodsTimesId,Integer userId){
		int top = buyTimes == 7 ? 7 : 6;
		return findTopNumsByGoodsTimesAndUser(top, goodsTimesId, userId);
	}

	/**
	 * 查询夺宝号码
	 * @param goodsTimesId
	 * @param userId
	 * @return
	 */
	public List<Integer> findNumsByGoodsTimesAndUser(Integer goodsTimesId,Integer userId){
		List<Integer> snatchNums = snatchNumDao.find(HQL_QUERY_NUM_BY_GOODS_TIMES_AND_USER, goodsTimesId, userId);
		return snatchNums;
	}

	/**
	 * @param snatchRecordDetailId  订单明细id
	 * @return
	 */
	public List<SnatchNum> findSnatchNum(Integer snatchRecordDetailId){
		List<SnatchNum> snatchNums= snatchNumDao.find(HQL_FIND_SNATCHNUM, snatchRecordDetailId);
		return snatchNums;
	}
	
	/**
	 * 
	 * @param goodsTimesId 商品期号id
	 * @param userId 用户id
	 * @return
	 */
	public List<SnatchNum> listSnatchNum(Integer goodsTimesId,Integer userId){
		List<SnatchNum> snatchNums= snatchNumDao.find(HQL_SEL_SNATCHNUM, goodsTimesId,userId);
		return snatchNums;
	}
	
	@Transactional
	public void save(SnatchNum entity){
		entity = snatchNumDao.save(entity);
	}

	public List<Integer> findTopNumsBySnatchRecordDetail(int top, Integer snatchRecordDetailId){
		return snatchNumDao.findTop(top, HQL_TOP_NUMS_BY_SNATCH_RECORD_DETAIL, snatchRecordDetailId);
	}

	public List<Integer> findTopNumsByBuyTimesAndSnatchRecordDetail(int buyTimes, Integer snatchRecordDetailId){
		int top = buyTimes == 7 ? 7 : 6;
		return findTopNumsBySnatchRecordDetail(top, snatchRecordDetailId);
	}

	/**
	 * 随机获取1条夺宝号码信息
	 * @param goodsTimesId
	 * @return
	 */
	public SnatchNum getRandom1ByGoodsTimes(Integer goodsTimesId){
		return (SnatchNum)snatchNumDao.getObject(QL_QUERY_RANDOM_1_BY_GOODS_TIMES, goodsTimesId);
	}


	/**
	 * 查询夺宝号码
	 * @param goodsTimesId 	商品期号id
	 * @param num			夺宝号码
	 * @return
	 */
	public SnatchNum getByGoodsTimesAndNum(Integer goodsTimesId, Integer num){
		return (SnatchNum)snatchNumDao.getObject(QL_QUERY_BY_GOODS_TIMES_AND_NUM, goodsTimesId, num);
	}

	/**
	 * 随机获取1个夺宝号码
	 * @param goodsTimesId
	 * @return
	 */
	public Integer getRandom1NumByGoodsTimes(Integer goodsTimesId, Integer userId){
		List<Integer> list = snatchNumDao.findTop(1, QL_QUERY_RANDOM1_BY_GOODS_TIMES_AND_USER, goodsTimesId, userId);
		return SimpleUtils.isNullList(list) ? null : list.get(0);
	}

}
