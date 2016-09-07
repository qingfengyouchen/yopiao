package com.zx.stlife.repository.jpa.goods;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.goods.GoodsCategory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoodsCategoryDao extends
        MyJpaRepository<GoodsCategory, Integer>{

    @Modifying
    @Query("update GoodsCategory set state=:state where id in (:ids)")
    int deleteByIds(@Param("ids")List<Integer> ids, @Param("state") Byte state);
}
