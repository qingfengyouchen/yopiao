package com.zx.stlife.controller.app.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by micheal on 15/12/18.
 */
public class JsonResultUtils {
    /**
     * 构建200成功状态信息
     *
     * @return
     */
    public static JsonResult buildSuccessResult() {
        return new JsonResult(AppStatusCode.SC_SUCCESS, null);
    }

    /**
     * 构建成功状态信息
     *
     * @param data
     *            数据
     * @return
     */
    public static JsonResult buildSuccessResult(Object data) {
        return new JsonResult(AppStatusCode.SC_SUCCESS, data);
    }

    /***
     * 构建300系统错误信息
     *
     * @return
     */
    public static JsonResult buildFailureResult() {
        return buildFailureResult(AppStatusCode.SC_FAILURE);
    }

    /**
     * 构建错误信息
     *
     * @param state
     *            错误状态码
     * @return
     */
    public static JsonResult buildFailureResult(int state) {
        return new JsonResult(state, null);
    }

    /**
     * 构建失败状态信息
     *
     * @param data
     *            数据
     * @return
     */
    public static JsonResult buildFailureResult(Object data) {
        return new JsonResult(AppStatusCode.SC_FAILURE, data);
    }

    /**
     * 构建一个元素的map
     * @param key
     * @param value
     * @return
     */
    public static Map<String, Object> buildOneElementMap(final String key, final Object value){
        return new HashMap<String, Object>(){{
            put(key, value);
        }};
    }
}
