/**
 * 
 */
package com.base.modules.util.json;


import org.apache.commons.lang3.StringUtils;

/**
 * 定义字段属性
 * @author micheal
 *
 */
public class Field {
	
	/**属性名称*/
	private String attrName;
	/**json对象属性名，当为空时，则该值为attrName的值*/
	private String shortName;
	/**数据类型*/
	private String dataType = DataType.String;
	/**格式*/
	private String pattern;
	
	public String getAttrName() {
		return attrName;
	}
	public Field setAttrName(String attrName) {
		this.attrName = attrName;
		return this ;
	}
	
	public String getShortName() {
		
		if(StringUtils.isEmpty(shortName))
			shortName = attrName;
		
		return shortName;
	}
	public Field setShortName(String shortName) {
		this.shortName = shortName;
		return this ;
	}
	public String getDataType() {
		return dataType;
	}
	public Field setDataType(String dataType) {
		this.dataType = dataType;
		return this ;
	}
	public String getPattern() {
		return pattern;
	}
	public Field setPattern(String pattern) {
		this.pattern = pattern;
		return this;
	}
	
	public class DataType{
		public static final String String = "java.lang.String";
		public static final String Date = "java.util.Date";
		public static final String TIMESTAMP = "java.sql.Timestamp";
	}
	
	public Field(){
	}
	
	public Field(String attrName){
		this.attrName = attrName;
	}
	
	public Field(String attrName, String shortName){
		this.attrName = attrName;
		this.shortName = shortName;
	}
}
