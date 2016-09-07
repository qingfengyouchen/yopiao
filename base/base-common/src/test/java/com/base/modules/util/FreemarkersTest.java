/*******************************************************************************
 * Copyright (c) 2005, 2014
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.base.modules.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FreemarkersTest {
	private final String TEMPLATE = "hello ${userName}";
	private final String ERROR_TEMPLATE = "hello ${";

	@Test
	public void renderString() {
		Map<String, String> model = Maps.newHashMap();
		model.put("userName", "micheal cao");
		String result = FreeMarkers.renderString(TEMPLATE, model);
		assertThat(result).isEqualTo("hello micheal cao");
	}

	@Test
	public void renderString2() {
		Map<String, Object> model = Maps.newHashMap();

		List<String> friends = Lists.newArrayList("a", "b", "c");

		model.put("friends", friends);
		String result = FreeMarkers.renderString("hello ${friends[0]}", model);
		assertThat(result).isEqualTo("hello a");
	}

	@Test(expected = RuntimeException.class)
	public void renderStringWithErrorTemplate() {
		Map<String, String> model = Maps.newHashMap();
		model.put("userName", "micheal cao");
		FreeMarkers.renderString(ERROR_TEMPLATE, model);
	}

	@Test
	public void renderFile() throws IOException {
		Map<String, String> model = Maps.newHashMap();
		model.put("userName", "micheal cao");
		Configuration cfg = FreeMarkers.buildConfiguration("classpath:/");
		Template template = cfg.getTemplate("testTemplate.ftl");
		String result = FreeMarkers.renderTemplate(template, model);
		assertThat(result).isEqualTo("hello micheal cao");
	}
}
