package com.base.jpa.query;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.Assert.isTrue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author loudyn
 */
public class Query {
	/**
	 * @author loudyn
	 */
	public enum AndOr {
		AND("and"), OR("or");

		private final String meta;

		private AndOr(final String meta) {
			this.meta = meta;
		}

		public String asMeta() {
			return meta;
		}
	}

	/**
	 * @author loudyn.
	 */
	public enum MatchType {
		EQ("="), NE("<>"), LIKE("like"), GT(">"), LT("<"), GE(">="), LE("<="), IN("in"), NI("not in"),
		NULL("is"), NTNULL("is not"), STARTWITH("start"), ENDWITH("end");

		private final String meta;

		private MatchType(final String meta) {
			this.meta = meta;
		}

		public String asMeta() {
			return meta;
		}
	}

	private final StringBuilder query = new StringBuilder();
	private final Map<String, Object> values = new HashMap<String, Object>();

	private int lengthOfTableName = 0;
	private boolean insertedWhere = false;

	public boolean isInsertedWhere() {
		return insertedWhere;
	}

	public void setInsertedWhere(boolean insertedWhere) {
		this.insertedWhere = insertedWhere;
	}

	/**
	 * @return
	 */
	public String getQLString() {
		if (!values.isEmpty() && !insertedWhere) {
			query.insert(lengthOfTableName, "where ");
			insertedWhere = true;
		}
		return query.toString();
	}

	/**
	 * @return
	 */
	public Map<String, Object> getValues() {
		return values;//unmodifiableMap(values);
	}

	/**
	 * @param table
	 * @return
	 */
	public Query table(String table) {
		String fromTable = table;

		if (fromTable.toLowerCase().indexOf("from") == -1) {
			fromTable = format("from %s ", fromTable);
		}

		if (!fromTable.endsWith(" ")) {
			fromTable = format("%s ", fromTable);
		}

		query.insert(0, fromTable);
		lengthOfTableName = fromTable.length();
		return this;
	}

	/**
	 * xxx is null or xxx not null
	 * 
	 * @param hql
	 * @return
	 */
	public Query more(final String hql) {
		query.append(hql);
		return this;
	}

	/**
	 * @param property
	 * @param propertyKey
	 * @param type
	 * @param propertyValue
	 * @param andOr
	 * @return
	 */
	public Query criteria(String property, String propertyKey, MatchType type, Object propertyValue, AndOr andOr) {
		if (null != propertyValue && isNotBlank(propertyValue.toString())) {

			propertyKey = transformKey(type, propertyKey);
			Object value = transformValue(type, propertyValue);

			if (!values.isEmpty()) {
				query.append(format("%s ", andOr.asMeta()));
			}

			boolean flag = true;
			if (type == MatchType.IN || type == MatchType.NI) {
				query.append(format("%s %s (:%s) ", property, type.asMeta(), propertyKey));
			}else if (type == MatchType.NULL || type == MatchType.NTNULL){
				flag = false;
				query.append(format("%s %s null ", property, type.asMeta()));
			}else if (type == MatchType.STARTWITH || type == MatchType.ENDWITH){
				type = MatchType.LIKE;
				query.append(format("%s %s :%s ", property, type.asMeta(), propertyKey));
			}else {
				query.append(format("%s %s :%s ", property, type.asMeta(), propertyKey));
			}
			
			if(flag)
				values.put(propertyKey, value);
		}
		return this;
	}

	/**
	 * @param matchType
	 * @param key
	 * @return
	 */
	private String transformKey(MatchType matchType, String key) {
		key = key.replaceAll("\\.", "_");
		if (values.containsKey(key)) {
			key = format("%s_%s", key, randomstring(4));
		}

		return key;
	}

	/**
	 * @param matchType
	 * @param value
	 * @return
	 */
	private Object transformValue(MatchType matchType, Object value) {

		switch (matchType) {
		case LIKE:
			if (value.toString().indexOf("%") == -1) {
				return format("%%%s%%", value.toString());
			}
		case STARTWITH:
			if (!value.toString().endsWith("%")) {
				return format("%s%%", value.toString());
			}
		case ENDWITH:
			if (!value.toString().startsWith("%")) {
				return format("%%%s", value.toString());
			}
		default:
			return value;
		}
	}

