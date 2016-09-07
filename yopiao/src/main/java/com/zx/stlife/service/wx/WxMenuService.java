package com.zx.stlife.service.wx;

import com.base.jpa.query.Query;
import com.zx.stlife.entity.wx.WxMenu;
import com.zx.stlife.repository.jpa.wx.WxMenuDao;
import org.javasimon.aop.Monitored;
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
@Monitored
public class WxMenuService {

	private static Logger logger = LoggerFactory.getLogger(WxMenuService.class);

	@Autowired
	private WxMenuDao dao;

	public WxMenu get(Integer id){
		return dao.findOne(id);
	}

	public List<WxMenu> findAllSuper(){
		Query query = new Query();
		query.table("select t from WxMenu t")
				.isnull("t.father.id")
				.orderBy("t.sortNum asc");
		return dao.find(query.getQLString());
	}

	public boolean isExistsName(String name){
		String ql = "select count(id) from WxMenu where name=?";
		Long amount = (Long)dao.getObject(ql, name);
		return amount > 0;
	}

	@Transactional(readOnly = false)
	public void save(WxMenu entity) {
		if(entity.getFather() != null && !entity.getFather().hasId()){
			entity.setFather(null);
		}
		entity = dao.save(entity);
	}

	@Transactional(readOnly = false)
	public void delete(List<String> ids){
		dao.deleteByIds(ids);
	}

}
