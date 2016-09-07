package com.zx.stlife.repository.jpa.sys;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.sys.Permission;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PermissionDao extends MyJpaRepository<Permission, Integer> {

    @Modifying
    @Query("delete from Permission where id in (:ids)")
    public int deleteByIds(@Param("ids") List<Integer> ids);
}
