/*******************************************************************************
 * Copyright (c) 2005, 2014
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.zx.stlife.data;

import com.zx.stlife.entity.sys.Role;
import com.zx.stlife.entity.sys.User;
import com.base.modules.test.data.RandomData;

/**
 * 用户测试数据生成.
 * 
 * @author micheal cao
 */
public class UserData {

	public static User randomUser() {
		String userName = RandomData.randomName("User");

		User user = new User();
		user.setUserName(userName);
		user.setTrueName(userName);
		user.setPlainPassword("123456");

		return user;
	}

	public static User randomUserWithAdminRole() {
		User user = UserData.randomUser();
		Role adminRole = UserData.adminRole();
		user.getRoleList().add(adminRole);
		return user;
	}

	public static Role adminRole() {
		Role role = new Role();
		role.setName("Admin");

		return role;
	}
}
