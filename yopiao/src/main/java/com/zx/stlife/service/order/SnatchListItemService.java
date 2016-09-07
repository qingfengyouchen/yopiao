package com.zx.stlife.service.order;

import java.util.List;

import com.zx.stlife.vo.snatch.SnatchListVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.order.SnatchListItem;
import com.zx.stlife.repository.jpa.order.SnatchListItemDao;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class SnatchListItemService {

	private static Logger logger = LoggerFactory.getLogger(SnatchListItemService.class);

	private static final String HQL_FIND_SNATCHLISTITEM="select s from SnatchListItem s where s.user.id=?1 and s.state=?2";
	
	@Autowired
	private SnatchListItemDao snatchListItemDao;
	/**
	 * 查看清单 
	 * @param userId
	 */
	public List<SnatchListItem> getList(Integer userId){
		logger.info("===SnatchListItemService before:" + userId);
		List<SnatchListItem> snatchListItems=snatchListItemDao.find(HQL_FIND_SNATCHLISTITEM, userId, Const.CommonState.ENABLE);
		logger.info("===SnatchListItemService after:" + snatchListItems);
		logger.info("===SnatchListItemService after:" + snatchListItems.size());
		return snatchListItems;
	}
	
	/**
	 * 根据用户获取清单总数 
	 */
	public Integer getUserListCount(Integer userId){
		return snatchListItemDao.getUserListCount(userId,Const.CommonState.ENABLE);
	}
	
	/**
	 * 条件查询这期清单
	 * @param userId 用户id
	 * @param goodsTimesId 商品期数id
	 * @return
	 */
	public SnatchListItem findBySnatchListItem(Integer userId,Integer goodsTimesId){
		SnatchListItem snatchListItem=snatchListItemDao.findBySnatchListItem(userId,goodsTimesId,Const.CommonState.ENABLE);
		return snatchListItem;
	}
	
	/**
	 * 修改清单
	 * @param id 清单id
	 * @param buyTimes 数量
	 * @param money 金额
	 * @return
	 */
	@Transactional
	public int update(Integer id,Integer buyTimes,Integer money){
		int state=snatchListItemDao.update(id, buyTimes, money);
		return state;
	}
	
	@Transactional
	public void save(SnatchListItem entity){
		snatchListItemDao.save(entity);
	}
	
	@Transactional
	public void delete(List<Integer> ids){
		for (Integer id : ids) {
			snatchListItemDao.delete(id);
		}
	}
	
	/**
	 * 删除用户指定的清单
	 * @param userId 用户id
	 * @param ids 清单id  list<ids>
	 */
	@Transactional
	public void delUserList(Integer userId,List<Integer> ids){
		snatchListItemDao.delUserList(userId, ids);
	} 

	/**
	 * 删除清单
	 * @param entity 清单实体
	 */
	@Transactional
	public void deleteEntity(SnatchListItem entity){
		snatchListItemDao.delete(entity);
	}

	@Transactional
	public void deleteEntity(Integer id){
		snatchListItemDao.delete(id);
	}
	
	/**
	 * 删除用户所有清单
	 * @param userId 用户id
	 */
	@Transactional
	public void delUserAllList(Integer userId){
		snatchListItemDao.delUserAllList(userId);
	}

	@Transactional
	public void deleteByGoodsTimes(List<Integer> goodsTimesIds){
		snatchListItemDao.deleteByGoodsTimes(goodsTimesIds);
	}

	public List<SnatchListVo> findSnatchListVoByUserAndGoodsTimes(Integer userId, List<Integer> goodsTimesIds){
		return snatchListItemDao.findSnatchListVoByUserAndGoodsTimes(userId, goodsTimesIds, Const.CommonState.ENABLE);
	}
}
