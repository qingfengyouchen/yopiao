package com.zx.stlife.service.sys;

import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.base.modules.cache.memcached.SpyMemcachedClient;
import com.base.modules.mapper.JsonMapper;
import com.base.modules.util.ConvertUtils;
import com.base.modules.util.SimpleUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.sys.Category;
import com.zx.stlife.repository.jpa.sys.CategoryDao;
import com.zx.stlife.tools.memcached.MemcachedObjectType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class CategoryService {

	private static Logger logger = LoggerFactory.getLogger(CategoryService.class);
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private SpyMemcachedClient spyMemcachedClient;

	private final JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();

	private static final String QL_DELETE =
			"update Category set state=:state where id in (:ids)";
	private static final String QL_COUNT_BY_NAME_CATEGROY =
			"select count(id) from Category where name=?1 and category=?2";

	private static final String QL_FIND_CATEGORY_BY_IDS =
			"select distinct category from Category where id in (:ids)";

	public Category get(Integer id){
		return categoryDao.findOne(id);
	}

	public void search(Page<Category> page, Map<String, String> params){
		Query query = buildQuery(params.get("name"), SimpleUtils.stringToByte(params.get("category")));
		categoryDao.queryPage(page, query.getQLString(), query.getValues());
	}

	public List<Category> findByCategory(Byte category){
		String key = MemcachedObjectType.CATEGORY.getPrefix() + category;
		List<Category> list = null;
		String jsonStr = spyMemcachedClient.get(key);
		if(jsonStr == null){
			list = refreshCache(category);
		}else{
			JavaType javaType = jsonMapper.contructCollectionType(List.class, Category.class);
			list = jsonMapper.fromJson(jsonStr, javaType);
		}

		return list;
	}

	public Map<Integer, String> findCategoryNameMap(Byte category){
		String key = MemcachedObjectType.CATEGORY.getPrefix() + category + ":map";
		Map<Integer, String> map =  null;
		String jsonStr = spyMemcachedClient.get(key);
		if(jsonStr == null){
			map = refreshMapCache(category);
		}else{
			JavaType javaType = jsonMapper.contructMapType(LinkedHashMap.class, Integer.class, String.class);
			map = jsonMapper.fromJson(jsonStr, javaType);
		}

		return map;
	}

	private List<Category> refreshCache(Byte category){
		List<Category> list = _findByCategory(category);
		String key = MemcachedObjectType.CATEGORY.getPrefix() + category;
		if(SimpleUtils.isNullList(list)){
			spyMemcachedClient.safeDelete(key);
		}else{
			String jsonStr = jsonMapper.toJson(list);
			spyMemcachedClient.safeSet(key, MemcachedObjectType.CATEGORY.getExpiredTime(), jsonStr);
		}

		return list;
	}

	private Map<Integer, String> refreshMapCache(Byte category){
		List<Category> list = _findByCategory(category);
		String key = MemcachedObjectType.CATEGORY.getPrefix() + category + ":map";
		Map<Integer, String> map = null;
		if(SimpleUtils.isNullList(list)){
			spyMemcachedClient.safeDelete(key);
		}else{
			map = new LinkedHashMap<>();
			ConvertUtils.convertPropertyToMap(list, "id", "name", map);
			String jsonStr = jsonMapper.toJson(map);
			spyMemcachedClient.safeSet(key, MemcachedObjectType.CATEGORY.getExpiredTime(), jsonStr);
		}

		return map;
	}

	private List<Category> _findByCategory(Byte category){
		Query query = buildQuery(null, category);
		return categoryDao.find(query.getQLString(), query.getValues());
	}

	private Query buildQuery(String name, Byte category) {
		return new Query()
				.table("select t from Category t")
				.like("t.name", name)
				.eq("t.category", category)
				.eq("t.state", Const.CommonState.ENABLE)
				.orderBy("t.category, t.sortNum");
	}

	@Transactional
	public void save(Category entity){
		entity.setState(Const.CommonState.ENABLE);
		entity = categoryDao.save(entity);
		if(entity.getCategory() != null) {
			refreshCache(entity.getCategory());
			refreshMapCache(entity.getCategory());
		}
	}

	@Transactional
	public void delete(List<Integer> ids){
		Map<String, Object> values = new HashMap<>();
		values.put("ids", ids);
		List<Byte> categoryList = findCategoryByIds(ids);

		values.put("state", Const.CommonState.DELETED);
		categoryDao.executeUpdate(QL_DELETE, values);

		if(SimpleUtils.isNotNullList(categoryList)){
			for(Byte category: categoryList) {
				refreshCache(category);
				refreshMapCache(category);
			}
		}
	}

	public List<Byte> findCategoryByIds(List<Integer> ids){
		Map<String, Object> values = new HashMap<>();
		values.put("ids", ids);
		return categoryDao.findByte(QL_FIND_CATEGORY_BY_IDS, values);
	}

	public boolean isExistsName(String name, Byte category){
		Long amount = (Long)categoryDao.getObject(QL_COUNT_BY_NAME_CATEGROY, name, category);
		return amount > 0;
	}
}
