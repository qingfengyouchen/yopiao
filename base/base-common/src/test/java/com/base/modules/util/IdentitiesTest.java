/*******************************************************************************
 * Copyright (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.base.modules.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class IdentitiesTest {

	@Test
	public void demo() {
		System.out.println("uuid: " + Identities.uuid());
		System.out.println("uuid2:" + Identities.uuid2());
		System.out.println("randomLong:  " + Identities.randomLong());
		System.out.println("randomBase62:" + Identities.randomBase62(7));
	}

	@Test
	public void demo2() {
		Map<String, String> map = new HashMap<>();
		int loopCount = 100000;
		for(int i = 0; i < loopCount; i ++) {
			String value = Identities.randomBase62(4);
			System.out.println(value);
			map.put(value, value);
		}

		Assert.assertEquals(loopCount, map.size());
	}
}
