package com.zx.stlife.tools.memcached;

/**
 * 统一定义Memcached中存储的各种对象的Key前缀和超时时间.
 * 
 * @author micheal cao
 */
public enum MemcachedObjectType {
	CATEGORY("category:", 60 * 60 * 24 * 10),
	TOP_SWITCH_IMG("topSwitchImg:", 60 * 60 * 24 * 10), // 10天
	MENU("menu:", 60 * 60 * 24 * 10), // 10天
	GOODS_CATEGORY("goodsCategory:", 60 * 60 * 24 * 10),
	SMS_CODE("stlife_app_smsCode:", 600); // 100s

	/**前缀*/
	private String prefix;
	/**过期时间，单位是秒*/
	private int expiredTime;

	MemcachedObjectType(String prefix, int expiredTime) {
		this.prefix = prefix;
		this.expiredTime = expiredTime;
	}

	public String getPrefix() {
		return prefix;
	}

	public int getExpiredTime() {
		return expiredTime;
	}

}
