package com.zx.stlife.repository.jpa.goods;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.goods.GoodsTimes;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoodsTimesDao extends MyJpaRepository<GoodsTimes, Integer>{
	
	@Modifying
	@Query("update GoodsTimes t set t.winngUserHeadImg=?1 where t.winngUser.id=?2")
	public int updateUserHeadImg(String userHeadImg, Integer userId);
	
	@Modifying
	@Query("update GoodsTimes set hasShareGoods=?1 where id=?2")
	public int updateHasShareGoods(boolean states, Integer goodsTimesId);

	@Query("select t from GoodsTimes t where t.goodsInfo.id=?1 and t.state=?2")
	GoodsTimes getLastByGoods(Integer gid, Byte state);

	@Query("select count(t.id) from GoodsTimes t where t.goodsInfo.id=?1 and t.state=?2")
	Integer getLastCount(Integer gid, Byte state);
	
	@Query("select t.id from GoodsTimes t where t.goodsInfo.id=?1 and t.state=?2")
	Integer getNewestTimesid(Integer gid, Byte state);
	
	@Query("select t from GoodsTimes t where t.winngUser.id=?1")
	List<GoodsTimes> getTimesList(Integer winngUserId);

	@Query("select t from GoodsTimes t where t.state=?1")
	List<GoodsTimes> findByState(Byte state);

	@Query("select count(t.id) from GoodsTimes t where t.goodsInfo.id=?1 and t.state=?2")
	Integer hasLastByGoods(Integer gid, Byte state);

	@Query("select t.state from GoodsTimes t where t.id=?1")
	Byte getState(Integer id);

	@Query("select count(id) from GoodsTimes where id=?1 and winngUser.id=?2")
	Integer countByIdAndUser(Integer id, Integer userId);
	
	@Query("select t from GoodsTimes t where t.times=?1")
	GoodsTimes getByTimes(Integer times);
}
