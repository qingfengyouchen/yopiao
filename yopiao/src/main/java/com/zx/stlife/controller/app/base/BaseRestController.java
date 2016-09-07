package com.zx.stlife.controller.app.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public class BaseRestController {

	public Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 构建200成功状态信息
	 * 
	 * @return
	 */
	public JsonResult buildSuccessResult() {
		return JsonResultUtils.buildSuccessResult();
	}

	/**
	 * 构建成功状态信息
	 * 
	 * @param data
	 *            数据
	 * @return
	 */
	public JsonResult buildSuccessResult(Object data) {
		return JsonResultUtils.buildSuccessResult(data);
	}

	/***
	 * 构建300系统错误信息
	 * 
	 * @return
	 */
	public JsonResult buildFailureResult() {
		return JsonResultUtils.buildFailureResult();
	}

	/**
	 * 构建错误信息
	 * 
	 * @param state
	 *            错误状态码
	 * @return
	 */
	public JsonResult buildFailureResult(int state) {
		return JsonResultUtils.buildFailureResult(state);
	}

	/**
	 * 构建失败状态信息
	 * 
	 * @param data
	 *            数据
	 * @return
	 */
	public JsonResult buildFailureResult(Object data) {
		return JsonResultUtils.buildFailureResult(data);
	}

	/**
	 * 构建一个元素的map
	 * @param key
	 * @param value
	 * @return
	 */
	public Map<String, Object> buildOneElementMap(String key, Object value){
		return JsonResultUtils.buildOneElementMap(key, value);
	}
}
