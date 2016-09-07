package com.zx.stlife.service.goods;

import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.base.modules.cache.memcached.SpyMemcachedClient;
import com.base.modules.util.FileUtilsEx;
import com.base.modules.util.SimpleUtils;
import com.base.modules.util.json.Field;
import com.base.modules.util.json.JsonUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.zx.stlife.entity.TmpFile;
import com.zx.stlife.entity.goods.GoodsCategory;
import com.zx.stlife.repository.jpa.goods.GoodsCategoryDao;
import com.zx.stlife.service.TmpFileService;
import com.zx.stlife.tools.memcached.MemcachedObjectType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.zx.stlife.constant.Const.*;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class GoodsCategoryService {

	private static Logger logger = LoggerFactory.getLogger(GoodsCategoryService.class);
	private static String QL_COUNT_BY_NAME =
			"select count(id) from GoodsCategory where name=?1 and state=?2";

	private static String QL_FIND_ALL =
			"select t from GoodsCategory t where t.state=?1 order by t.sortNum";

	private static String QL_FIND_ALL_WITH_ID_NAME =
			"select new GoodsCategory(t.id, t.name) from GoodsCategory t where t.state=?1 order by t.sortNum";

	private static final String QL_DELETE_THUMB_IMG =
			"update GoodsCategory set imgUrl = null where id=?1";

	@Autowired
	private GoodsCategoryDao dao;
	@Autowired
	private TmpFileService tmpFileService;
	@Autowired
	private SpyMemcachedClient spyMemcachedClient;

	public GoodsCategory get(Integer id){
		return dao.findOne(id);
	}

	public List<GoodsCategory> findAllWithIdAndName(){
		return dao.find(QL_FIND_ALL_WITH_ID_NAME, CommonState.ENABLE);
	}

	private List<Field> fields = JsonUtils.createField("id", "name", "imgUrl");

	public boolean isExistsName(String name){
		Long amount = (Long)dao.getObject(QL_COUNT_BY_NAME, name, CommonState.ENABLE);
		return amount > 0;
	}

	@Transactional
	public void save(GoodsCategory entity) {
		entity = dao.save(entity);
		refreshCache();
	}

	@Transactional
	public void save(GoodsCategory entity, Integer tmpFileId) {
		if( tmpFileId != null ){
			if( -1 == tmpFileId ){
				entity.setImgUrl(null);
			}else {
				TmpFile tmpFile = tmpFileService.get(tmpFileId);
				String imgPath = tmpFileService.dowithImg(
						GOODS_CATEGORY_IMG_ROOT_PATH, null, tmpFile.getUrl());
				entity.setImgUrl(imgPath);
				tmpFileService.deleteById(tmpFileId, false);
			}
		}

		entity = dao.save(entity);
		refreshCache();
	}

	public void search(Page<GoodsCategory> page, String name) {
		Query query = new Query();
		query.table("select t from GoodsCategory t")
				.like("t.name", name)
				.eq("t.state", CommonState.ENABLE)
				.orderBy("t.sortNum asc");

		dao.queryPage(page, query.getQLString(), query.getValues());
	}

	@Transactional
	public void delete(List<Integer> ids){
		dao.deleteByIds(ids, CommonState.DELETED);
		refreshCache();
	}

	@Transactional
	public int deleteThumbImg(Integer id){
		GoodsCategory entity = get(id);
		if(entity == null || StringUtils.isBlank(entity.getImgUrl())) {
			return 0;
		}
		deleteImgFile(entity.getImgUrl());
		dao.executeUpdate(QL_DELETE_THUMB_IMG, id);
		return 1;
	}

	private void deleteImgFile(String imgUrl){
		String path = FileUtilsEx.joinPaths(GOODS_CATEGORY_IMG_ROOT_PATH, "/", imgUrl);
		FileUtilsEx.deleteFile(path);
	}

	public List<GoodsCategory> findAllWithCache(){
		String key = getCacheKey();
		List<GoodsCategory> list = null;
		String jsonStr = spyMemcachedClient.get(key);
		if(jsonStr == null){
			list = refreshCache();
		}else{
			JavaType javaType = jsonMapper.contructCollectionType(ArrayList.class, GoodsCategory.class);
			list = jsonMapper.fromJson(jsonStr, javaType);
		}

		return list;
	}

	private List<GoodsCategory> refreshCache(){
		List<GoodsCategory> list = _findAll();
		String key = getCacheKey();
		if(SimpleUtils.isNullList(list)){
			spyMemcachedClient.safeDelete(key);
		}else{
			String jsonStr = JsonUtils.listToJson(list, fields);
			spyMemcachedClient.safeSet(key, MemcachedObjectType.TOP_SWITCH_IMG.getExpiredTime(), jsonStr);
		}

		return list;
	}

	private String getCacheKey() {
		return MemcachedObjectType.TOP_SWITCH_IMG.getPrefix();
	}


	private List<GoodsCategory> _findAll(){
		return dao.find(QL_FIND_ALL, CommonState.ENABLE);
	}
}
