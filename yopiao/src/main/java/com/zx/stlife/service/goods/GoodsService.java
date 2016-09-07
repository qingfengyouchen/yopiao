package com.zx.stlife.service.goods;

import static com.zx.stlife.constant.Const.GOODS_HTML_ROOT_PATH;
import static com.zx.stlife.constant.Const.GOODS_IMG_ROOT_PATH;
import static com.zx.stlife.constant.Const.GOODS_IMG_ROOT_URI;
import static com.zx.stlife.constant.Const.ROOT_URI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import com.base.modules.util.FileUtilsEx;
import com.base.modules.util.SimpleUtils;
import com.google.common.collect.Maps;
import com.zx.stlife.base.SpringContextHolder;
import com.zx.stlife.base.UserUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.constant.Const.CommonState;
import com.zx.stlife.constant.Const.GoodsImageCategory;
import com.zx.stlife.constant.Const.GoodsState;
import com.zx.stlife.constant.Const.GoodsTimesState;
import com.zx.stlife.entity.TmpFile;
import com.zx.stlife.entity.goods.GoodsCategory;
import com.zx.stlife.entity.goods.GoodsImage;
import com.zx.stlife.entity.goods.GoodsInfo;
import com.zx.stlife.repository.jpa.goods.GoodsImageDao;
import com.zx.stlife.repository.jpa.goods.GoodsInfoDao;
import com.zx.stlife.service.TmpFileService;
import com.zx.stlife.service.order.SnatchRecordService;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.tools.FreeMarkerUtils;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class GoodsService {

	private static Logger logger = LoggerFactory.getLogger(GoodsService.class);

	private static String QL_COUNT_BY_NAME =
			"select count(id) from GoodsInfo where name=?1 and state=?2";

	private static String QL_FIND_ALL =
			"select t from GoodsInfo t where t.state=?1 order by t.id";

	private static final String QL_DELETE_THUMB_IMG =
			"update GoodsInfo set thumbnail = null where id=?1";

	private static final String QL_QUERY_HTML_URL_BY_IDS =
			"select detailsHtmlUrl from GoodsInfo where id in (:ids)";

	private static final String QL_DELETE_IMG =
			"delete from GoodsImage where id=?1";

	private static final String QL_DELETE_IMG_BY_GOODIS =
			"delete from GoodsImage t where t.goodsInfo.id=?1 and t.category=?2 and t.url like '%http%'";
	@Autowired
	private GoodsInfoDao dao;
	@Autowired
	private GoodsImageDao goodsImageDao;
	@Autowired
	private TmpFileService tmpFileService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private GoodsTimesService goodsTimesService;
	@Autowired
	private ShareGoodsService shareGoodsService;

	public GoodsInfo get(Integer id){
		return dao.findOne(id);
	}

	public List<GoodsInfo> findAll(){
		return dao.find(QL_FIND_ALL, Const.CommonState.ENABLE);
	}

	public boolean isExistsName(String name){
		Long amount = (Long)dao.getObject(QL_COUNT_BY_NAME, name, Const.CommonState.ENABLE);
		return amount > 0;
	}

	@Transactional
	public void save(GoodsInfo entity) {
		entity = dao.save(entity);
	}

	@Transactional
	public void save(GoodsInfo entity, Integer tmpThumbId, List<Integer> tmpTopIdList,
					 List<Integer> tmpDetailIdsList, List<String> topSwitchUrlList,
					 List<String> detailsUrlList, String oldName, Byte oldState) {
		boolean isModify = entity.hasId();
		if( !isModify ) {
			entity = dao.save(entity);
		}
		String idStr = String.valueOf(entity.getId());
		if( tmpThumbId != null ){
			if(-1 == tmpThumbId ){
				entity.setThumbnail(null);
			}else {
				TmpFile tmpFile = tmpFileService.get(tmpThumbId);
				String imgPath = tmpFileService.dowithImg(
						GOODS_IMG_ROOT_PATH, idStr, tmpFile.getUrl());
				entity.setThumbnail(imgPath);
				tmpFileService.deleteById(tmpThumbId, false);

				if(isModify){ // 同时更新期号中的缩略图
					goodsTimesService.updateThumbnail(entity.getId(), imgPath);
				}
			}
		}

		if(entity.getGoodsCategoryId() != null){
			entity.setGoodsCategory(
					new GoodsCategory(entity.getGoodsCategoryId()));
		}

		if(isModify && !StringUtils.equals(oldName, entity.getName())){
			goodsTimesService.updateGoodsName(entity.getId(), entity.getName());
			shareGoodsService.updateGoodsName(entity.getId(), entity.getName());
			SnatchRecordService snatchRecordService =
					(SnatchRecordService) SpringContextHolder.getBean(SnatchRecordService.class);
			snatchRecordService.updateGoodsName(entity.getId(), entity.getName());
		}

		entity = dao.save(entity);

		if(SimpleUtils.isNotNullList(tmpTopIdList)){
			for(Integer id : tmpTopIdList){
				TmpFile tmpFile = tmpFileService.get(id);

				String imgPath = tmpFileService.dowithImg(
						GOODS_IMG_ROOT_PATH, idStr, tmpFile.getUrl());
				GoodsImage goodsImage = new GoodsImage(
						entity, imgPath, GoodsImageCategory.TOP_SWITCH);
				saveImage(goodsImage);

				tmpFileService.deleteById(id, false);
			}
		}
		
		if(SimpleUtils.isNotNullList(topSwitchUrlList)){
			if (entity.getId() != null) {
				goodsImageDao.executeUpdate(QL_DELETE_IMG_BY_GOODIS,
						entity.getId(), GoodsImageCategory.TOP_SWITCH);
			}
			for(String url : topSwitchUrlList){
				GoodsImage goodsImage = new GoodsImage(
						entity, url, GoodsImageCategory.TOP_SWITCH);
				saveImage(goodsImage);
			}
		}

		List<GoodsImage> detailImageList = new ArrayList<GoodsImage>();
		if(isModify){
			detailImageList = findByGoodsAndCategory(
					entity.getId(), GoodsImageCategory.DETAILS);
		}
		if(SimpleUtils.isNotNullList(tmpDetailIdsList)){
			if(SimpleUtils.isNullList(detailImageList)){
				detailImageList = new ArrayList<GoodsImage>();
			}
			for(Integer id : tmpDetailIdsList){
				TmpFile tmpFile = tmpFileService.get(id);

				String imgPath = tmpFileService.dowithImg(
						GOODS_IMG_ROOT_PATH, idStr, tmpFile.getUrl());
				GoodsImage goodsImage = new GoodsImage(
						entity, imgPath, GoodsImageCategory.DETAILS);
				saveImage(goodsImage);

				tmpFileService.deleteById(id, false);

				detailImageList.add(goodsImage);
			}
		}
		
		List<GoodsImage> detailImageUrlList = new ArrayList<GoodsImage>();
		if(SimpleUtils.isNotNullList(detailsUrlList)){
			if (entity.getId() != null) {
				goodsImageDao.executeUpdate(QL_DELETE_IMG_BY_GOODIS,
						entity.getId(), GoodsImageCategory.DETAILS);
			}
			for(String url : detailsUrlList){
				GoodsImage goodsImage = new GoodsImage(
						entity, url, GoodsImageCategory.DETAILS);
				saveImage(goodsImage);
				detailImageUrlList.add(goodsImage);
			}
		}

		createHtml(entity, detailImageList, detailImageUrlList);

		// 新增上架商品或重新上架，新增商品期号
		if(entity.getState() == GoodsState.ENABLE &&
				(!isModify || oldState == GoodsState.OUT_SALES)){
			goodsTimesService.createByGoods(entity);
		}
	}

	@Transactional
	public void createHtml(GoodsInfo entity, List<GoodsImage> goodsImageList,
			List<GoodsImage> detailImageUrlList){
		String htmlUrl = entity.getDetailsHtmlUrl();
		if(StringUtils.isBlank(htmlUrl)){
			htmlUrl = configService.getStaticHtmlNo()+".html";
			entity.setDetailsHtmlUrl(htmlUrl);
		}
		String htmlPath = StringUtils.substringBefore(htmlUrl, "?");
		entity.setDetailsHtmlUrl(htmlPath + "?" + DateUtilsEx.getCurrentMMDDHHMMSS());
		save(entity);

		Map<String, Object> model = Maps.newHashMap();
		model.put("ctx", ROOT_URI);
		
		List<GoodsImage> goodsDetailImageList = new ArrayList<GoodsImage>();
		if (goodsImageList != null && goodsImageList.size() > 0) {
			for (GoodsImage img : goodsImageList) {
				if (StringUtils.isNotEmpty(img.getUrl())
						&& img.getUrl().indexOf("http") < 0) {
					goodsDetailImageList.add(img);
				}
			}
		}

		model.put("imageList", goodsDetailImageList);
		model.put("imgRootUri", GOODS_IMG_ROOT_URI);
		model.put("detailImageUrlList", detailImageUrlList);

		FreeMarkerUtils.createHtml("goods-details.ftl", model, GOODS_HTML_ROOT_PATH, htmlPath);
	}

	public List<GoodsImage> findByGoodsAndCategory(Integer goodsId, Byte imageCategory){
		return goodsImageDao.findByGoodsAndCategory(goodsId, imageCategory, CommonState.ENABLE);
	}

	@Transactional
	public void saveImage(GoodsImage entity){
		entity = goodsImageDao.save(entity);
	}

	public void search(Page<GoodsInfo> page, Map<String, String> params) {
		Query query = new Query();
		query.table("select t from GoodsInfo t")
				.like("t.name", params.get("name"))
				.eq("t.goodsCategory.id", SimpleUtils.stringToInteger(params.get("goodsCategoryId")));

		Byte state = SimpleUtils.stringToByte(params.get("state"));
		if(state == null){
			query.ne("t.state", CommonState.DELETED);
		}else{
			query.eq("t.state", state);
		}

		query.orderBy("t.createTime desc");

		dao.queryPage(page, query.getQLString(), query.getValues());
	}

	@Transactional
	public void delete(List<Integer> ids){
//		dao.deleteByIds(ids, UserUtils.getCurrentUser().getName(), Const.CommonState.DELETED);
		//deleteHtml(ids);
		dao.delGoodInfo(ids);
		dao.delGoodTimes(ids);
	}

	private void deleteHtml(List<Integer> ids){
		Map<String, Object> values = new HashMap<>();
		values.put("ids", ids);
		List<String> list = dao.findString(QL_QUERY_HTML_URL_BY_IDS, values);
		if(SimpleUtils.isNotNullList(list)){
			for(String htmlUri: list){
				String htmlPath = FileUtilsEx.joinPaths(GOODS_HTML_ROOT_PATH, "/",
						StringUtils.substringBefore(htmlUri, "?"));
				FileUtilsEx.deleteFile(htmlPath);
			}
		}
	}

	/**
	 * 删除缩略图
	 * @param id
	 * @return
	 */
	@Transactional
	public int deleteThumbImg(Integer id){
		GoodsInfo entity = get(id);
		if(entity == null || StringUtils.isBlank(entity.getThumbnail())) {
			return 0;
		}
		deleteImgFile(entity.getThumbnail());
		dao.executeUpdate(QL_DELETE_THUMB_IMG, id);
		return 1;
	}

	private void deleteImgFile(String imgUrl){
		String path = FileUtilsEx.joinPaths(GOODS_IMG_ROOT_PATH, "/", imgUrl);
		FileUtilsEx.deleteFile(path);
	}

	/***
	 * 删除顶部切换图片或图文详情
	 * @param id
	 * @return
	 */
	@Transactional
	public int deleteImg(Integer id){
		GoodsImage img = getGoodsImage(id);
		int result = 0;
		if(img != null){
			result = goodsImageDao.executeUpdate(QL_DELETE_IMG, img.getId());
			if(result > 0) {
				deleteImgFile(img.getUrl());
			}
		}

		return result;
	}

	public GoodsImage getGoodsImage(Integer id){
		return goodsImageDao.findOne(id);
	}

	@Transactional
	public void updateState(Integer id, Byte oldState){
		Byte state = null;
		if(oldState == GoodsState.ENABLE){         // 下架
			state = GoodsState.OUT_SALES;
		}else if(oldState == GoodsState.OUT_SALES) {  // 上架
			state = GoodsState.ENABLE;

			GoodsInfo goodsInfo = get(id);
			goodsTimesService.createByGoods(goodsInfo);
		}else{
			return;
		}

		dao.updateState(id, UserUtils.getCurrentUser().getName(), state);
	}

	/**
	 * 查询所有已上架但未开售的商品
	 * @return
	 */
	public List<GoodsInfo> findByNotSales(){
		List<Byte> goodsTimsState = Arrays.asList(GoodsTimesState.GOING);

		return dao.findByNotSales(GoodsState.ENABLE, goodsTimsState);
	}


	public Integer getIdByGoodsTimes(Integer goodsTimesId){
		return dao.getIdByGoodsTimes(goodsTimesId);
	}
}