	private static String RANDOM_SOURCE = "0123456789ABCDEF";

	/**
	 * @param length
	 * @return
	 */
	private String randomstring(int length) {
		Random random = new Random();
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int index = random.nextInt(RANDOM_SOURCE.length());
			buf.append(RANDOM_SOURCE.charAt(index));
		}
		return buf.toString();
	}

	/**
	 * @param property
	 * @return
	 */
	public Query isnull(String property) {
		if (!values.isEmpty()) {
			query.append(format(" %s ", AndOr.AND.asMeta()));
		}
		query.append(format("%s is null ", property));
		values.put("", property);
		return this;
	}
	
	/**
	 * @param property
	 * @return
	 */
	public Query isnotnull(String property) {
		if (!values.isEmpty()) {
			query.append(format(" %s ", AndOr.AND.asMeta()));
		}
		query.append(format("%s is not null ", property));
		//values.put("", property);
		return this;
	}

	/**
	 * @param property
	 * @return
	 */
	public Query nulloreq(String property, Object propertyValue) {
		if (propertyValue == null) 
			isnull(property);
		else
			eq(property, propertyValue);
		return this;
	}

	/**
	 * @param property
	 * @param propertyValue
	 * @param andOr
	 * @return
	 */
	public Query eq(String property, Object propertyValue) {
		return criteria(property, property, MatchType.EQ, propertyValue, AndOr.AND);
	}

	/**
	 * @param property
	 * @param propertyValue
	 * @param andOr
	 * @return
	 */
	public Query ne(String property, Object propertyValue) {
		return criteria(property, property, MatchType.NE, propertyValue, AndOr.AND);
	}

	/**
	 * @param property
	 * @param propertyValue
	 * @param andOr
	 * @return
	 */
	public Query gt(String property, Object propertyValue) {
		return criteria(property, property, MatchType.GT, propertyValue, AndOr.AND);
	}

	/**
	 * @param property
	 * @param propertyValue
	 * @param andOr
	 * @return
	 */
	public Query lt(String property, Object propertyValue) {
		return criteria(property, property, MatchType.LT, propertyValue, AndOr.AND);
	}

	/**
	 * @param property
	 * @param propertyValue
	 * @param andOr
	 * @return
	 */
	public Query ge(String property, Object propertyValue) {
		return criteria(property, property, MatchType.GE, propertyValue, AndOr.AND);
	}

	/**
	 * @param property
	 * @param propertyValue
	 * @param andOr
	 * @return
	 */
	public Query le(String property, Object propertyValue) {
		return criteria(property, property, MatchType.LE, propertyValue, AndOr.AND);
	}

	/** like */
	public Query like(String property, String propertyValue) {
		return criteria(property, property, MatchType.LIKE, propertyValue, AndOr.AND);
	}
	
	public Query like(String property, String propertyValue, AndOr andOr) {
		return criteria(property, property, MatchType.LIKE, propertyValue, andOr);
	}

	/** startWith */
	public Query startWith(String property, String propertyValue) {
		return criteria(property, property, MatchType.STARTWITH, propertyValue, AndOr.AND);
	}
	
	public Query startWith(String property, String propertyValue, AndOr andOr) {
		return criteria(property, property, MatchType.STARTWITH, propertyValue, andOr);
	}
	/** endWith */
	public Query endWith(String property, String propertyValue) {
		return criteria(property, property, MatchType.ENDWITH, propertyValue, AndOr.AND);
	}
	
	public Query endWith(String property, String propertyValue, AndOr andOr) {
		return criteria(property, property, MatchType.ENDWITH, propertyValue, andOr);
	}
	/**
	 * @param property
	 * @param propertyValue
	 * @param andOr
	 * @return
	 */
	public Query in(String property, Object propertyValue) {
		return criteria(property, property, MatchType.IN, propertyValue, AndOr.AND);
	}

	/**
	 * @param property
	 * @param propertyValue
	 * @param andOr
	 * @return
	 */
	public Query orIn(String property, Object propertyValue) {
		return criteria(property, property, MatchType.IN, propertyValue, AndOr.OR);
	}
	/**
	 * @param property
	 * @param propertyValue
	 * @param andOr
	 * @return
	 */
	public Query ni(String property, Object propertyValue) {
		return criteria(property, property, MatchType.NI, propertyValue, AndOr.AND);
	}

	/**
	 * @param property
	 * @param type
	 * @param value
	 * @return
	 */
	public Query or(String property, MatchType type, Object value) {
		return or(new String[] { property }, new MatchType[] { type }, new Object[] { value });
	}

	/**
	 * @param properties
	 * @param matchTypes
	 * @param values
	 * @return
	 */
	public Query or(String[] properties, MatchType[] matchTypes, Object[] values) {

		isTrue(properties.length == matchTypes.length ? matchTypes.length == values.length : false);

		List<Integer> avaliablePropertyValues = new ArrayList<Integer>();

		for (int i = 0; i < properties.length; i++) {
			Object value = values[i];

			if (null != value && isNotBlank(value.toString())) {
				avaliablePropertyValues.add(i);
			}
		}

		if (avaliablePropertyValues.isEmpty()) {
			return this;
		}

		if (avaliablePropertyValues.size() == 1) {
			int index = avaliablePropertyValues.get(0);
			Object value = transformValue(matchTypes[index], values[index]);
			return criteria(properties[index], properties[index], matchTypes[index], value, AndOr.OR);
		}

		if (!this.values.isEmpty()) {
			query.append(format("%s ", AndOr.AND.asMeta()));
		}

		StringBuilder buf = new StringBuilder();

		int cursor = 0;
		for (int index : avaliablePropertyValues) {

			String key = transformKey(matchTypes[index], properties[index]);
			Object value = transformValue(matchTypes[index], values[index]);

			boolean flag = true;
			if (matchTypes[index] == MatchType.IN || matchTypes[index] == MatchType.NI) {
				buf.append(format("(%s %s (:%s)) ", properties[index], matchTypes[index].asMeta(), key));
			}else if (matchTypes[index] == MatchType.NULL || matchTypes[index] == MatchType.NTNULL){
				flag = false;
				buf.append(format("%s %s null ", properties[index], matchTypes[index].asMeta()));
			} else {
				buf.append(format("(%s %s :%s) ", properties[index], matchTypes[index].asMeta(), key));
			}

			if(flag)
				this.values.put(key, value);

			if (cursor < avaliablePropertyValues.size() - 1) {
				buf.append(format("%s ", AndOr.OR.asMeta()));
			}

			cursor++;
		}

		query.append(format("(%s) ", buf.substring(0, buf.length() - 1)));
		return this;
	}

	/**
	 * @param orderBy
	 * @return
	 */
	public Query orderBy(final String orderBy) {
		String orderByString = orderBy;

		if (orderByString.toLowerCase().indexOf("order by") == -1) {
			orderByString = format(" order by %s ", orderByString);
		}

		query.append(orderByString);
		return this;
	}

	/**
	 * @param groupBy
	 * @return
	 */
	public Query groupBy(final String groupBy) {
		String groupByString = groupBy;

		if (groupByString.toLowerCase().indexOf("group by") == -1) {
			groupByString = format("group by %s ", groupByString);
		}

		query.append(groupByString);
		return this;
	}
	
	/** 设置转义符 */
	public static String escape(String ss, String driver) {
		ss = ss.replaceAll("'", "''");
		ss = ss.replaceAll("[\\x00]", "");
		//下面四个用于SQLSERVER
		if (driver.indexOf("sqlserver") > 0) {
			ss = ss.replaceAll("!", "!!");
			ss = ss.replaceAll("%", "!%");
			ss = ss.replaceAll("[", "![");
			ss = ss.replaceAll("]", "!]");
		}
		return ss;
	}
}
