package com.zx.stlife.service.sys;

import com.google.common.collect.Maps;
import com.zx.stlife.entity.sys.Permission;
import com.base.jpa.query.Page;
import com.base.modules.test.spring.SpringTransactionalTestCase;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.Map;

/**
 * Created by micheal on 15/7/1.
 */

@DirtiesContext
@ContextConfiguration(locations = {"/spring/applicationContext.xml"})
// 如果存在多个transactionManager，可以需显式指定
@TransactionConfiguration(transactionManager = "transactionManager")
public class PermissionServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private PermissionService permissionService;

    @Test
    public void test(){
        Page<Permission> page = new Page<>();
        Map<String, String> params = Maps.newHashMap();
        params.put("name", "查看用户");
        permissionService.search(page, params);

        Assert.assertEquals(page.getResult().size(), 1);
        logger.info(ToStringBuilder.reflectionToString(page.getResult().get(0)));
        Assert.assertEquals(page.getResult().get(0).getName(), "查看用户");
    }
}
