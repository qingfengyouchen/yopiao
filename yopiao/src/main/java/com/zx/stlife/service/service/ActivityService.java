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
import com.zx.stlife.entity.TmpFile;
import com.zx.stlife.entity.service.Activity;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.service.ActivityDao;
import com.zx.stlife.service.TmpFileService;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.tools.FreeMarkerUtils;

/**
 * 天台活动 service
 * @author lxw
 *
 */
@Service
@Transactional(readOnly = true)
public class ActivityService {
	
	private static final String HQL_UPDATE_ACTIVITY_STATE="update Activity a set a.state=?1 where a.id=?2"; 

	private static final String HQL_UPDATE_ACTIVITY_HAS_JOIN_AMOUNT="update Activity a set a.hasJoinAmount=?1 where a.id=?2";
	
	@Autowired
	private ActivityDao activityDao;

	@Autowired
	private TmpFileService tmpFileService;
	
	@Autowired
	private ConfigService configService;
	
	public void search(Page<Activity> page, Map<String, String> params){
		Query query = new Query();
		if(params.get("type")!=null&&params.get("type")!="")
		{
			int type= Integer.parseInt(params.get("type"));
			query.table("select a from Activity a").eq("a.type",type).ne("a.state", Const.ActivityState.DELETED).like("a.title", params.get("title"));
			query.orderBy("a.id desc");
		}else
		{
			query.table("select a from Activity a").ne("a.state", Const.ActivityState.DELETED).like("a.title", params.get("title"));
			query.orderBy("a.id desc");
		}
		activityDao.queryPage(page,query.getQLString(),query.getValues());
	} 
	
	@Transactional
	public void save(Activity entity,Integer thumbImgUrlId,Integer imgUrlId) {
		boolean isModify = entity.hasId();
		User user = UserUtils.getCurrentUser();
		if( !isModify ) {
			entity.setCreator(user.getName());
			entity.setHasJoinAmount(0);
			entity = activityDao.save(entity);
		}
		entity.setEditor(user.getName());
		entity.setState(Const.ActivityState.ENABLE);
		entity.setEditTime(DateUtilsEx.getNow());
		entity.setContent(Encodes.unescapeHtml(entity.getContent()));
		String idStr = String.valueOf(entity.getId());
		if( thumbImgUrlId != null ){
			if(-1 == thumbImgUrlId ){
				entity.setThumbImgUrl(null);
			}else {
				TmpFile tmpFile = tmpFileService.get(thumbImgUrlId);
				String imgPath = tmpFileService.dowithImg(Const.ACTIVITY_IMG_ROOT_PATH, idStr, tmpFile.getUrl());
				entity.setThumbImgUrl(imgPath);
				tmpFileService.deleteById(thumbImgUrlId, false);
			}
		}
		if( imgUrlId != null ){
			if(-1 == imgUrlId ){
				entity.setImgUrl(null);
			}else {
				TmpFile tmpFile = tmpFileService.get(imgUrlId);
				String imgPath = tmpFileService.dowithImg(Const.ACTIVITY_IMG_ROOT_PATH, idStr, tmpFile.getUrl());
				entity.setImgUrl(imgPath);
				tmpFileService.deleteById(imgUrlId, false);
			}
		}
		
		entity = activityDao.save(entity);
		
		String htmlUrl = entity.getHtmlUrl();
		if(StringUtils.isBlank(htmlUrl)){
			htmlUrl = configService.getStaticHtmlNo()+".html";
			entity.setHtmlUrl(htmlUrl);
		}
		String htmlPath = StringUtils.substringBefore(htmlUrl, "?");
		entity.setHtmlUrl(htmlPath + "?" + DateUtilsEx.getCurrentMMDDHHMMSS());
		entity=activityDao.save(entity);

		Map<String, Object> model = Maps.newHashMap();
		model.put("ctx", ROOT_URI);
		model.put("activity", entity);
		model.put("activityRootUri", Const.ACTIVITY_IMG_ROOT_URL);
		if(entity.getType().equals(1))
		{
			FreeMarkerUtils.createHtml("activity-details.ftl", model, Const.ACTIVITY_HTML_ROOT_PATH, htmlPath);
		}else
		{
			FreeMarkerUtils.createHtml("bulletin-details.ftl", model, Const.ACTIVITY_HTML_ROOT_PATH, htmlPath);
		}
	}
	
	public Activity getByNotice(Integer activityId) {
		return activityDao.getOne(activityId);
	}
	
	@Transactional
	public void delete(List<Integer> ids){
		for (Integer id : ids) {
			activityDao.executeUpdate(HQL_UPDATE_ACTIVITY_STATE, Const.ActivityState.DELETED,id);
		}
	} 
	
	public List<Activity> selListActivity(){
		List<Activity> activities=activityDao.selListActivity(Const.ActivityState.ENABLE);
		return activities;
	}
	
	@Transactional
	public void updateState(Integer id){
		activityDao.executeUpdate(HQL_UPDATE_ACTIVITY_STATE, Const.ActivityState.END,id);
	}
	
	@Transactional
	public void updateHasJoinAmount(Integer id,Integer hasJoinAmount){
		activityDao.executeUpdate(HQL_UPDATE_ACTIVITY_HAS_JOIN_AMOUNT, hasJoinAmount,id);
	}
	
	/**
	 * 删除缩略图
	 * @param id
	 */
	@Transactional
	public void delThumbImgUrl(Integer id){
		activityDao.updateThumbImgUrl(id);
	}
	
	/**
	 * 删除图片
	 * @param id
	 */
	@Transactional
	public void delImgUrl(Integer id){
		activityDao.updateImgUrl(id);
	}
}
