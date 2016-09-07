package com.zx.stlife.repository.jpa.goods;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.goods.GoodsInfo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoodsInfoDao extends MyJpaRepository<GoodsInfo, Integer>{

    @Modifying
    @Query("update GoodsInfo set state=:state, editor=:editor, editTime=now() where id in (:ids)")
    int deleteByIds(@Param("ids")List<Integer> ids, @Param("editor") String editor, @Param("state") Byte state);

	@Modifying
	@Query("delete from GoodsInfo where id in (:ids)")
	void delGoodInfo(@Param("ids")List<Integer> ids);
	
	@Modifying
	@Query("delete from GoodsTimes t where t.goodsInfo.id in (:ids)")
	void delGoodTimes(@Param("ids")List<Integer> ids);

    @Modifying
    @Query("update GoodsInfo set editor=?2, editTime=now(), state=?3 where id=?1")
    void updateState(Integer id, String editor, Byte state);

    @Query(nativeQuery = true,
            value="select g.* from goods_info g " +
            "where g.id not in ( " +
            "select good_id from goods_times where state in (:goodsTimesStates) " +
            ") and state=:goodsState")
    List<GoodsInfo> findByNotSales(@Param("goodsState") Byte goodsState,
                                   @Param("goodsTimesStates") List<Byte> goodsTimesStates);

    @Query("select t.goodsInfo.id from GoodsTimes t where t.id=?1")
    Integer getIdByGoodsTimes(Integer goodsTimesId);
}
