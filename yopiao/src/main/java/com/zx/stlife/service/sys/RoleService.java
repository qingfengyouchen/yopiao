package com.zx.stlife.service.sys;

import com.zx.stlife.entity.sys.Module;
import com.google.common.collect.Maps;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.sys.Permission;
import com.zx.stlife.entity.sys.Role;
import com.zx.stlife.repository.jpa.sys.RoleDao;
import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.base.modules.util.SimpleUtils;
import org.javasimon.aop.Monitored;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理业务类.
 * 
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class RoleService {

	private static Logger logger = LoggerFactory.getLogger(RoleService.class);

	@Autowired
	private RoleDao roleDao;
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private PermissionService permissionService;

	private static String QL_FIND_ALL =
			"select new Role(id, name) from Role order by sortNum";

	private static final String SQL_FIND_ALL_AND_FLAG_SELECTED =
			"select r.id id,r.name name,(case when t.role_id is null then false else true end) hasSelected " +
					"from sys_role r left join ( " +
					"select role_id from sys_user_role where user_id=?1 " +
					") t on r.id=t.role_id " +
					"order by r.sort_num";

	public Role get(Integer id){
		return roleDao.findOne(id);
	}

	public void save(Role role, List<Integer> newPermissionIds ) {
		role.getPermissionList().clear();
		for (Integer id : newPermissionIds) {
			Permission permission = new Permission(id);
			role.getPermissionList().add(permission);
		}

		role = roleDao.save(role);
	}

	@Transactional(readOnly = true)
	public void search(Page<Role> page, Map<String, String> params) {
		Query query = new Query();
		query.table("select r from Role r")
				.like("r.name", params.get("name"))
				.orderBy("r.sortNum asc");

		roleDao.queryPage(page, query.getQLString(), query.getValues());

	}

	public void delete(List<Integer> ids){
		if(ids.contains(Const.ADMIN_ROLE_ID)){
			throw new RuntimeException("不能删除系统管理员角色");
		}
		Map<String, Object> values  = Maps.newHashMap();
		values.put("ids", ids);
		//String sql1 = "delete from sys_user_role where role_id=:ids";
		//String sql2 = "delete from sys_role_permission where role_id=:ids";
		String sql = "delete from sys_role where id=:ids";
		/*roleDao.executeSQLUpdate(sql1, values);
		roleDao.executeSQLUpdate(sql1, values);*/
		roleDao.executeSQLUpdate(sql, values);
	}

	public boolean isExistsName(String name){
		String ql = "select count(id) from Role where name=?";
		Long amount = (Long)roleDao.getObject(ql, name);
		return amount > 0;
	}

	@Transactional(readOnly = true)
	public void fillFormData(Role entity, Model model){
		model.addAttribute("entity", entity);
		boolean hasId = entity.hasId();
		Map<String, List<Permission>> map = new LinkedHashMap<>();
		List<Module> moduleList = moduleService.findAll();
		if(SimpleUtils.isNullList(moduleList))
			return;

		List<Integer> selectedPermissionIds = null;
		if(hasId){
			selectedPermissionIds = permissionService.findPermissionIdsByRole(entity.getId());
		}
		for(Module module: moduleList){
			List<Permission> permissions = permissionService.findByModule(module.getId());
			if(SimpleUtils.isNullList(permissions))
				continue;

			if(hasId) {
				for (Permission permission : permissions) {
					permission.setHasSelected(
							selectedPermissionIds.contains(permission.getId()));
				}
			}

			map.put(module.getName(), permissions);
		}

		model.addAttribute("modulePermissionMap", map);
	}

	public List<Role> findAll(){
		return roleDao.find(QL_FIND_ALL);
	}

	public List<Role> findAllAndFlagSelected(Integer userId){
		String sql = "select r.id id,r.name name,(case when t.role_id is null then '0' else '1' end) hasSelected " +
				"from sys_role r left join ( " +
				"select role_id from sys_user_role where user_id=?1 " +
				") t on r.id=t.role_id " +
				"order by r.sort_num";
		return roleDao.querySQL(Role.class, sql, userId);
	}

}
