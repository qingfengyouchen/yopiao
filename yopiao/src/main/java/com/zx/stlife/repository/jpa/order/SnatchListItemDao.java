package com.zx.stlife.repository.jpa.order;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.order.SnatchListItem;
import com.zx.stlife.vo.snatch.SnatchListVo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SnatchListItemDao extends MyJpaRepository<SnatchListItem, Integer>{

	@Query("select s from SnatchListItem s where s.user.id=?1 and s.goodsTimes.id=?2 and s.state=?3")
	SnatchListItem findBySnatchListItem(Integer userId,Integer goodsTimesId,Byte state);

	@Modifying
	@Query("update SnatchListItem s set s.buyTimes=:buyTimes ,s.money=:money where s.id=:id")
	int update(@Param("id")Integer id,@Param("buyTimes")Integer buyTimes,@Param("money")Integer money);

	@Modifying
	@Query("delete from SnatchListItem s where s.user.id=:userId and s.id in (:ids)")
	void delUserList(@Param("userId")Integer userId,@Param("ids")List<Integer> ids);

	@Modifying
	@Query("delete from SnatchListItem s where s.user.id=:userId")
	void delUserAllList(@Param("userId")Integer userId);

	@Modifying
	@Transactional
	@Query("delete from SnatchListItem s where s.goodsTimes.id in (:goodsTimesIds)")
	void deleteByGoodsTimes(@Param("goodsTimesIds")List<Integer> goodsTimesIds);

	@Query("select count(s.id) from SnatchListItem s where s.user.id=:userId and s.state=:state")
	Integer getUserListCount(@Param("userId")Integer userId,@Param("state")Byte state);

	@Query("select new com.zx.stlife.vo.snatch.SnatchListVo(t.id, g.id, g.goodsName, t.buyTimes, g.state) " +
			"from SnatchListItem t left join t.goodsTimes g where " +
			"t.user.id=:userId and g.id in (:goodsTimesIds) and t.state=:state")
	List<SnatchListVo> findSnatchListVoByUserAndGoodsTimes(@Param("userId")Integer userId,
						@Param("goodsTimesIds")List<Integer> goodsTimesIds, @Param("state") Byte state);
}
