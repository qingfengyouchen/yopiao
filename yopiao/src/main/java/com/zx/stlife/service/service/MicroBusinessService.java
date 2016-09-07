package com.zx.stlife.service.service;

import static com.zx.stlife.constant.Const.ROOT_URI;

import java.awt.color.CMMException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.base.modules.util.DateUtilsEx;
import com.google.common.collect.Maps;
import com.zx.stlife.base.UserUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.constant.Const.CommonState;
import com.zx.stlife.entity.goods.GoodsInfo;
import com.zx.stlife.entity.service.BusinessInfo;
import com.zx.stlife.entity.service.FunFood;
import com.zx.stlife.entity.service.MicroBusiness;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.service.MicroBusinessDao;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.tools.FreeMarkerUtils;

//Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class MicroBusinessService {

	private static Logger logger = LoggerFactory.getLogger(MicroBusinessService.class);
	
	@Autowired
	private MicroBusinessDao microBusinessDao;
	@Autowired
	private ConfigService configService;
	@Transactional
	public void save(MicroBusiness entity){
		boolean isModify = entity.hasId();
		if (!isModify) {
			microBusinessDao.save(entity);
		}
		creatHtml(entity); 
	}
	
	@Transactional
	public void creatHtml(MicroBusiness entity) {
		String htmlUrl = entity.getHtmlUrl();
		if (StringUtils.isBlank(htmlUrl)) {
			htmlUrl = configService.getStaticHtmlNo() + ".html";
			entity.setHtmlUrl(htmlUrl);
		}
		String htmlPath = StringUtils.substringBefore(htmlUrl, "?");
		entity.setHtmlUrl(htmlPath + "?" + DateUtilsEx.getCurrentMMDDHHMMSS());

		Map<String, Object> model = Maps.newHashMap();
		model.put("entity", entity);
		model.put("ctx", ROOT_URI);
		FreeMarkerUtils.createHtml(
				"micro-business-details.ftl", model,
				Const.MICROBUSINESS_HTML_ROOT_PATH, htmlPath);
	}
	
	@Transactional(readOnly=true)
	public List<MicroBusiness> findByState(){
		return microBusinessDao.findByState(CommonState.ENABLE);
	}
	
	@Transactional(readOnly=true)
	public MicroBusiness findOne(Integer id){
		return microBusinessDao.findOne(id);
	}
	
	@Transactional
	public int delete(List<Integer> ids){
		return microBusinessDao.deleteByIds(CommonState.DELETED,ids);
	}
	
	public void search(Page<MicroBusiness> page,Map<String, String> params){
		Query query = new Query();
		query.table("select t from MicroBusiness t").like("t.name", params.get("name"))
			.eq("t.state", CommonState.ENABLE)
			.orderBy("t.createTime desc");
		microBusinessDao.queryPage(page, query.getQLString(), query.getValues());
	}
	
	public void searchAll(Page<MicroBusiness> page) {
		Query query = new Query();
		query.table("select t from MicroBusiness t")
				.eq("t.state", CommonState.ENABLE)
				.orderBy("t.createTime desc");
		microBusinessDao.queryPage(page, query.getQLString(), query.getValues());
	}
}
