package com.zx.stlife.service.sys;

import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.zx.stlife.entity.sys.Permission;
import com.zx.stlife.repository.jpa.sys.PermissionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class PermissionService {

	@Autowired
	private PermissionDao permissionDao;

	private static final String QL_QUERY_BY_MODULE =
			"select new Permission(t.id, t.name) from Permission t where t.module.id=?1 order by t.sortNum";

	private static final String QL_HAS_ROLE_PERMISSION =
			"select count(p.id) from Permission p inner join p.roleList r " +
					"where r.id in (:roleIds) and p.code=:permission";

	private static final String QL_QUERY_IDS_BY_ROLE =
			"select t.id from Permission t left join t.roleList r where r.id=?1";

	public Permission get(Integer id){
		return permissionDao.findOne(id);
	}

	public boolean isExistsCode(String code){
		String ql = "select count(id) from Permission where code=?";
		Long amount = (Long)permissionDao.getObject(ql, code);
		return amount > 0;
	}

	@Transactional
	public void save(Permission permission) {
		permission =  permissionDao.save(permission);
	}

	public void search(Page<Permission> page, Map<String, String> params) {
				Query query = new Query();
		query.table("select p.id as id, p.name as name, p.code as code, " +
				" p.sort_num as sortNum,m.name as moduleName from " +
				" sys_permission p left join sys_module m on p.module_id=m.id")
				.like("p.code", params.get("code"))
				.like("p.name", params.get("name"))
				.eq("m.id", params.get("moduleId"))
				.orderBy("order by m.sort_num,p.sort_num");

		permissionDao.querySQLPage(page, Permission.class, query.getQLString(), query.getValues());
	}

	@Transactional
	public void delete(List<Integer> ids){
		permissionDao.deleteByIds(ids);
	}

	public List<Permission> findByModule(Integer moduleId){
		return permissionDao.find(QL_QUERY_BY_MODULE, moduleId);
	}

	public boolean hasRolePermission(List<Integer> roleIds, String permission){
		Map<String, Object> values = new HashMap<>(2);
		values.put("roleIds", roleIds);
		values.put("permission", permission);
		Long amount = (Long)permissionDao.getObject(QL_HAS_ROLE_PERMISSION, values);

		return amount != null && amount > 0;
	}

	public List<Integer> findPermissionIdsByRole(Integer roleId){
		return (List<Integer>)permissionDao.findInteger(QL_QUERY_IDS_BY_ROLE, roleId);
	}
}
