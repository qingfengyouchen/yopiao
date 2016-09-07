package com.zx.stlife.repository.jpa.order;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.order.WinngGoodsState;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WinngGoodsStateDao extends MyJpaRepository<WinngGoodsState, Integer>{

    @Query("select t from WinngGoodsState t where t.goodsTimes.id=?1 order by t.state")
    List<WinngGoodsState> findByGoodsTimes(Integer goodsTimesId);

    @Query("select t from WinngGoodsState t where t.goodsTimes.id=?1 and t.isCurrState=true")
    WinngGoodsState getCurrent(Integer goodsTimesId);

    @Query("select t from WinngGoodsState t where t.goodsTimes.id=?1 and t.state=?2")
    WinngGoodsState getByGoodsTimesAndState(Integer goodsTimesId, Byte state);

    @Query("select count(t.id) from WinngGoodsState t where t.goodsTimes.id=?1")
    Integer countByGoodsTimes(Integer goodsTimesId);
}
