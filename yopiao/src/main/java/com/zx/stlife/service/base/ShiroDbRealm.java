package com.zx.stlife.service.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import com.google.code.kaptcha.Constants;
import com.zx.stlife.base.UserUtils;
import com.zx.stlife.entity.sys.Role;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.captcha.CaptchaUsernamePasswordToken;
import com.zx.stlife.service.captcha.IncorrectCaptchaException;
import com.zx.stlife.service.sys.AccountService;
import com.base.modules.util.Collections3;
import com.base.modules.util.SimpleUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import com.base.modules.util.Encodes;
import com.google.common.base.Objects;
import org.springframework.beans.factory.annotation.Autowired;

import static com.zx.stlife.constant.Const.UserState;

public class ShiroDbRealm extends AuthorizingRealm {

	protected AccountService accountService;

	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		CaptchaUsernamePasswordToken token = (CaptchaUsernamePasswordToken) authcToken;
		// 验证码 验证
		String captcha = null;
		Object obj_captcha = SecurityUtils.getSubject().getSession()
				.getAttribute(Constants.KAPTCHA_SESSION_KEY);
		if (obj_captcha instanceof String)
			captcha = (String) obj_captcha;

		if (captcha != null && !captcha.equalsIgnoreCase(token.getCaptcha()))
			throw new IncorrectCaptchaException("验证码错误！");

		// UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		User user = null;
		String userName = token.getUsername();
		if(SimpleUtils.isMobileNo(userName)){
			user = accountService.findUserByMobileNO(userName);
		}
		if(user ==  null) {
			user = accountService.findUserByUserName(userName);
		}else{
			token.setUsername(user.getUserName());
		}
		if (user != null) {
			if (user.getState() == UserState.DISENABLED
					|| user.getState() ==  UserState.DELETED) {
				throw new DisabledAccountException();
			}

			UserUtils.putCache(UserUtils.CACHE_USER, user);

			byte[] salt = Encodes.decodeHex(user.getSalt());
			String name = getName();
			return new SimpleAuthenticationInfo(new ShiroUser(
					user.getId(), user.getUserName(), user.getTrueName()), user.getPassword(),
					ByteSource.Util.bytes(salt), name);
		} else {
			return null;
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		User user = accountService.findUserByUserName(shiroUser.userName);

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		for (Role role : user.getRoleList()) {
			// 基于Role的权限信息
			info.addRole(role.getName());
			// 基于Permission的权限信息
			List<String> list = Collections3.extractToList(
					role.getPermissionList(), "code");
			info.addStringPermissions(list);
		}
		return info;
	}

	/**
	 * 设定Password校验的Hash算法与迭代次数.
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(
				AccountService.HASH_ALGORITHM);
		matcher.setHashIterations(AccountService.HASH_INTERATIONS);

		setCredentialsMatcher(matcher);
	}

	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	public static class ShiroUser implements Serializable {
		private static final long serialVersionUID = -1373760761780840081L;
		public Integer id;
		public String userName;
		public String name;
		private Map<String, Object> cacheMap;

		public ShiroUser(Integer id, String userName, String name) {
			this.id = id;
			this.userName = userName;
			this.name = name;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public Map<String, Object> getCacheMap() {
			if (cacheMap==null){
				cacheMap = new HashMap<String, Object>();
			}
			return cacheMap;
		}

		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString() {
			return userName;
		}

		/**
		 * 重载hashCode,只计算loginName;
		 */
		@Override
		public int hashCode() {
			return Objects.hashCode(userName);
		}

		/**
		 * 重载equals,只计算loginName;
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ShiroUser other = (ShiroUser) obj;
			if (userName == null) {
				if (other.userName != null)
					return false;
			} else if (!userName.equals(other.userName))
				return false;
			return true;
		}
	}

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
}
