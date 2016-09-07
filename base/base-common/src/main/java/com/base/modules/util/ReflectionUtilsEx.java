package com.base.modules.util;

import com.base.modules.util.json.Field;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.util.*;


/**
 * 
 * Title: ReflectionUtilsEx.java
 * Description: 扩展ReflectionUtils，增加对获取多层属性的类型
 * Copyright: Copyright (c) 2011
 * Company：中山网政软件有限公司
 *
 * @author Micheal Cao
 * @version 1.0
 */
public class ReflectionUtilsEx extends ReflectionUtils{
	/**
	 * 获得多层属性的Class对象
	 * @param object 实体对象
	 * @param fieldName 属性
	 * @return Class对象 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("rawtypes")
	public static Class getMultilayerFieldType(Object object, final String fieldName) throws InstantiationException, IllegalAccessException{
		String fields[] = fieldName.split("\\.");
		if(fields.length==1){
			return ReflectionUtils.getFieldType(object,fields[0]);
		}else if(fields.length>0){
			Class clas = null;
			for (int i = 0; i < fields.length; i++) {
				clas = ReflectionUtils.getFieldType(object,fields[i]);
				object = clas.newInstance();
			}
			return clas;
		}else{
			return null;
		}
	}
	
	/***
	 * 获得多层属性的数据类型,不针对集合
 	 * @param object 对象
	 * @param fieldName 属性
	 * @return 数据类型
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("rawtypes")
	public static String getMultilayerFieldTypeName(Object object, final String fieldName) throws InstantiationException, IllegalAccessException{
		String fields[] = fieldName.split("\\.");
		if(fields.length==1){
			return ReflectionUtils.getFieldType(object,fields[0]).getName();
		}else if(fields.length>0){
			Class clas = null;
			for (int i = 0; i < fields.length; i++) {
				clas = ReflectionUtils.getFieldType(object,fields[i]);
				object = clas.newInstance();
			}
			return clas.getName();
		}else{
			return null;
		}
	}
	
	/**
	 * 获取属性值，最多两层，如：aa.bb
	 * @param object
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object getFieldValue(final Object object, final String fieldName) {
		try{
			if(fieldName.contains(".")){
				String fields[] = fieldName.split("\\.");
				String fieldName1 = fields[0];
				Class clas = getFieldType(object, fieldName1);
				if(clas == HashSet.class || clas == Set.class 
						|| clas == List.class || clas == ArrayList.class){//判断是否集合
					Collection c = (Collection)ReflectionUtils.getFieldValue(object, fieldName1);
					return ConvertUtils.convertPropertyToString(c, fields[1], ",");
				}
			}
			Object value = BeanUtils.getProperty(object, fieldName);	
			if (value != null) {
				try{
					String dataType = getMultilayerFieldTypeName(object, fieldName);
					if (Field.DataType.Date.equals(dataType))
						value = ( (Date)ReflectionUtils.getFieldValue(object, fieldName) ).getTime();
				}catch(Exception ex){
					
				}
			}
			
			return value;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
}
