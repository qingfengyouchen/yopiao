package com.base.modules.util.json;

import com.base.modules.util.ExceptionUtils;
import com.base.modules.util.SimpleUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author micheal
 *
 */
public class JsonUtils {
	/**双引号*/
	public static final String DOUBLE_QUOTES="\"";
	/**id name属性*/
	public static final List<Field> FIELD_OF_ID_NAME = createField("id", "name");
	
	public final static ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * @param bean
	 * @return
	 */
	public static String toJsonString(Object bean) {
		String json = null;
		try {
			json = MAPPER.writeValueAsString(bean);
		} catch (Exception e) {
			e.printStackTrace();
			throw ExceptionUtils.toUnchecked(e);
		}
		
		if(StringUtils.isNotBlank(json))
			json = json.replace("\"null\"", "")
						.replace("null", "\"\"");
		return json;
	}

	/**
	 * 
	 * @param <T>
	 * @param jsonString
	 * @param clazz
	 * @return
	 */
	public static <T> T fromJsonString(String jsonString, Class<T> clazz) {
		try {
			return MAPPER.readValue(jsonString, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			throw ExceptionUtils.toUnchecked(e);
		}
	}
	
	/***/
	public static List<Field> createField(String... fieldNames) {
		Assert.notEmpty(fieldNames);
		
		List<Field> fields = new ArrayList<Field>();
		for (String name : fieldNames)
			fields.add(new Field(name));
		
		return fields;
	}
	@SuppressWarnings("rawtypes")
	public static String listToJson(List list, List<Field> fields){
		StringBuilder json = new StringBuilder("[");
		int size = 0;
		if(list != null && (size = list.size()) > 0){
			if (list.get(0) instanceof Map) {
				boolean isEmptyField = SimpleUtils.isNullList(fields);
				for (int i = 0; i < size; i++) {
					Map map = (Map)list.get(i);
					if(isEmptyField){
						json.append(toJsonString(map))
							.append((i==size-1)?"":", ");
					}else{
						int fieldSize = fields.size();
						json.append("{");
						for (int j = 0;j < fieldSize; j++) {
							Field field = fields.get(j);
							Object value = map.get(field.getAttrName());
							
							if (value != null) {
								String dataType = value.getClass().getName();
								if(Field.DataType.Date.equals(dataType) || Field.DataType.TIMESTAMP.equals(dataType))
									value = ( (Date)value).getTime();
							}
							
							json.append(addAroundDoubleQuotes(field.getShortName())).append(" : ").append(addAroundDoubleQuotes( value==null ? "" : value ))
								.append((j == fieldSize-1)? "" : ", ");
						}
						json.append("}").append((i==size-1)?"":", ");
					}
				}
			}else{
				int fieldSize = fields.size();
				for (int i = 0; i < size; i++) {
					Object entity = list.get(i);
					json.append("{");
					entityToJson(json, entity, fields, fieldSize);
					json.append("}").append((i==size-1)?"":", ");
				}
			}
		}
		json.append("]");
		return json.toString();
	}
	
	public static String entityToJson(Object entity, List<Field> fields) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		entityToJson(json, entity, fields, fields.size());
		json.append("}");
		return json.toString();
	}

	public static String addAroundDoubleQuotes(Object str) {
		if (str instanceof String) {
			String value = (String)str;
			value = StringUtils.replace(value, "'", "\\'");					//将单引号转义
			value = StringUtils.replace(value, "\"", "\\\"");				//将双引号转义
			value = StringUtils.replace(value, "\r\n", "\\u000d\\u000a");	//将回车换行转义
			value = StringUtils.replace(value, "\n", "\\u000a");			//将回车转义
			str = value;
		}
		StringBuffer sb = new StringBuffer();
		return sb.append(DOUBLE_QUOTES).append(str).append(DOUBLE_QUOTES).toString();
	}
	
	private static void entityToJson(StringBuilder json, Object entity, List<Field> fields, int fieldSize) {
		for (int j = 0; j < fieldSize; j++) {
			Object value = null;
			Field field = fields.get(j);
			try {
				value = BeanUtils.getProperty(entity, field.getAttrName());
			}catch (Exception ex){
			}

			if (value == null)
				value = "";
		
			json.append(addAroundDoubleQuotes(field.getShortName())).append(" : ").append(addAroundDoubleQuotes(value))
				.append((j == fieldSize-1)? "" : ", ");
		}
	}
}
