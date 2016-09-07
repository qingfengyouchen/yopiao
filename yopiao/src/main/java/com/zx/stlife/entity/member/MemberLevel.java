package com.zx.stlife.entity.member;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.constant.Const;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

/**
 * 会员等级
 */
@Entity
@Table(name = "sys_member_level")
// 默认的缓存策略.
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MemberLevel extends SuperIntEntity {

    /**
     * Default constructor
     */
    public MemberLevel() {
        super(Const.CommonState.ENABLE);
    }

    public MemberLevel(Integer id) {
        super(id, Const.CommonState.ENABLE);
    }

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Integer minValue;

    /**
     * 
     */
    private Integer maxValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMinValue() {
        return minValue;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public Integer getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }
}