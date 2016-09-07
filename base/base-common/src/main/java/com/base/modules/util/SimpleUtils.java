package com.base.modules.util;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDHexGenerator;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.rmi.server.UID;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单函数集合.
 * 
 * 提供各种常用函数.
 * 
 * @author wing
 */
public class SimpleUtils {

	public static String TEMP = "temp_";
	private static final String[] CHINESE_NUMBER = new String[] { "○", "一",
			"二", "三", "四", "五", "六", "七", "八", "九", "十", };

	/**
	 * 生成一个独一的ID
	 */
	public static String getGUID() {
		return new UID().toString().replaceAll(":", "").replaceAll("-", "");
	}
	
	/**
	 * 采用hibernate生成uuid
	 * @return
	 */
	public static String getHibernateUUID(){
		IdentifierGenerator gen = new UUIDHexGenerator();
        return (String) gen.generate(null, null);
	}
	/**
	 * 将数字转换成汉语式的字符串
	 * 
	 * @param number
	 *            数字
	 */
	public static String getChineseNumber(int number) {
		if (number == 10)
			return CHINESE_NUMBER[10];
		String num = Integer.toString(number);
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < num.length(); i++) {
			int n = Integer.parseInt(num.substring(i, i + 1));
			if (num.length() == 2 && n == 1 && i == 0) // 改十
				buf.append(CHINESE_NUMBER[10]);
			else
				buf.append(CHINESE_NUMBER[n]);
		}
		return buf.toString();
	}

	/**
	 * 将数字转换成汉语式的字符串（只考虑两位数）
	 * 
	 * @param number
	 *            数字
	 */
	public static String getChineseNumber2(int number) {
		if (number == 10)
			return CHINESE_NUMBER[10];
		String num = Integer.toString(number);
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < num.length(); i++) {
			int n = Integer.parseInt(num.substring(i, i + 1));
			if (num.length() == 2 && n == 1 && i == 0) // 改十
				buf.append(CHINESE_NUMBER[10]);
			else
				buf.append(CHINESE_NUMBER[n]);
			if (num.length() == 2 && i == 0) // 增加十，如三十三
				buf.append(CHINESE_NUMBER[10]);
		}
		return buf.toString();
	}

	/**
	 * 将Date按照指定格式转换成String.
	 */
	public static String dateToString(Date date, String format) {
		if (date == null)
			return "";
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	/**
	 * 将Date按照默认格式yyyy-MM-dd转换成String.
	 */
	public static String dateToString(Date date) {
		return dateToString(date, "yyyy-MM-dd");
	}

	/** 将指定格式的String转换成Date */
	public static Date stringToDate(String source, String format) {
		if(StringUtils.isBlank(source))
			return null;
		
		source = source.trim();
		if(source.length() == 10)
			format = "yyyy-MM-dd";
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(source);
		} catch (Exception e) {
		}
		return null;
	}

	public static Date stringToDate(String source, Date defaultDate, String format) {
		Date date = stringToDate(source, format);
		return date == null ? defaultDate : date;
	}

	/** 将格式yyyy-MM-dd的String转换成Date */
	public static Date stringToDate(String source) {
		return stringToDate(source, "yyyy-MM-dd");
	}

	/**
	 * 将对象转换成字符串
	 */
	public static String objectToString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	/**
	 * 将Date按照中文日期格式转换成String.
	 */
	public static String dateToChineseStyle(Date sourceDate) {
		// 日期格式转化为字符串格式
		String dateString = dateToString(sourceDate, "yyyy年MM月dd日");
		// 用来存储加工重组字符符串日期
		String[] changeDate = new String[9];
		// 重组字符串格式日期，方便下一步对其进行操作
		for (int i = 0; i < dateString.length(); i++) {
			if (i < 5) {
				changeDate[i] = String.valueOf(dateString.charAt(i));
			}
			// 将月份的数值的两位字符合并成一个字符串
			changeDate[5] = String.valueOf(dateString.charAt(5))
					+ String.valueOf(dateString.charAt(6));
			changeDate[6] = String.valueOf(dateString.charAt(7));
			// 将日期的数值的两位字符合并成一个字符串
			changeDate[7] = String.valueOf(dateString.charAt(8))
					+ String.valueOf(dateString.charAt(9));
			changeDate[8] = String.valueOf(dateString.charAt(10));
		}
		// 遍历重组后的字符串格式日期数组，生成中文日期格式字符串
		String targetString = new String();
		for (int i = 0; i < changeDate.length; i++) {
			if (changeDate[i].equals("0")) {
				targetString += "零";
				continue;
			}

			if (changeDate[i].equals("1") || changeDate[i].equals("01")) {
				targetString += "一";
				continue;
			}

			if (changeDate[i].equals("2") || changeDate[i].equals("02")) {
				targetString += "二";
				continue;
			}

			if (changeDate[i].equals("3") || changeDate[i].equals("03")) {
				targetString += "三";
				continue;
			}

			if (changeDate[i].equals("4") || changeDate[i].equals("04")) {
				targetString += "四";
				continue;
			}

			if (changeDate[i].equals("5") || changeDate[i].equals("05")) {
				targetString += "五";
				continue;
			}

			if (changeDate[i].equals("6") || changeDate[i].equals("06")) {
				targetString += "六";
				continue;
			}

			if (changeDate[i].equals("7") || changeDate[i].equals("07")) {
				targetString += "七";
				continue;
			}

			if (changeDate[i].equals("8") || changeDate[i].equals("08")) {
				targetString += "八";
				continue;
			}

			if (changeDate[i].equals("9") || changeDate[i].equals("09")) {
				targetString += "九";
				continue;
			}

			if (changeDate[i].equals("10")) {
				targetString += "十";
				continue;
			}

			if (changeDate[i].equals("11")) {
				targetString += "十一";
				continue;
			}

			if (changeDate[i].equals("12")) {
				targetString += "十二";
				continue;
			}

			if (changeDate[i].equals("13")) {
				targetString += "十三";
				continue;
			}

			if (changeDate[i].equals("14")) {
				targetString += "十四";
				continue;
			}

			if (changeDate[i].equals("15")) {
				targetString += "十五";
				continue;
			}

			if (changeDate[i].equals("16")) {
				targetString += "十六";
				continue;
			}

			if (changeDate[i].equals("17")) {
				targetString += "十七";
				continue;
			}

			if (changeDate[i].equals("18")) {
				targetString += "十八";
				continue;
			}

			if (changeDate[i].equals("19")) {
				targetString += "十九";
				continue;
			}

			if (changeDate[i].equals("20")) {
				targetString += "二十";
				continue;
			}

			if (changeDate[i].equals("21")) {
				targetString += "二十一";
				continue;
			}

			if (changeDate[i].equals("22")) {
				targetString += "二十二";
				continue;
			}

			if (changeDate[i].equals("23")) {
				targetString += "二十三";
				continue;
			}

			if (changeDate[i].equals("24")) {
				targetString += "二十四";
				continue;
			}

			if (changeDate[i].equals("25")) {
				targetString += "二十五";
				continue;
			}

			if (changeDate[i].equals("26")) {
				targetString += "二十六";
				continue;
			}

			if (changeDate[i].equals("27")) {
				targetString += "二十七";
				continue;
			}

			if (changeDate[i].equals("28")) {
				targetString += "二十八";
				continue;
			}

			if (changeDate[i].equals("29")) {
				targetString += "二十九";
				continue;
			}

			if (changeDate[i].equals("30")) {
				targetString += "三十";
				continue;
			}

			if (changeDate[i].equals("31")) {
				targetString += "三十一";
				continue;
			}

			if (changeDate[i].equals("年")) {
				targetString += "年";
				continue;
			}

			if (changeDate[i].equals("月")) {
				targetString += "月";
				continue;
			}

			if (changeDate[i].equals("日")) {
				targetString += "日";
				continue;
			}
		}
		return targetString;
	}

	/**
	 * 将数字转为中文，null返回“零”，可判断负数，暂时只到亿
	 * 
	 * @param src
	 * @return
	 */
	public static String IntegerToChinese(int src) {
		String result = "";
		String str = String.valueOf(src);
		String[] values = str.split("");
		int k = values.length;
		for (int i = 0; i < values.length; i++) {
			if ("-".equals(values[i])) {
				result += "负";
			} else if (!"".equals(values[i])) {
				if ("0".equals(values[i]) && k > 1 && !result.endsWith("零")
						&& !(isAllEquals(values, i + 1, "0") == 1)) {
					result += "零";
				}
				if ("1".equals(values[i])) {
					if ((k > 2) || (!"".equals(result) && !"负".equals(result))) {
						result += "一";
					}
					result += getNumUnit(k);
				} else if ("2".equals(values[i])) {
					result += "二";
					result += getNumUnit(k);
				} else if ("3".equals(values[i])) {
					result += "三";
					result += getNumUnit(k);
				} else if ("4".equals(values[i])) {
					result += "四";
					result += getNumUnit(k);
				} else if ("5".equals(values[i])) {
					result += "五";
					result += getNumUnit(k);
				} else if ("6".equals(values[i])) {
					result += "六";
					result += getNumUnit(k);
				} else if ("7".equals(values[i])) {
					result += "七";
					result += getNumUnit(k);
				} else if ("8".equals(values[i])) {
					result += "八";
					result += getNumUnit(k);
				} else if ("9".equals(values[i])) {
					result += "九";
					result += getNumUnit(k);
				}
			}
			k = k - 1;
		}
		return "".equals(result) ? "零" : result;
	}

	/**
	 * 取得位数单位
	 * 
	 * @param i
	 *            位数
	 * @return
	 */
	public static String getNumUnit(int i) {
		String unit = "";
		if (i > 0) {
			int k2 = i % 4;

			// 数位
			if ((i == 4) || (i > 4 && k2 == 0)) {
				unit += "千";
			} else if (k2 == 3) {
				unit += "百";
			} else if (k2 == 2) {
				unit += "十";
			}
			// 单位
			if (i > 8) {
				unit += "亿";
			} else if (i == 5) {
				unit += "万";
			}
		}
		return unit;
	}

	/**
	 * 计算中英文混合字符串的长度
	 */
	public static int stringLength(String src, String code) {
		int len = 0;
		int step = "utf-8".equals(code.toLowerCase()) ? 3 : 2;
		if (StringUtils.isNotEmpty(src)) {
			try {
				byte[] bt = src.getBytes(code);
				for (int i = 0; i < bt.length;) {
					if (bt[i] < 0) {
						i += step;
						len += 2;
					} else {
						i++;
						len++;
					}
				}
			} catch (UnsupportedEncodingException e) {
			}
		}
		return len;
	}

	/**
	 * 判断数组c在i开始的元素是否都和d相等
	 * 
	 * @param c
	 * @param i
	 * @param d
	 * @return {0：不全相等，1：全相等，2：i大于c长度}
	 */
	public static int isAllEquals(String[] c, int i, String d) {
		if (i > c.length)
			return 2;
		else {
			for (int j = i; j < c.length; j++) {
				if (!d.equals(c[j])) {
					return 0;
				}
			}
		}
		return 1;
	}

	public static boolean isNumeric(String source) {
		Pattern pattern = Pattern.compile("^-?[0-9]+");
		Matcher isNum = pattern.matcher(source);
		return isNum.matches();
	}

	/**
	 * 将String转换成Integer.
	 */
	public static Integer stringToInteger(String source) {
		if (source != null)
			source = source.trim();
		if (StringUtils.isNotEmpty(source))
			if (isNumeric(source))
				return Integer.parseInt(source);
		return null;
	}

	public static Integer stringToInteger(String source, Integer defaultVal) {
		Integer result = stringToInteger(source);
		return result == null ? defaultVal : result;
	}

	/**
	 * 将String转换成Long.
	 */
	public static Long stringToLong(String source) {
		if (source != null)
			source = source.trim();
		if (StringUtils.isNotEmpty(source))
			if (isNumeric(source))
				return Long.parseLong(source);
		return null;
	}

	public static Long stringToLong(String source, Long defaultVal) {
		Integer result = stringToInteger(source);
		return result == null ? defaultVal : result;
	}

	/**
	 * 将String转换成Short.
	 */
	public static Short stringToShort(String source) {
		if (source != null)
			source = source.trim();
		if (StringUtils.isNotEmpty(source))
			if (isNumeric(source))
				return Short.valueOf(source);
		return null;
	}

	/**
	 * 将String转换成Byte.
	 */
	public static Byte stringToByte(String source) {
		if (source != null)
			source = source.trim();
		if (StringUtils.isNotEmpty(source))
			if (isNumeric(source))
				return Byte.valueOf(source);
		return null;
	}

	/**
	 * String转换成Double
	 */
	public static double stringToDouble(String source) {
		if (StringUtils.isNotEmpty(source)) {
			source = source.trim();
			return Double.parseDouble(source);
		}
		return 0.0;
	}

	/**
	 * 将String转换成Boolean.
	 */
	public static Boolean stringToBoolean(String source) {
		if (source != null)
			source = source.trim();
		if (StringUtils.isNotEmpty(source))
			return "1".equals(source) || "true".equalsIgnoreCase(source);
		return null;
	}

	/**
	 * 将String转换成boolean.
	 */
	public static boolean stringToboolean(String source) {
		Boolean b = stringToBoolean(source);
		return b == null ? false : b.booleanValue();
	}

	/**
	 * 清除小数点后面的0
	 */
	public static String clearNumberZero(String num) {
		if (StringUtils.isNotEmpty(num)) {
			int point = num.indexOf(".");
			for (int i = num.length() - 1; i >= point; i--) {
				if (num.charAt(i) != '0') {
					num = num.substring(0, i);
					break;
				}
			}
		}
		return num;
	}

	public static String clearNumberZero(double value) {
		String num = String.format("%.4f", value);
		return SimpleUtils.clearNumberZero(num);
	}

	public static String integerToString(Integer value) {
		return (value == null) ? "" : String.valueOf(value);
	}

	public static String doubleToString(Double value) {
		return (value == null) ? "" : String.valueOf(value);
	}

	/** 两个对象相加 **/
	public static Integer addInteger(Integer n1, Integer n2) {
		if (n1 == null)
			return n2;
		if (n2 == null)
			return n1;
		return n1 + n2;
	}

	/** Double类型相加 */
	public static Double addDouble(Double one, Double other) {
		if (one != null && other != null) {
			BigDecimal d1 = new BigDecimal(Double.toString(one));
			BigDecimal d2 = new BigDecimal(Double.toString(other));
			return d1.add(d2).doubleValue();
		} else if (one != null)
			return one.doubleValue();
		else if (other != null)
			return other.doubleValue();
		return 0.0;
	}

	/** Double类型相减 */
	public static Double subtractDouble(Double one, Double other) {
		if (one != null && other != null) {
			BigDecimal d1 = new BigDecimal(Double.toString(one));
			BigDecimal d2 = new BigDecimal(Double.toString(other));
			return d1.add(d2).doubleValue();
		} else if (one != null)
			return one.doubleValue();
		else if (other != null)
			return other.doubleValue();
		return 0.0;
	}

	/** Double类型相乘 **/
	public static double multiplyDouble(double one, double other) {
		BigDecimal d1 = new BigDecimal(Double.toString(one));
		BigDecimal d2 = new BigDecimal(Double.toString(other));
		return d1.multiply(d2).doubleValue();
	}

	/** 相除，精确到小数点后scale位 */
	public static double divideDouble(double divisor, double divident, int scale) {
		if (divident == 0d) {
			return new BigDecimal(Double.toString(0d)).doubleValue();
		}
		BigDecimal d1 = new BigDecimal(Double.toString(divisor));
		BigDecimal d2 = new BigDecimal(Double.toString(divident));
		return d1.divide(d2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param value
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static Double roundDouble(Double value, int scale) {
		if (null == value) {
			BigDecimal v = new BigDecimal("0");
			BigDecimal one = new BigDecimal("1");
			return v.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		BigDecimal v = new BigDecimal(Double.toString(value));
		BigDecimal one = new BigDecimal("1");
		return v.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * double转成百分比，精确到小数点后scale位
	 * 
	 * @param divisor
	 * @param dividend
	 * @param scale
	 * @return
	 */
	public static String doubleToPercent(Double divisor, Double dividend,
			int scale) {
		if (null == divisor || null == dividend) {
			return "";
		}
		double result = divideDouble(divisor * 100d, dividend, scale);
		return String.format("%s%%", result);
	}

	/**
	 * 将数组用分割符号(如",")分开,各值用修饰符号(如"'")包裹成字符串
	 * 
	 * @param list
	 * @param divisionSign
	 *            分割符号
	 * @param embellishSign
	 *            修饰符号,为null则不需要修饰符号
	 * @return 字符串
	 * @throws Exception
	 */
	public static String listToString(List<String> list, String divisionSign,
			String embellishSign) throws Exception {
		StringBuffer sql = new StringBuffer();

		if (list == null || list.size() <= 0)
			return null;

		for (int i = 0; i < list.size(); i++) {
			if (embellishSign != null)
				sql.append(embellishSign);
			sql.append(list.get(i));

			if (embellishSign != null)
				sql.append(embellishSign);
			sql.append(divisionSign);
		}

		String returnValue = sql.toString();
		returnValue = returnValue.substring(0, returnValue.length() - 1);
		return returnValue;
	}

	/**
	 * 字符串list转字符串数组
	 * 
	 * @param list
	 * @return
	 */
	public static String[] listToArray(List<String> list) {
		if (list == null)
			return null;
		String[] arr = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i);
		}
		return arr;
	}

	/***
	 * 字符串转换成集合
	 * 
	 * @param str
	 * @param divisionSign
	 *            分割符号
	 * @return
	 */
	public static List<String> stringToList(String str, String divisionSign) {
		List<String> list = new ArrayList<String>();
		if (StringUtils.isEmpty(str))
			return list;

		if (StringUtils.isEmpty(divisionSign))
			divisionSign = ",";
		String arr[] = str.split(divisionSign);
		for (int i = 0; i < arr.length; i++) {
			list.add(arr[i]);
		}
		return list;
	}

	/**
	 * 将数组用分割符号(如",")分开,各值用修饰符号(如"'")包裹成字符串
	 * 
	 * @param strs
	 * @param divisionSign
	 *            分割符号
	 * @param embellishSign
	 *            修饰符号,为null则不需要修饰符号
	 * @return 字符串
	 * @throws Exception
	 */
	public static String arrayToString(Object[] strs, String divisionSign,
			String embellishSign) throws Exception {
		StringBuffer sql = new StringBuffer();

		if (strs == null || strs.length == 0)
			return null;

		for (int i = 0; i < strs.length; i++) {
			if (StringUtils.isEmpty((String) strs[i]))
				continue;
			if (embellishSign != null)
				sql.append(embellishSign);
			sql.append(strs[i]);

			if (embellishSign != null)
				sql.append(embellishSign);
			sql.append(divisionSign);
		}

		String returnValue = sql.toString();
		returnValue = returnValue.substring(0, returnValue.length()
				- divisionSign.length());

		return returnValue;
	}

	/***
	 * 字符串数组转List
	 * 
	 * @param strs
	 *            符串数组
	 * @return List 集合
	 */
	public static List<String> arrayToList(String[] strs) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < strs.length; i++) {
			list.add(strs[i]);
		}
		return list;
	}

	/**
	 * 对象数组中是否包含指定对象（null也可判断）
	 * 
	 * @return 指定对象在对象数组中的第一个符合的位置，不包含该对象则返回-1
	 */
	public static int containIndex(Object[] objs, Object obj) {
		if (objs != null) {
			for (int i = 0; i < objs.length; i++) {
				if (obj == null && objs[i] == null)
					return i;
				else if (obj != null && obj.equals(objs[i]))
					return i;
			}
		}
		return -1;
	}

	@SuppressWarnings("rawtypes")
	public static class FieldInfo {
		private Object oldFieldValue;
		private Object newFieldValue;

		private Class fieldClass;

		public FieldInfo(Object oldFieldValue, Object newFieldValue,
				Class fieldClass) {
			this.oldFieldValue = oldFieldValue;
			this.newFieldValue = newFieldValue;
			this.fieldClass = fieldClass;
		}

		public Object getOldFieldValue() {
			return oldFieldValue;
		}

		public void setOldFieldValue(Object oldFieldValue) {
			this.oldFieldValue = oldFieldValue;
		}

		public Object getNewFieldValue() {
			return newFieldValue;
		}

		public void setNewFieldValue(Object newFieldValue) {
			this.newFieldValue = newFieldValue;
		}

		public Class getFieldClass() {
			return fieldClass;
		}

		public void setFieldClass(Class fieldClass) {
			this.fieldClass = fieldClass;
		}
	}

	/**
	 * 还原被过滤的字符串
	 * 
	 * @param src
	 * @return 字符串
	 */
	public static String decodeString(String src) {
		if (src == null)
			return "";
		String des = src;
		// 被过滤的字符map
		Map<String, String> map = new HashMap<String, String>();
		map.put(">", "&gt;");
		map.put("<", "&lt;");
		map.put("'", "&apos;");
		map.put("\"", "&quot;");

		for (Map.Entry<String, String> entry : map.entrySet()) {
			des = des.replaceAll(entry.getValue(), entry.getKey());
		}
		return des;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isNullList(Collection c) {
		if (c == null || c.size() < 1)
			return true;

		return false;
	}

	public static boolean isNullArr(String[] arr) {
		if (arr == null || arr.length < 1)
			return true;

		return false;
	}

	public static boolean isNotNullList(Collection c) {
		return !isNullList(c);
	}

	public static boolean isNotNullArr(String[] arr) {
		return !isNullArr(arr);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List splitList(List list, int splitLength) {
		List lists = new ArrayList();
		int listSize = list.size();
		if (listSize <= splitLength) {
			lists.add(list);
		} else {
			int size = listSize / splitLength;
			if (listSize % splitLength != 0)
				size++;

			for (int i = 0; i < size - 1; i++) {
				List list1 = list.subList(i * splitLength, (i + 1)
						* splitLength);
				lists.add(list1);
			}

			List list1 = list.subList((size - 1) * splitLength, listSize);
			lists.add(list1);
		}

		return lists;
	}

	// 保留两位小数
	private static DecimalFormat formator = new DecimalFormat("###,##0.00");

	public static String convertByteTo(long size) {
		double kb = 1024.0;
		double mb = 1048576.0;
		double gb = 1073741824.0;
		double tb = 1099511627776.0;
		if (size < kb) {
			return formator.format(size) + "B";
		} else if (size < mb) {
			return formator.format(size / kb) + "KB";
		} else if (size < gb) {
			return formator.format(size / mb) + "MB";
		} else if (size < tb) {
			return formator.format(size / gb) + "GB";
		} else {
			return formator.format(size / tb) + "TB";
		}
	}

	public static List<String> getWeekdayList(int days) {
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= days; i++)
			list.add(String.format("星期%s", CHINESE_NUMBER[i]));
		return list;
	}

	/**
	 * objStr 是否含有字符串arr数组中的元素
	 * 
	 * @param objStr
	 * @param arr
	 * @return
	 */
	public static boolean hasContains(String objStr, String[] arr) {
		boolean flag = false;
		for (String string : arr) {
			if (objStr.contains(string)) {
				flag = true;
				break;
			}
		}

		return flag;
	}

	/**
	 * 去除字符串
	 * 
	 * @param regEx
	 *            正则表达式
	 * @param str
	 * @return
	 */
	public static String replaceAll(String regEx, String str) {
		if (StringUtils.isEmpty(str))
			return null;

		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);

		return m.replaceAll("").trim();
	}
	
	public static String formatNumber(int num, int length, String fillCharOnLeft){
		String str = String.valueOf(num);
		int len = length - str.length();
		for (int i = len; i > 0; i--) {
			str = fillCharOnLeft + str;
		}
		return str;
	}
	
	public static boolean regex(String value,String regex){
		if(StringUtils.isBlank(value))
			return false;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	
	public static boolean isMobileNo(String mobileNo){
		if(StringUtils.isBlank(mobileNo))
			return false;
		
		String regex = "^1\\d{10}$";
		return regex(mobileNo, regex);
	}

	public static int doubleToInteger(Double num){
		return num == null ? 0 : num.intValue();
	}

	public static String join(String str1, String ...strs){
		if(strs == null || strs.length == 0){
			return str1;
		}

		StringBuffer sb = new StringBuffer(str1);
		for(String str : strs) {
			sb.append(str);
		}

		return sb.toString();
	}

	public static String limitLength(String str, int len){
		if(str == null)
			return null;

		if(str.length() <= len)
			return str;

		return str.substring(0, len);
	}

	public static List<Integer> createNum(int from, int to){
		if(from >= to){
			return null;
		}

		List<Integer> list = new ArrayList<>();
		while(from <= to){
			list.add(from);
			from ++;
		}

		return list;
	}

	private static String EXCLUDE_URIS = "^.*(" +
			"/login/|" +
			"/user/saveUser|" +
			"/user/modifyInfo|" +
			"/wx/wechat" +
			").*$";
	public static void main(String[] args) {
		String url = "/stlife/wx/user/home";
		System.out.println(SimpleUtils.regex(url, EXCLUDE_URIS));
	}
}
