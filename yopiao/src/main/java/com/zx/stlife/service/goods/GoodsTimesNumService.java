package com.zx.stlife.service.goods;

import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.base.exception.BizException;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.goods.GoodsTimesNum;
import com.zx.stlife.entity.order.SnatchNum;
import com.zx.stlife.entity.order.SnatchRecordDetail;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.goods.GoodsTimesDao;
import com.zx.stlife.repository.jpa.goods.GoodsTimesNumDao;
import com.zx.stlife.service.order.SnatchNumService;
import com.zx.stlife.tools.RandomUitls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Service
@Transactional(readOnly = true)
public class GoodsTimesNumService {

	private static Logger logger = LoggerFactory.getLogger(GoodsTimesNumService.class);

	private static String QL_QUERY_RANDOM_LIMIT_BY_GOODS_TIMES =
		"select t from GoodsTimesNum t where t.goodsTimes.id=?1 order by rand()";

	private static final Map<Integer, Lock> lockMap = new ConcurrentHashMap<>();

	@Autowired
	private GoodsTimesNumDao goodsTimesNumDao;
	@Autowired
	private SnatchNumService snatchNumService;

	public GoodsTimesNum get(Integer id) {
		return goodsTimesNumDao.findOne(id);
	}

	@Transactional
	public void save(GoodsTimesNum entity) {
		entity = goodsTimesNumDao.save(entity);
	}

	@Transactional
	public void createNums(GoodsTimes goodsTimes){
		if (null != goodsTimes) {
			int tottalTimes = goodsTimes.getTotalTimes();
			for(int i = 0; i < tottalTimes; i++){
				GoodsTimesNum goodsTimesNum = new GoodsTimesNum(goodsTimes, Const.SNATCH_BASE_NUM + i);
				save(goodsTimesNum);
			}
		}
	}

	public GoodsTimesNum getByGoodsTimesAndNum(Integer goodsTimesId, Integer num){
		return goodsTimesNumDao.getByGoodsTimesAndNum(goodsTimesId, num);
	}

	@Transactional
	public void delete(GoodsTimesNum entity){
		goodsTimesNumDao.delete(entity);
	}

	public List<Integer> findNumByGoodsTimes(Integer goodsTimesId){
		return goodsTimesNumDao.findNumByGoodsTimes(goodsTimesId);
	}

	/**
	 * 随机获取号码
	 * @param goodsTimesId
	 * @param amount
	 * @return
	 */
	public List<GoodsTimesNum> findRandomNumByGoodsTimes(Integer goodsTimesId, int amount){
		return goodsTimesNumDao.findTop(amount, QL_QUERY_RANDOM_LIMIT_BY_GOODS_TIMES, goodsTimesId);
	}

	/**
	 * 产生夺宝号码
	 * @param snatchRecordDetail
	 * @return
	 */
	public Integer[] produceSnatchNum(SnatchRecordDetail snatchRecordDetail) throws BizException{
		GoodsTimes goodsTimes = snatchRecordDetail.getGoodsTimes();
		User user = snatchRecordDetail.getUser();
		int buyTimes  = snatchRecordDetail.getBuyTimes();

		Lock lock = lockMap.get(goodsTimes.getId());
		if(lock == null ){
			lockMap.put(goodsTimes.getId(), lock = new ReentrantLock());
		}

		Integer[] nums = null;

		lock.lock();
		try{
			
			logger.info("==========goodsTimes.getId() :" + goodsTimes.getId());
			logger.info("==========buyTimes :" + buyTimes);
			List<GoodsTimesNum> goodsTimesNumList = findRandomNumByGoodsTimes(goodsTimes.getId(), buyTimes);
			
			
			logger.info("==========goodsTimesNumList.size : " +goodsTimesNumList.size());
			if(SimpleUtils.isNullList(goodsTimesNumList)){
				logger.info("==========获取的夺宝号码为空");
				throw new BizException("获取的夺宝号码为空");
			}
			int numSize = buyTimes == 7 ? 7 : (buyTimes > 7 ? 6 : buyTimes);
			nums = new Integer[numSize];
			int i = 0;
			for(GoodsTimesNum goodsTimesNum: goodsTimesNumList){
				SnatchNum snatchNum = new SnatchNum(
						goodsTimesNum.getNum(), snatchRecordDetail, goodsTimes, user);
				snatchNumService.save(snatchNum);
				if(i < numSize) {
					nums[i++] = goodsTimesNum.getNum();
				}

				delete(goodsTimesNum);
			}
		}finally {
			lock.unlock();
		}

		return nums;
	}
}
