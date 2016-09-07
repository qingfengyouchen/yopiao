package com.zx.stlife.repository.jpa.sys;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.sys.Config;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConfigDao extends MyJpaRepository<Config, String> {
    @Modifying
    @Query("update Config set value=:value where key=:key")
    int update(@Param("key")String key, @Param("value")String value);

    @Modifying
    @Query("delete from Config where key like ?1%")
    void deleteByLikeKey(String likeKey);

}
