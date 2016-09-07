package com.zx.stlife.service.sys;

import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.base.modules.cache.memcached.SpyMemcachedClient;
import com.base.modules.util.ConvertUtils;
import com.base.modules.util.FileUtilsEx;
import com.base.modules.util.SimpleUtils;
import com.base.modules.util.json.Field;
import com.base.modules.util.json.JsonUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Maps;
import static com.zx.stlife.constant.Const.*;
import com.zx.stlife.entity.TmpFile;
import com.zx.stlife.entity.sys.ImageSetting;
import com.zx.stlife.repository.jpa.sys.ImageSettingDao;
import com.zx.stlife.service.TmpFileService;
import com.zx.stlife.tools.memcached.MemcachedObjectType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class ImageSettingService {

	private static Logger logger = LoggerFactory.getLogger(ImageSettingService.class);

	@Autowired
	private ImageSettingDao imageSettingDao;
	@Autowired
	private TmpFileService tmpFileService;
	@Autowired
	private SpyMemcachedClient spyMemcachedClient;

	private static final String QL_DELETE =
			"update ImageSetting set state=:state where id in (:ids)";

	private static final String QL_DELETE_THUMB_IMG =
			"update ImageSetting set url = null where id=?1";

	private static final String QL_QUERY_ALL =
			"select new ImageSetting(id, actionType, value, url, sortNum) from ImageSetting " +
					"where state=?1 and category=?2 order by sortNum";

	private List<Field> fields = JsonUtils.createField("id", "actionType", "value", "url", "sortNum");

	public List<ImageSetting> findAllByCategory(Byte category){
		return _findAll(category);
	}

	public ImageSetting get(Integer id){
		return imageSettingDao.findOne(id);
	}

	@Transactional
	public void save(ImageSetting entity){
		entity = imageSettingDao.save(entity);
		refreshCache(entity.getCategory());
	}

	@Transactional
	public void save(ImageSetting entity, Integer tmpFileId){
		Byte category = entity.getCategory();

		if( tmpFileId != null ){
			if( -1 == tmpFileId ){
				entity.setUrl(null);
			}else {
				TmpFile tmpFile = tmpFileService.get(tmpFileId);
				String imgPath = tmpFileService.dowithImg(
						TOP_SWITCH_IMG_ROOT_PATH, String.valueOf(category), tmpFile.getUrl());
				entity.setUrl(imgPath);
				tmpFileService.deleteById(tmpFileId, false);
			}
		}

		entity = imageSettingDao.save(entity);
		refreshCache(category);
	}

	@Transactional
	public void delete(List<Integer> ids, Byte category){
		Map<String, Object> values = new HashMap<>();
		values.put("state", CommonState.DELETED);
		values.put("ids", ids);
		imageSettingDao.executeUpdate(QL_DELETE, values);

		refreshCache(category);
	}

	@Transactional
	public int deleteThumbImg(Integer id){
		ImageSetting entity = get(id);
		if(entity == null || StringUtils.isBlank(entity.getUrl())) {
			return 0;
		}
		deleteImgFile(entity.getUrl());
		imageSettingDao.executeUpdate(QL_DELETE_THUMB_IMG, id);
		return 1;
	}

	private void deleteImgFile(String imgUrl){
		String path = FileUtilsEx.joinPaths(TOP_SWITCH_IMG_ROOT_PATH, "/", imgUrl);
		FileUtilsEx.deleteFile(path);
	}

	public List<ImageSetting> findAllWithCache(Byte category){
		String key = getCacheKey(category);
		List<ImageSetting> list = null;
		String jsonStr = spyMemcachedClient.get(key);
		if(jsonStr == null){
			list = refreshCache(category);
		}else{
			JavaType javaType = jsonMapper.contructCollectionType(ArrayList.class, ImageSetting.class);
			list = jsonMapper.fromJson(jsonStr, javaType);
		}

		return list;
	}

	private List<ImageSetting> refreshCache(Byte category){
		List<ImageSetting> list = _findAll(category);
		String key = getCacheKey(category);
		if(SimpleUtils.isNullList(list)){
			spyMemcachedClient.safeDelete(key);
		}else{
			String jsonStr = JsonUtils.listToJson(list, fields);//jsonMapper.toJson(list);
			spyMemcachedClient.safeSet(key, MemcachedObjectType.TOP_SWITCH_IMG.getExpiredTime(), jsonStr);
		}

		return list;
	}

	private String getCacheKey(Byte category){
		return MemcachedObjectType.TOP_SWITCH_IMG.getPrefix() + ":" + category;
	}

	private List<ImageSetting> _findAll(Byte category) {
		return imageSettingDao.find(QL_QUERY_ALL, CommonState.ENABLE, category);
	}
}
