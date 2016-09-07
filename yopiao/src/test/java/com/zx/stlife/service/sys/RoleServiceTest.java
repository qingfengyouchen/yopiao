package com.zx.stlife.service.sys;

import com.base.modules.test.spring.SpringTransactionalTestCase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Created by micheal on 15/7/1.
 */

@DirtiesContext
@ContextConfiguration(locations = {"/spring/applicationContext.xml"})
// 如果存在多个transactionManager，可以需显式指定
@TransactionConfiguration(transactionManager = "transactionManager")
public class RoleServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private RoleService roleService;

    @Test
    public void testIsExistsName(){
        Assert.assertTrue(roleService.isExistsName("系统管理员"));
    }
}
