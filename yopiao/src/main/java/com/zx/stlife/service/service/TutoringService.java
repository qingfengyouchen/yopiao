package com.zx.stlife.service.service;

import static com.zx.stlife.constant.Const.ROOT_URI;

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
import com.base.modules.util.SimpleUtils;
import com.google.common.collect.Maps;
import com.zx.stlife.constant.Const;
import com.zx.stlife.constant.Const.CommonState;
import com.zx.stlife.entity.service.BusinessInfo;
import com.zx.stlife.entity.service.MicroBusiness;
import com.zx.stlife.entity.service.Tutoring;
import com.zx.stlife.repository.jpa.service.BusinessInfoDao;
import com.zx.stlife.repository.jpa.service.TutoringDao;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.tools.FreeMarkerUtils;

@Component
@Transactional(readOnly = true)
public class TutoringService {

	private static Logger logger = LoggerFactory
			.getLogger(TutoringService.class);
	@Autowired
	private TutoringDao tutoringDao;

	@Autowired
	private ConfigService configService;
	
	@Transactional
	public void save(Tutoring entity) {
		boolean isModify = entity.hasId();
		if (!isModify) {
			tutoringDao.save(entity);
		}
		creatHtml(entity);
		
	}
	
	@Transactional
	public void creatHtml(Tutoring entity) {
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
				"tutoring-details.ftl", model,
				Const.TUTORING_HTML_ROOT_PATH, htmlPath);
	}

	@Transactional(readOnly = true)
	public List<Tutoring> findByState() {
		return tutoringDao.findByState(CommonState.ENABLE);
	}

	@Transactional(readOnly = true)
	public Tutoring findOne(Integer id) {
		return tutoringDao.findOne(id);
	}

	@Transactional
	public int delete(List<Integer> ids) {
		return tutoringDao.deleteByIds(CommonState.DELETED,ids);
	}

	public void search(Page<Tutoring> page, Map<String, String> params) {
		Query query = new Query();
		query.table("select t from Tutoring t")
				.like("t.title", params.get("title"))
				.eq("t.state", CommonState.ENABLE);
		Byte type = SimpleUtils.stringToByte(params.get("type"));
		if(type != null){
			query.eq("t.type", type);
		}
		query.orderBy("t.createTime desc");
		tutoringDao.queryPage(page, query.getQLString(), query.getValues());
	}
	
	public void searchAll(Page<Tutoring> page,Byte type) {
		Query query = new Query();
		query.table("select t from Tutoring t")
				.eq("t.type", type)
				.eq("t.state", CommonState.ENABLE)
				.orderBy("t.createTime desc");
		tutoringDao.queryPage(page, query.getQLString(), query.getValues());
	}

}
