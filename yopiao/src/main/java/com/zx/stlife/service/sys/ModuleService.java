package com.zx.stlife.service.sys;

import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.sys.Module;
import com.zx.stlife.repository.jpa.sys.ModuleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户管理业务类.
 * 
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class ModuleService {

	private static Logger logger = LoggerFactory.getLogger(ModuleService.class);

	private static String QL_COUNT_BY_NAME =
			"select count(id) from Module where name=?1";

	private static String QL_FIND_ALL =
			"select new Module(id, name) from Module order by sortNum";

	@Autowired
	private ModuleDao moduleDao;

	public Module get(Integer id){
		return moduleDao.findOne(id);
	}

	public List<Module> findAll(){
		return moduleDao.find(QL_FIND_ALL);
	}

	public boolean isExistsName(String name){
		Long amount = (Long)moduleDao.getObject(QL_COUNT_BY_NAME, name);
		return amount > 0;
	}

	@Transactional
	public void save(Module entity) {
		entity = moduleDao.save(entity);
	}

	public void search(Page<Module> page, String name) {
		Query query = new Query();
		query.table("select t from Module t")
				.like("t.name", name)
				.eq("t.state", Const.CommonState.ENABLE)
				.orderBy("t.sortNum asc");

		moduleDao.queryPage(page, query.getQLString(), query.getValues());
	}

	@Transactional
	public void delete(List<Integer> ids){
		moduleDao.deleteByIds(ids);
	}

}
