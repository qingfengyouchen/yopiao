package com.zx.stlife.service.member;

import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.member.MemberLevel;
import com.zx.stlife.repository.jpa.member.MemberLevelDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class MemberLevelService {

	private static Logger logger = LoggerFactory.getLogger(MemberLevelService.class);

	private static String QL_COUNT_BY_NAME =
			"select count(id) from MemberLevel where name=?1 and state=?2";

	private static String QL_FIND_ALL =
			"select t from MemberLevel t where t.state=?1 order by t.minValue";

	@Autowired
	private MemberLevelDao dao;

	public MemberLevel get(Integer id){
		return dao.findOne(id);
	}

	public List<MemberLevel> findAll(){
		return dao.find(QL_FIND_ALL, Const.CommonState.ENABLE);
	}

	public boolean isExistsName(String name){
		Long amount = (Long)dao.getObject(QL_COUNT_BY_NAME, name, Const.CommonState.ENABLE);
		return amount > 0;
	}

	@Transactional
	public void save(MemberLevel entity) {
		entity = dao.save(entity);
	}

	public void search(Page<MemberLevel> page, String name) {
		Query query = new Query();
		query.table("select t from MemberLevel t")
				.like("t.name", name)
				.orderBy("t.minValue asc");

		dao.queryPage(page, query.getQLString(), query.getValues());
	}

	@Transactional
	public void delete(List<Integer> ids){
		dao.deleteByIds(Const.CommonState.DELETED, ids);
	}
	
	@Transactional(readOnly = true)
	public MemberLevel findMemberLevelByMinValue(Integer minValue){
		return dao.findMemberLevelByMinValue(Const.CommonState.ENABLE, minValue);
	}
	
}
