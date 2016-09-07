package com.zx.stlife.tools.quartz;

import com.base.jpa.model.SuperStringEntity;

/**
 * Quartz动态设置的支持实体
 */
public abstract class QuartzSupportEntity extends SuperStringEntity {

	public QuartzSupportEntity(String id) {
		super(id);
	}

	public abstract String getCronExpression();
}
