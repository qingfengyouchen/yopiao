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
import com.zx.stlife.entity.service.DelegateDrive;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.service.DelegateDriveDao;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.tools.FreeMarkerUtils;

/**
 * 顺风代驾  service
 * @author lxw
 *
 */
@Service
@Transactional(readOnly = true)
public class DelegateDriveService {

	private static final String HQL_UPDATE_DELEGATEDRIVE_STATE="update DelegateDrive d set d.state=?1 where d.id=?2"; 
	
	@Autowired
	private DelegateDriveDao delegateDriveDao;

	@Autowired
	private ConfigService configService;
	
	public void search(Page<DelegateDrive> page, Map<String, String> params){
		Query query = new Query();
		query.table("select d from DelegateDrive d").eq("d.state", Const.CommonState.ENABLE).like("d.title", params.get("title"));
		query.orderBy("d.id desc");
		delegateDriveDao.queryPage(page,query.getQLString(),query.getValues());
	} 
	
	@Transactional
	public void save(DelegateDrive entity) {
		User user = UserUtils.getCurrentUser();
		if(entity.getCreator()==null ||entity.getCreator()==""){
			entity.setCreator(user.getName());
		}
		entity.setEditor(user.getName());
		entity.setState(Const.CommonState.ENABLE);
		entity.setEditTime(DateUtilsEx.getNow());
		entity.setContent(Encodes.unescapeHtml(entity.getContent()));
		entity = delegateDriveDao.save(entity);
		
		String htmlUrl = entity.getHtmlUrl();
		if(StringUtils.isBlank(htmlUrl)){
			htmlUrl = configService.getStaticHtmlNo()+".html";
			entity.setHtmlUrl(htmlUrl);
		}
		String htmlPath = StringUtils.substringBefore(htmlUrl, "?");
		entity.setHtmlUrl(htmlPath + "?" + DateUtilsEx.getCurrentMMDDHHMMSS());
		entity=delegateDriveDao.save(entity);

		Map<String, Object> model = Maps.newHashMap();
		model.put("ctx", ROOT_URI);
		model.put("delegateDrive", entity);
		FreeMarkerUtils.createHtml("delegate-drive-details.ftl", model, Const.DelegateDrive_HTML_ROOT_PATH, htmlPath);
	}
	
	public DelegateDrive getByNotice(Integer delegateDriveId) {
		return delegateDriveDao.getOne(delegateDriveId);
	}
	
	@Transactional
	public void delete(List<Integer> ids){
		for (Integer id : ids) {
			delegateDriveDao.executeUpdate(HQL_UPDATE_DELEGATEDRIVE_STATE, Const.CommonState.DELETED,id);
		}
	} 
	
	public List<DelegateDrive> selListDelegateDrive(){
		List<DelegateDrive> delegateDrives=delegateDriveDao.selListDelegateDrive(Const.CommonState.ENABLE);
		return delegateDrives;
	}
}
