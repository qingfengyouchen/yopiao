package com.zx.stlife.entity;

import com.base.jpa.model.SuperIntEntity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * 乐观锁基类
 */
@MappedSuperclass
public class SuperIntVersion extends SuperIntEntity {

    public Integer version;

    /**
     * Default constructor
     */
    public SuperIntVersion() {
    }

    public SuperIntVersion(Integer id) {
        super(id);
    }

    public SuperIntVersion(Byte state) {
        super(state);
    }

    public SuperIntVersion(Integer id, Byte state) {
        super(id, state);
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}