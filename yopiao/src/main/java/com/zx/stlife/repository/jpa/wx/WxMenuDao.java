package com.zx.stlife.repository.jpa.wx;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.wx.WxAccount;
import com.zx.stlife.entity.wx.WxMenu;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WxMenuDao extends MyJpaRepository<WxMenu, Integer> {
    @Modifying
    @Query("delete from WxMenu where id in (:ids)")
    public int deleteByIds(@Param("ids") List<String> ids);
}
