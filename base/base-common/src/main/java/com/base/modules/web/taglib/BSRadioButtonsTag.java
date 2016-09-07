/*******************************************************************************
 * Copyright (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.base.modules.web.taglib;

public class BSRadioButtonsTag extends BSAbstractMultiCheckedElementTag {
	private static final long serialVersionUID = 6257615872362092808L;

	@Override
	protected String getInputType() {
		return "radio";
	}
}
