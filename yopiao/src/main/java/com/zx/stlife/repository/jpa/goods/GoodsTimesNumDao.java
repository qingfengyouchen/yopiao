package com.zx.stlife.repository.jpa.goods;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.goods.GoodsTimesNum;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoodsTimesNumDao extends MyJpaRepository<GoodsTimesNum, Integer>{

    @Query("select t from GoodsTimesNum t where t.goodsTimes.id=?1 and num=?2")
    GoodsTimesNum getByGoodsTimesAndNum(Integer goodsTimesId, Integer num);

    @Query("select t.num from GoodsTimesNum t where t.goodsTimes.id=?1")
    List<Integer> findNumByGoodsTimes(Integer goodsTimesId);
}
