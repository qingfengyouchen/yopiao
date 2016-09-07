package com.zx.stlife.tools.quartz.entity;

import com.zx.stlife.tools.quartz.QuartzSupportEntity;

/**
 * Created by micheal on 15/9/15.
 */
public class QuartzEntity extends QuartzSupportEntity {

    private String cronExpression;

    public QuartzEntity(String id, String cronExpression) {
        super(id);
        this.cronExpression = cronExpression;
    }

    @Override
    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }
}
