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
import com.zx.stlife.entity.service.Job;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.service.JobDao;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.tools.FreeMarkerUtils;

/**
 * 
 * 求职工作 service
 * @author lxw
 *
 */
@Service
@Transactional(readOnly = true)
public class JobService {

	private static final String HQL_UPDATE_JOB_STATE="update Job j set j.state=?1 where j.id=?2"; 
	
	@Autowired
	private JobDao jobDao;
	
	@Autowired
	private ConfigService configService;
	
	/**
	 * 搜索查询
	 */
	public void search(Page<Job> page, Map<String, String> params){
		Query query = new Query();
		query.table("select j from Job j").eq("j.state", Const.CommonState.ENABLE).like("j.title", params.get("title"));
		query.orderBy("j.id desc");
		jobDao.queryPage(page,query.getQLString(),query.getValues());
	} 
	
	@Transactional
	public void save(Job entity) {
		User user = UserUtils.getCurrentUser();
		if(entity.getCreator()==null ||entity.getCreator()==""){
			entity.setCreator(user.getName());
		}
		entity.setLinker(entity.getName());
		entity.setEditor(user.getName());
		entity.setState(Const.CommonState.ENABLE);
		entity.setEditTime(DateUtilsEx.getNow());
		entity.setContent(Encodes.unescapeHtml(entity.getContent()));
		entity = jobDao.save(entity);
		
		String htmlUrl = entity.getHtmlUrl();
		if(StringUtils.isBlank(htmlUrl)){
			htmlUrl = configService.getStaticHtmlNo()+".html";
			entity.setHtmlUrl(htmlUrl);
		}
		String htmlPath = StringUtils.substringBefore(htmlUrl, "?");
		entity.setHtmlUrl(htmlPath + "?" + DateUtilsEx.getCurrentMMDDHHMMSS());
		entity=jobDao.save(entity);

		Map<String, Object> model = Maps.newHashMap();
		model.put("ctx", ROOT_URI);
		model.put("job", entity);
		model.put("gender",Const.Gender.genderMap.get(entity.getGender()));
		FreeMarkerUtils.createHtml("job-details.ftl", model, Const.JOB_HTML_ROOT_PATH, htmlPath);
	}
	
	public Job getByNotice(Integer jobId) {
		return jobDao.getOne(jobId);
	}
	
	@Transactional
	public void delete(List<Integer> ids){
		for (Integer id : ids) {
			jobDao.executeUpdate(HQL_UPDATE_JOB_STATE, Const.CommonState.DELETED,id);
		}
	} 
	
	public List<Job> selListJob(){
		List<Job> jobs=jobDao.selListJob(Const.CommonState.ENABLE);
		return jobs;
	}
}
