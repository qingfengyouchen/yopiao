package com.zx.stlife.service.sys;

import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.base.modules.persistence.Hibernates;
import com.base.modules.security.utils.Digests;
import com.base.modules.util.Encodes;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.base.UserUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.sys.Role;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.sys.UserDao;
import com.zx.stlife.service.base.ShiroDbRealm;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 用户管理业务类.
 * 
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class AccountService {
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;

	private static Logger logger = LoggerFactory.getLogger(AccountService.class);

	@Autowired
	private UserDao userDao;
	@Autowired
	private PermissionService permissionService;

	private static String QL_COUNT_BY_USER_NAME = "select count(id) from User where userName=?1";

	private static String QL_COUNT_BY_MOBILE_NO = "select count(id) from User where mobileNo=?1 and openId is null";

	private static String QL_GET_NAME_MOBILE_NO =
			"select new User(id, trueName, mobileNo) from User where id=?1";

	private static String QL_QUERY_RANDOM_VIRTUAL_USER =
			"select t from User t where t.isVirtual=true order by rand()";
	private String mobileNO;
	
	private static String QL_GET_MAX_ID = "select max(id) from User";

	@Transactional
	public void saveUser(User user, List<Integer> newRoleIds ) {
		//设置不能修改系统管理员的角色
		if (UserUtils.isAdmin()) {
			logger.warn("操作员{}尝试修改超级管理员用户", getCurrentUserName());
			/*throw new ServiceException("不能修改超级管理员用户");*/
		}else{
			// bind roleList
			if(SimpleUtils.isNotNullList(newRoleIds)) {
				if (user.hasId()) {
					user.getRoleList().clear();
				}
				for (Integer roleId : newRoleIds) {
					Role role = new Role(roleId);
					user.getRoleList().add(role);
				}
			}
		}

		// 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
		if (StringUtils.isNotBlank(user.getPlainPassword())) {
			entryptPassword(user);
		}

		save(user);
	}

	@Transactional
	public void save(User entity){
		entity = userDao.saveAndFlush(entity);
	}

	/**
	 * 按Id获得用户.
	 */
	public User getUser(Integer id) {
		return userDao.findOne(id);
	}

	/**
	 * 按Id获得用户.
	 */
	public User getUserForRefresh(Integer id) {
		User user = getUser(id);
		refresh(user);
		return user;
	}

	/**
	 * 获取全部用户，并在返回前对用户的延迟加载关联角色进行初始化.
	 */
	public List<User> getAllUserInitialized() {
		List<User> result = (List<User>) userDao.findAll();
		for (User user : result) {
			Hibernates.initLazyProperty(user.getRoleList());
		}
		return result;
	}

	/**
	 * 按登录名查询用户.
	 */
	public User findUserByUserName(String userName) {
		return userDao.findByUserName(userName);
	}

	public User findUserByMobileNO(String mobileNO) {
		this.mobileNO = mobileNO;
		return userDao.findByMobileNo(mobileNO);
	}

	public User findByMobileNoAndSource(String mobileNO, Byte source) {
		return userDao.findByMobileNoAndSource(mobileNO, source);
	}

	public User findUserByOpenId(String openId) {
		return userDao.findByOpenId(openId);
	}

	/**
	 * 按名称查询用户, 并在返回前对用户的延迟加载关联角色进行初始化.
	 */
	/*public User findUserByNameInitialized(String name) {
		User user = userDao.findByName(name);
		if (user != null) {
			Hibernates.initLazyProperty(user.getRoleList());
		}
		return user;
	}*/

	/**
	 * 按页面传来的查询条件查询用户.
	 */
	public void searchUser(Page<User> page, Map<String, String> params) {
		Query query = new Query();
		query.table("select u from User u")
				.like("u.userName", params.get("userName"))
				.like("u.trueName", params.get("trueName"))
				.like("u.mobileNo", params.get("mobileNo"))
				.ne("u.state", Const.UserState.DELETED)
				.orderBy("u.createTime asc");

		userDao.queryPage(page, query.getQLString(), query.getValues());
	}

	@Transactional
	public void deleteUser(List<Integer> ids){
		if(ids.contains(Const.ADMIN_ID)){
			throw new RuntimeException("不能删除系统管理员账号");
		}
		userDao.deleteByIds(Const.UserState.DELETED, getCurrentUserName(), ids);
	}

	/**
	 * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
	 */
	public void entryptPassword(User user) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		user.setSalt(Encodes.encodeHex(salt));

		byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(), salt, HASH_INTERATIONS);
		user.setPassword(Encodes.encodeHex(hashPassword));
	}
	
	public String encodePassword(String salt, String password){
		byte[] hashPassword = Digests.sha1(password.getBytes(), Encodes.decodeHex(salt), HASH_INTERATIONS);
		return Encodes.encodeHex(hashPassword);
	}

	/**
	 * 取出Shiro中的当前用户LoginName.
	 */
	private String getCurrentUserName() {
		ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.userName;
	}

	public boolean isExistsUserName(String userName, String oldUserName){
		if (StringUtils.equals(userName, oldUserName) ) {
			return false;
		}

		Long amount = (Long)userDao.getObject(QL_COUNT_BY_USER_NAME, userName);
		return amount > 0;
	}

	public boolean isExistsMobileNo(String mobileNo, String oldMobileNo){
		if (StringUtils.equals(oldMobileNo, mobileNo) ) {
			return false;
		}

		Long amount = (Long)userDao.getObject(QL_COUNT_BY_MOBILE_NO, mobileNo);
		return amount > 0;
	}

	public User getTrueNameMobileNo(String id){
		return (User)userDao.getObject(QL_GET_NAME_MOBILE_NO, id);
	}

	public void refresh(User user){
		userDao.refresh(user);
	}
	
	public Integer getMaxId(){
		return (Integer) userDao.getObject(QL_GET_MAX_ID);
	}

	/**
	 * 获取系统所有虚拟用户
	 * @return
	 */
	public List<User> findByVirtualUser(){
		return userDao.findByIsVirtual(true);
	}

	public List<User> findRandomVirtualUser(int amount){
		return userDao.findTop(amount, QL_QUERY_RANDOM_VIRTUAL_USER);
	}

	public User getByGoodsTimesAndNum(Integer goodsTimesId, Integer num){
		return userDao.getByGoodsTimesAndNum(goodsTimesId, num);
	}

	public String getMobileNo(Integer id){
		return userDao.getMobileNo(id);
	}

	public List<String> getMobileNo(List<Integer> userIds){
		return userDao.getMobileNo(userIds);
	}

	public User getRandom1VirtualUser(){
		List<User> userList = findRandomVirtualUser(1);
		return SimpleUtils.isNullList(userList) ? null : userList.get(0);
	}
}
