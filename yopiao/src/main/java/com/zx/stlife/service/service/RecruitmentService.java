package com.zx.stlife.service.service;

import static com.zx.stlife.constant.Const.ROOT_URI;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.base.modules.util.DateUtilsEx;
import com.base.modules.util.Encodes;
import com.google.common.collect.Maps;
import com.zx.stlife.base.UserUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.service.Recruitment;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.service.RecruitmentDao;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.tools.FreeMarkerUtils;

/**
 * 招聘工作 service
 * @author lxw
 *
 */
@Service
@Transactional(readOnly = true)
public class RecruitmentService {

	private static final String HQL_UPDATE_RECRUITMENT_STATE="update Recruitment r set r.state=?1 where r.id=?2"; 
	
	@Autowired
	private RecruitmentDao recruitmentDao;

	@Autowired
	private ConfigService configService;
	/**
	 * 搜索查询
	 */
	public void search(Page<Recruitment> page, Map<String, String> params){
		Query query = new Query();
		query.table("select r from Recruitment r").eq("r.state", Const.CommonState.ENABLE).like("r.title", params.get("title"));
		query.orderBy("r.id desc");
		recruitmentDao.queryPage(page,query.getQLString(),query.getValues());
	} 
	
	@Transactional
	public void save(Recruitment entity) {
		User user = UserUtils.getCurrentUser();
		if(entity.getCreator()==null ||entity.getCreator()==""){
			entity.setCreator(user.getName());
		}
		entity.setEditor(user.getName());
		entity.setState(Const.CommonState.ENABLE);
		entity.setEditTime(DateUtilsEx.getNow());
		entity.setContent(Encodes.unescapeHtml(entity.getContent()));
		entity.setCompanyIntroduce(Encodes.unescapeHtml(entity.getCompanyIntroduce()));
		entity = recruitmentDao.save(entity);
		
		String htmlUrl = entity.getHtmlUrl();
		if(StringUtils.isBlank(htmlUrl)){
			htmlUrl = configService.getStaticHtmlNo()+".html";
			entity.setHtmlUrl(htmlUrl);
		}
		String htmlPath = StringUtils.substringBefore(htmlUrl, "?");
		entity.setHtmlUrl(htmlPath + "?" + DateUtilsEx.getCurrentMMDDHHMMSS());
		entity=recruitmentDao.save(entity);

		Map<String, Object> model = Maps.newHashMap();
		model.put("ctx", ROOT_URI);
		model.put("recruitment", entity);
		FreeMarkerUtils.createHtml("recruitment-details.ftl", model, Const.RECRUITMENT_HTML_ROOT_PATH, htmlPath);
	}
	
	public Recruitment getByNotice(Integer recruitmentId) {
		return recruitmentDao.getOne(recruitmentId);
	}
	
	@Transactional
	public void delete(List<Integer> ids){
		for (Integer id : ids) {
			recruitmentDao.executeUpdate(HQL_UPDATE_RECRUITMENT_STATE, Const.CommonState.DELETED,id);
		}
	} 
	
	public List<Recruitment> selListRecruitment(){
		List<Recruitment> recruitments=recruitmentDao.selListRecruitment(Const.CommonState.ENABLE);
		return recruitments;
	}
}
