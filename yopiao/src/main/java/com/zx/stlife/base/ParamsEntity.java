package com.zx.stlife.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询功能封装参数
 * @author micheal
 *
 */
public class ParamsEntity {
	private Map<String, String> params;

	public String get(String key) {
		return params == null ? null : params.get(key);
	}

	public Map<String, String> getParams() {
		initParams();
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
	public ParamsEntity instanceParams(){
		initParams();
		return this ;
	}
	
	public ParamsEntity putParam(String key ,String value){
		initParams();
		params.put(key, value);
		return this ;
	}
	
	private void initParams(){
		if(null == params){
			params =  new HashMap<String, String>();
		}
	}
}
