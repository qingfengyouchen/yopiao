package com.base.modules.util;

import com.fasterxml.jackson.databind.util.BeanUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 
 * @author micheal
 * 
 */
public class ConvertUtils {

	/**
	 * 
	 * @param <T>
	 * @param beans
	 * @param keyPropertyName
	 * @param valuePropertyName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, PK, PV> void convertPropertyToMap(Collection<T> beans,
														String keyPropertyName,
														String valuePropertyName,
														Map<PK, PV> target) {

		Assert.notNull(beans, "beans must not null");
		Assert.hasLength(keyPropertyName, "keyPropertyName must not blank");
		Assert.hasLength(valuePropertyName, "valuePropertyName must not blank");
		Assert.notNull(target, "target must not null");

		try {

			for (T bean : beans) {
				PK pk = (PK) BeanUtils.getProperty(bean, keyPropertyName);
				PV pv = (PV) BeanUtils.getProperty(bean, valuePropertyName);
				target.put(pk, pv);
			}

		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 集合转map list
	 * @param beans
	 * @param keyAttrNames 属性集合
	 * @return
	 */
	public static List<Map<String, Object>> convertCollectionToListMap(Collection beans,
														String ...keyAttrNames) {

		Assert.notNull(keyAttrNames, "keyAttrNames must not blank");
		if(SimpleUtils.isNullList(beans)){
			return null;
		}

		List<Map<String, Object>> list = new ArrayList<>(beans.size());
		int attrLen = keyAttrNames.length;
		try {
			for (Object bean : beans) {
				Map<String, Object> map = new HashMap<>(attrLen);
				for(String keyAttrName: keyAttrNames){
					map.put(keyAttrName, BeanUtils.getProperty(bean, keyAttrName));
				}
				list.add(map);
			}
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}

		return list;
	}

	/**
	 * 集合转map list
	 * @param beans
	 * @param attrAliasArrList 属性数组，长度为2，当长度为1时表示不使用别名；否则使用别名(第二个元素就是别名)
	 * @return
	 */
	public static List<Map<String, Object>> convertCollectionToListMap(Collection beans,
																	   String[] ...attrAliasArrList) {

		Assert.notNull(attrAliasArrList, "attrAliasArrList must not blank");
		if(SimpleUtils.isNullList(beans)){
			return null;
		}

		List<Map<String, Object>> list = new ArrayList<>(beans.size());
		int attrLen = attrAliasArrList.length;
		try {
			for (Object bean : beans) {
				Map<String, Object> map = new HashMap<>(attrLen);
				for(String[] attrAliasArr: attrAliasArrList){
					if(SimpleUtils.isNullArr(attrAliasArr)){
						continue;
					}
					String key = attrAliasArr[0];
					if(attrAliasArr.length == 2){
						key = attrAliasArr[1];
					}
					map.put(key, BeanUtils.getProperty(bean, attrAliasArr[0]));
				}
				list.add(map);
			}
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}

		return list;
	}

	/**
	 * 集合转map list
	 * @param beans
	 * @param attrAliasArrList 属性数组，元素1为属性名；元素2为别名；元素3为URL前缀.
	 * @return
	 */
	public static List<Map<String, Object>> convertCollectionToListMapWithUrl(Collection beans,
																			  String[] ...attrAliasArrList) {

		Assert.notNull(attrAliasArrList, "attrAliasArrList must not blank");
		if(SimpleUtils.isNullList(beans)){
			return null;
		}

		List<Map<String, Object>> list = new ArrayList<>(beans.size());
		int attrLen = attrAliasArrList.length;
		try {
			for (Object bean : beans) {
				Map<String, Object> map = new HashMap<>(attrLen);
				for(String[] attrAliasArr: attrAliasArrList){
					if(SimpleUtils.isNullArr(attrAliasArr)){
						continue;
					}
					String key = attrAliasArr[0];
					if(attrAliasArr.length == 2){
						key = attrAliasArr[1];
					}
					if(attrAliasArr.length == 3){
						key = attrAliasArr[1];
						String urlPrefix = attrAliasArr[2];
						map.put(key, UrlUtils.joinUrl(urlPrefix, BeanUtils.getProperty(bean, attrAliasArr[0])));
						continue;
					}
					map.put(key, BeanUtils.getProperty(bean, attrAliasArr[0]));
				}
				list.add(map);
			}
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}

		return list;
	}

