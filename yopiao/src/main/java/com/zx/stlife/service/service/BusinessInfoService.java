package com.zx.stlife.service.service;

import static com.zx.stlife.constant.Const.GOODS_HTML_ROOT_PATH;
import static com.zx.stlife.constant.Const.GOODS_IMG_ROOT_URI;
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
import com.zx.stlife.repository.jpa.service.BusinessInfoDao;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.tools.FreeMarkerUtils;

@Component
@Transactional(readOnly = true)
public class BusinessInfoService {
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory
			.getLogger(BusinessInfoService.class);

	@Autowired
	private BusinessInfoDao businessInfoDao;
	@Autowired
	private ConfigService configService;

	@Transactional
	public void save(BusinessInfo entity) {

		boolean isModify = entity.hasId();
		if (!isModify) {
			entity = businessInfoDao.save(entity);
		}
		creatHtml(entity);
	}

	public void creatHtml(BusinessInfo entity) {
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
				"business-info-details.ftl", model,
				Const.BUSINESS_HTML_ROOT_PATH, htmlPath);
	}

	@Transactional(readOnly = true)
	public List<BusinessInfo> findByState() {
		return businessInfoDao.findByState(CommonState.ENABLE);
	}

	@Transactional(readOnly = true)
	public BusinessInfo findOne(Integer id) {
		return businessInfoDao.findOne(id);
	}

	@Transactional
	public int delete(List<Integer> ids) {
		return businessInfoDao.deleteByIds(CommonState.DELETED, ids);
	}

	public void search(Page<BusinessInfo> page, Map<String, String> params) {
		Query query = new Query();
		query.table("select t from BusinessInfo t")
				.like("t.title", params.get("title"))
				.eq("t.state", CommonState.ENABLE);
		Byte type = SimpleUtils.stringToByte(params.get("type"));
		if (type != null) {
			query.eq("t.type", type);
		}
		query.orderBy("t.createTime desc");
		businessInfoDao.queryPage(page, query.getQLString(), query.getValues());
	}

	public void searchAll(Page<BusinessInfo> page,Byte type) {
		Query query = new Query();
		query.table("select t from BusinessInfo t")
				.eq("t.type", type)
				.eq("t.state", CommonState.ENABLE)
				.orderBy("t.createTime desc");
		businessInfoDao.queryPage(page, query.getQLString(), query.getValues());
	}
}
