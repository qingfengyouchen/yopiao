package com.zx.stlife.base;

import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.sys.UserDao;
import com.google.common.collect.Maps;
import com.zx.stlife.service.base.ShiroDbRealm;
import com.zx.stlife.service.sys.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 用户工具类
 */
public class UserUtils {
	private static UserDao userDao;
	public static final String CACHE_USER = "user";

	public static User getCurrentUser(){
		User user = (User)getCache(CACHE_USER);
		if (user == null){
			Subject subject = SecurityUtils.getSubject();
			if( subject == null ){
				return null;
			}

			ShiroDbRealm.ShiroUser shiroUser = (ShiroDbRealm.ShiroUser)subject.getPrincipal();
			if (shiroUser!=null){
				if(userDao ==  null){
					userDao = SpringContextHolder.getBean(UserDao.class);
				}
				user = userDao.findOne(shiroUser.getId());
				putCache(CACHE_USER, user);
			}
		}
		if (user == null){
			user = UserWxUtils.getCurrUser();
		}
		if (user == null){
			user = new User();
			SecurityUtils.getSubject().logout();
		}
		return user;
	}

	public static Integer getCurrentUserId(){
		User user = getCurrentUser();
		return user != null && user.hasId() ? user.getId() : null;
	}
	
	public static User getCurrentUser(boolean isRefresh){
		if (isRefresh){
			removeCache(CACHE_USER);
		}
		return getCurrentUser();
	}

	public static boolean hasAuthenticated(){
		Subject subject = SecurityUtils.getSubject();
		if(subject == null)
			return false;

		return subject.isAuthenticated();
	}

	public static boolean hasRole(String roleName){
		return hasAuthenticated() && SecurityUtils.getSubject().hasRole(roleName);
	}

	public static boolean hasPermission(String permission){
		return hasAuthenticated() && SecurityUtils.getSubject().isPermitted(permission);
	}

	/**
	 * 是否系统管理员
	 * @return
	 */
	public static boolean isSysAdmin(){
		return hasRole(Const.ROLE_NAME_SYS_ADMIN);
	}

	/**
	 * 是否admin用户
	 * @return
	 */
	public static boolean isAdmin(){
		try {
			User user = getCurrentUser();
			return user != null && "admin".equals(user.getName());
		}catch (Exception ex){

		}

		return false;
	}

	// ============== User Cache ==============
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
		Object obj = getCacheMap().get(key);
		return obj==null?defaultValue:obj;
	}

	public static void putCache(String key, Object value) {
		getCacheMap().put(key, value);
	}

	public static void removeCache(String key) {
		getCacheMap().remove(key);
	}
	
	public static Map<String, Object> getCacheMap(){
		try{
			Subject subject = SecurityUtils.getSubject();
			ShiroDbRealm.ShiroUser shiroUser = (ShiroDbRealm.ShiroUser)subject.getPrincipal();
			if(shiroUser == null){
				return Maps.newConcurrentMap();
			}else{
				return shiroUser.getCacheMap();
			}
		}catch (UnavailableSecurityManagerException e) {
			return Maps.newConcurrentMap();
		}
	}

}