	/**
	 * 实体转map
	 * @param source
	 * @param attrAlias 属性数组，元素1为属性名；元素2为别名；元素3为URL前缀.
	 * @return
	 */
	public static Map<String, Object> convertEntityToMapWithUrl(Object source, String[] ...attrAlias) {

		Assert.notNull(attrAlias, "keyAttrNames must not blank");
		if(source == null){
			return null;
		}

		int attrLen = attrAlias.length;
		Map<String, Object> map = new HashMap<>(attrLen);
		try {
			for (String[] attrAlisArr : attrAlias) {
				if(SimpleUtils.isNullArr(attrAlisArr)){
					continue;
				}
				String key = attrAlisArr[0];
				if(attrAlisArr.length == 2){
					key = attrAlisArr[1];
				}
				if(attrAlisArr.length == 3){
					key = attrAlisArr[1];
					String urlPrefix = attrAlisArr[2];
					map.put(key, UrlUtils.joinUrl(urlPrefix, BeanUtils.getProperty(source, attrAlisArr[0])));
					continue;
				}
				map.put(key, BeanUtils.getProperty(source, attrAlisArr[0]));
			}
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
		return map;
	}

	/**
	 * 实体转map
	 * @param source
	 * @param attrAlias 属性数组，长度为2，当长度为1时表示不使用别名；否则使用别名(第二个元素就是别名)
	 * @return
	 */
	public static Map<String, Object> convertEntityToMap(Object source, String[] ...attrAlias) {

		Assert.notNull(attrAlias, "keyAttrNames must not blank");
		if(source == null){
			return null;
		}

		int attrLen = attrAlias.length;
		Map<String, Object> map = new HashMap<>(attrLen);
		try {
			for (String[] attrAlisArr : attrAlias) {
				if(SimpleUtils.isNullArr(attrAlisArr))
					continue;

				String key = attrAlisArr.length == 1 ? attrAlisArr[0] : attrAlisArr[1];
				//ReflectionUtilsEx.getFieldValue(source, attrAlisArr[0])
				map.put(key, BeanUtils.getProperty(source, attrAlisArr[0]));
			}
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
		return map;
	}

	public static Map<String, Object> convertEntityToMap(Object source, String ...keyAttrNames) {

		Assert.notNull(keyAttrNames, "keyAttrNames must not blank");
		if(source == null){
			return null;
		}

		int attrLen = keyAttrNames.length;
		Map<String, Object> map = new HashMap<>(attrLen);
		try {
			for (String keyAttrName : keyAttrNames) {
				map.put(keyAttrName, BeanUtils.getProperty(source, keyAttrName));
			}
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
		return map;
	}

	/**
	 * 
	 * @param <T>
	 * @param beans
	 * @param propertyName
	 * @param separator
	 * @return
	 */
	public static <T> String convertPropertyToString(Collection<T> beans,
														String propertyName,
														String separator) {
		Assert.notNull(beans, "beans must not null");
		Assert.hasLength(propertyName, "propertyName must not blank");

		List<String> target = new ArrayList<String>();
		convertPropertyToList(beans, propertyName, target);
		return StringUtils.join(target, separator);
	}

	/**
	 * 
	 * @param <T>
	 * @param beans
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, PV> void convertPropertyToList(Collection<T> beans,
													String propertyName,
													List<PV> target) {

		Assert.notNull(beans, "beans must not null");
		Assert.hasLength(propertyName, "propertyName must not blank");
		Assert.notNull(target, "target must not null");

		try {

			for (T bean : beans) {

				target.add((PV) BeanUtils.getProperty(bean, propertyName));
			}

		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static <T, PV> void convertPropertyToList(Collection<T> beans,
													 String propertyName,
													 List<PV> target, Class<PV> pvClass) {

		Assert.notNull(beans, "beans must not null");
		Assert.hasLength(propertyName, "propertyName must not blank");
		Assert.notNull(target, "target must not null");

		try {
			for (T bean : beans) {
				if(pvClass.getSimpleName().equals(String.class.getSimpleName()))
				target.add((PV) Reflections.getFieldValue(bean, propertyName));
			}

		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	private ConvertUtils(){
	}

}
