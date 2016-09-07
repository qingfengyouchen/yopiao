/*******************************************************************************
 * Copyright (c) 2005, 2014
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.zx.stlife.service;

import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.sys.UserDao;
import com.zx.stlife.service.base.ServiceException;
import com.zx.stlife.service.sys.AccountService;
import com.google.common.collect.Lists;
import com.base.modules.test.security.shiro.ShiroTestUtils;
import com.zx.stlife.service.base.ShiroDbRealm;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class AccountServiceTest {

	@InjectMocks
	private AccountService accountService;

	@Mock
	private UserDao mockUserDao;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		ShiroTestUtils.mockSubject(new ShiroDbRealm.ShiroUser(1, "foo", "Foo"));
	}

	@After
	public void tearDown() {
		ShiroTestUtils.clearSubject();
	}

	@Test
	public void saveUser() {
		User admin = new User();

		User user = new User();
		user.setPlainPassword("123");

		List<Integer> roleIds = Lists.newArrayList();
		// 正常保存用户.
		accountService.saveUser(user, roleIds);

		// 保存超级管理用户抛出异常.
		try {
			accountService.saveUser(admin, roleIds);
			failBecauseExceptionWasNotThrown(ServiceException.class);
		} catch (ServiceException e) {
			// expected exception
		}
		Mockito.verify(mockUserDao, Mockito.never()).delete(admin.getId());
	}
}
