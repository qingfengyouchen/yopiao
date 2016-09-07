/*******************************************************************************
 * Copyright (c) 2005, 2014
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.zx.stlife.jpa;

import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.sys.UserDao;
import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.base.modules.test.spring.SpringTransactionalTestCase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(locations = {"/spring/applicationContext.xml"})
public class UserDaoTest extends SpringTransactionalTestCase {

	@Autowired
	private UserDao userDao;

	@Test
	public void test() {
		Page<User> page = new Page<>();
		Query query = new Query()
				.table("User");

		userDao.queryPage(page, query.getQLString(), query.getValues());

		Assert.assertEquals(page.getResult().size(), 2);
	}
}
