package com.base.modules.util;

import org.apache.commons.beanutils.BeanUtils;

public class CopyDiffClassPropertyUtils {
	/**
	 * 两个类不同类间复制相同属性名称的值
	 * 
	 * @param source
	 *            源
	 * @param target
	 *            目标
	 * @param properties
	 *            属性列表
	 */
	public static void copy(Object source, Object target, String... properties) {
		if (source == null || target == null
				|| SimpleUtils.isNullArr(properties))
			return;

		for (String property : properties) {
			try {
				BeanUtils.setProperty(target, property,
						BeanUtils.getProperty(source, property));
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
	}

	/**
	 * 两个类不同类间复制属性值
	 * 
	 * @param source
	 *            源
	 * @param target
	 *            目标
	 * @param arr
	 *            属性二维数组, y轴长度小于等于2，索引0:表示source的属性名称，索引1：表示target的属性名称
	 */
	public static void copy(Object source, Object target, String[][] arr) {
		if (source == null || target == null || arr == null || arr.length == 0)
			return;

		for (String[] names : arr) {
			if (names == null || names.length < 1)
				continue;

			String sourceProperty = names[0];
			String targetProperty = names.length == 1 ? sourceProperty
					: names[1];

			try {
				BeanUtils.setProperty(target, targetProperty,
						BeanUtils.getProperty(source, sourceProperty));
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
	}
}
