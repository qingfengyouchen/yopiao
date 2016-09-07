package com.zx.stlife.controller.app.base;

import java.io.Serializable;

/**
 * 封装返回JSON格式数据
 */
public class JsonResult implements Serializable {

	private static final long serialVersionUID = -4699713095477151086L;
	/**
	 * 状态
	 */
	private int state;
	/**
	 * 数据，可以是entity, map, list
	 */
	private Object data;

	public JsonResult() {
		super();
	}

	public JsonResult(Object data) {
		super();
		this.data = data;
	}

	public JsonResult(int state) {
		super();
		this.state = state;
	}

	public JsonResult(int state, Object data) {
		super();
		this.state = state;
		this.data = data;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
