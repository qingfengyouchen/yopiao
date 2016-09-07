package com.zx.stlife.repository.jpa.goods;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.goods.ShareGoods;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShareGoodsDao extends MyJpaRepository<ShareGoods, Integer>{

	@Query("select s from ShareGoods s where s.user.id=?1")
	List<ShareGoods> findShareGoods(Integer userId);
	
	@Modifying
	@Query("update ShareGoods s set s.state=?1 where s.id in (?2)")
	int deleteShareGoods(Byte state ,List<Integer> ids);
	
	@Query("select s from ShareGoods s where s.goodsTimes.id=?1 and state!=?2")
	ShareGoods getShareGoodsByGoodsTimes(Integer goodsTimesid, Byte excludeState);

	@Query("select count(s.id) from ShareGoods s where s.goodsTimes.id=?1 and state!=?2")
	Integer countByGoodsTimes(Integer goodsTimesid, Byte excludeState);
	
	@Modifying
	@Query("update ShareGoods s set s.userHeadImg=?1 where s.user.id=?2")
	int updateUserHeadImg(String userHeadImg, Integer userId);
	
}
