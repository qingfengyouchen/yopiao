package com.zx.stlife.repository.jpa.goods;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.goods.GoodsImage;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoodsImageDao extends
        MyJpaRepository<GoodsImage, Integer>{

    @Query("select t from GoodsImage t where t.goodsInfo.id=?1 and t.category=?2 and t.state=?3")
    List<GoodsImage> findByGoodsAndCategory(Integer goodsId, Byte imageCategory, Byte state);
}
