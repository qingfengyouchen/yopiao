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
import com.zx.stlife.entity.service.FlowerArtist;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.service.FlowerArtistDao;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.tools.FreeMarkerUtils;

/**
 * 鲜花速递 service
 */
@Service
@Transactional(readOnly = true)
public class FlowerArtistService {

	private static final String HQL_UPDATE_FLOWERARTIST_STATE="update FlowerArtist f set f.state=?1 where f.id=?2"; 
	
	@Autowired
	private FlowerArtistDao flowerArtistDao;

	@Autowired
	private ConfigService configService;
	
	public void search(Page<FlowerArtist> page, Map<String, String> params){
		Query query = new Query();
		query.table("select f from FlowerArtist f").eq("f.state", Const.CommonState.ENABLE).like("f.title", params.get("title"));
		query.orderBy("f.id desc");
		flowerArtistDao.queryPage(page,query.getQLString(),query.getValues());
	} 
	
	@Transactional
	public void save(FlowerArtist entity) {
		User user = UserUtils.getCurrentUser();
		if(entity.getCreator()==null ||entity.getCreator()==""){
			entity.setCreator(user.getName());
		}
		entity.setEditor(user.getName());
		entity.setState(Const.CommonState.ENABLE);
		entity.setEditTime(DateUtilsEx.getNow());
		entity.setContent(Encodes.unescapeHtml(entity.getContent()));
		entity = flowerArtistDao.save(entity);
		
		String htmlUrl = entity.getHtmlUrl();
		if(StringUtils.isBlank(htmlUrl)){
			htmlUrl = configService.getStaticHtmlNo()+".html";
			entity.setHtmlUrl(htmlUrl);
		}
		String htmlPath = StringUtils.substringBefore(htmlUrl, "?");
		entity.setHtmlUrl(htmlPath + "?" + DateUtilsEx.getCurrentMMDDHHMMSS());
		entity=flowerArtistDao.save(entity);

		Map<String, Object> model = Maps.newHashMap();
		model.put("ctx", ROOT_URI);
		model.put("flowerArtist", entity);
		FreeMarkerUtils.createHtml("flower-artist-details.ftl", model, Const.FLOWERARTIST_HTML_ROOT_PATH, htmlPath);
		
	}
	
	public FlowerArtist getByNotice(Integer flowerArtist) {
		return flowerArtistDao.getOne(flowerArtist);
	}
	
	@Transactional
	public void delete(List<Integer> ids){
		for (Integer id : ids) {
			flowerArtistDao.executeUpdate(HQL_UPDATE_FLOWERARTIST_STATE, Const.CommonState.DELETED,id);
		}
	} 
	
	public List<FlowerArtist> selListFlowerArtist(){
		List<FlowerArtist>  flowerArtists=flowerArtistDao.selListFlowerArtist(Const.CommonState.ENABLE);
		return flowerArtists;
	}
}
