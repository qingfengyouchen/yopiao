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
import com.google.common.collect.Maps;
import com.zx.stlife.constant.Const;
import com.zx.stlife.constant.Const.CommonState;
import com.zx.stlife.entity.service.BusinessInfo;
import com.zx.stlife.entity.service.Convenience;
import com.zx.stlife.repository.jpa.service.ConvenienceDao;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.tools.FreeMarkerUtils;

@Component
@Transactional(readOnly = true)
public class ConvenienceService {
	
	private static Logger logger = LoggerFactory 
			.getLogger(ConvenienceService.class);
	
	@Autowired
	private ConvenienceDao convenienceDao;
	@Autowired
	private ConfigService configService;
	
	@Transactional
	public void save(Convenience entity) {
		boolean isModify = entity.hasId();
		if (!isModify) {
			entity = convenienceDao.save(entity);
		}
		creatHtml(entity);
	}
	
	@Transactional
	public void creatHtml(Convenience entity) {
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
				"convenience-details.ftl", model,
				Const.CONVENIENCE_HTML_ROOT_PATH, htmlPath);
	}

	@Transactional(readOnly = true)
	public List<Convenience> findByState() {
		return convenienceDao.findByState(CommonState.ENABLE);
	}

	@Transactional(readOnly = true)
	public Convenience findOne(Integer id) {
		return convenienceDao.findOne(id);
	}

	@Transactional
	public int delete(List<Integer> ids) {
		return convenienceDao.deleteByIds(ids);
	}

	public void search(Page<Convenience> page, Map<String, String> params) {
		Query query = new Query();
		query.table("select t from Convenience t").like("t.title", params.get("title"))
				.eq("t.state", CommonState.ENABLE).orderBy("t.createTime desc");
		convenienceDao.queryPage(page, query.getQLString(), query.getValues());
	}

	public void searchAll(Page<Convenience> page) {
		Query query = new Query();
		query.table("select t from Convenience t")
				.eq("t.state", CommonState.ENABLE)
				.orderBy("t.createTime desc");
		convenienceDao.queryPage(page, query.getQLString(), query.getValues());
	}
}
