package com.base.jpa;

import com.base.jpa.query.Page;
import com.base.modules.util.SimpleUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Micheal
 *
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public class MyConcreteJpaRepository<T, ID extends Serializable>
		extends SimpleJpaRepository<T, ID> implements MyJpaRepository<T, ID> {
	public Logger logger = LoggerFactory.getLogger(getClass());
	private final EntityManager entityManager;  
	//private final JpaEntityInformation<T, ?> entityInformation;
	
	/*public MyConcreteJpaRepository(Class<T> domainClass, EntityManager em) {
		this(JpaEntityInformationSupport.getMetadata(domainClass, em), em);
	}

	public MyConcreteJpaRepository(final JpaEntityInformation<T, ?> entityInformation, final EntityManager entityManager) {
	    super(entityInformation, entityManager);  
	    Assert.notNull(entityInformation);
		Assert.notNull(entityManager);
		this.entityInformation = entityInformation;
	    this.entityManager = entityManager;  
	}*/

	public MyConcreteJpaRepository(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		entityManager=em;
	}

	public MyConcreteJpaRepository(final JpaEntityInformation<T, ?> entityInformation,
								   final EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List findTop(int top, String qlString, Object... values) {
		Query query = createQuery(qlString, values);
		return query.setMaxResults(top).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List findTop(int top, String qlString, Map<String, Object> values) {
		Query query = createQuery(qlString, values);
		return query.setMaxResults(top).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public  List findTop(int top, Class beanClass, String sql, Object... values) {
		Query query = createSQLQuery(sql, values);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(beanClass));
		return query.setMaxResults(top).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List findTop(int top, Class beanClass, String sql, Map<String, Object> values) {
		Query query = createSQLQuery(sql, values);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(beanClass));
		return query.setMaxResults(top).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List find(String qlString, Object... values) {
		Query query = createQuery(qlString, values);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List find(String qlString, Map<String, Object> values) {
		Query query = createQuery(qlString, values);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> findString(String qlString, Map<String, Object> values) {
		Query query = createQuery(qlString, values);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> findStringBySql(String sql, Map<String, Object> values) {
		Query query = createSQLQuery(sql, values);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> findString(String qlString, Object ...values) {
		Query query = createQuery(qlString, values);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> findStringBySql(String sql, Object ...values) {
		Query query = createSQLQuery(sql, values);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> findInteger(String qlString, Map<String, Object> values) {
		Query query = createQuery(qlString, values);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> findIntegerBySql(String sql, Map<String, Object> values) {
		Query query = createSQLQuery(sql, values);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> findInteger(String qlString, Object ...values) {
		Query query = createQuery(qlString, values);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> findIntegerBySql(String sql, Object ...values) {
		Query query = createSQLQuery(sql, values);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Byte> findByte(String qlString, Map<String, Object> values) {
		Query query = createQuery(qlString, values);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Byte> findByteBySql(String sql, Map<String, Object> values) {
		Query query = createSQLQuery(sql, values);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Byte> findByte(String qlString, Object ...values) {
		Query query = createQuery(qlString, values);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Byte> findByteBySql(String sql, Object ...values) {
		Query query = createSQLQuery(sql, values);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getObject(String qlString, Map<String, Object> values) {
		Query query = createQuery(qlString, values);
		return getOneResult(query);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getObject(String qlString, Object ...values) {
		Query query = createQuery(qlString, values);
		return getOneResult(query);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getObjectBySQL(String qlString, Map<String, Object> values) {
		Query query = createSQLQuery(qlString, values);
		return getOneResult(query);
	}

	private Object getOneResult(Query query) {
		query.setMaxResults(1);
		List<Object> list = query.getResultList();
		return SimpleUtils.isNullList(list) ? null : list.get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getObjectBySQL(String qlString, Object ...values) {
		Query query = createSQLQuery(qlString, values);
		return getOneResult(query);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getObjectBySQL(Class beanClass, String qlString, Map<String, Object> values) {
		Query query = createSQLQuery(qlString, values);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(beanClass));
		return (T) getOneResult(query);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getObjectBySQL(Class beanClass, String qlString, Object ...values) {
		Query query = createSQLQuery(qlString, values);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(beanClass));
		return (T) getOneResult(query);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void queryPage(Page page, String queryString,
						  Map<String, Object> values, String ...countHql) {
		if(countHql == null || countHql.length<1)
			setPageTotalCount(page, queryString, values);
		else{
			 Long count = (Long) createQuery(countHql[0], values).getResultList().get(0);
			 page.setTotalCount(count);
		}

		String orderHql = createOrderBy(page, queryString);
		Query query = createQuery(String.format("%s%s", queryString, orderHql), values);

		page.setResult(
						setPageParameter(query, page).getResultList() );
	}

	public void queryPage(Page page, String queryString, Object ...values){
		setPageTotalCount(page, queryString, values);
		String orderHql = createOrderBy(page, queryString);
		Query query = createQuery(String.format("%s%s", queryString, orderHql), values);

		page.setResult(
				setPageParameter(query, page).getResultList() );
	}

	@Override
	@SuppressWarnings("unchecked")
	public void querySQLPage(Page page, Class beanClass, String sql,
							 Map<String, Object> values, String ...countSql) {
		if(countSql == null || countSql.length < 1)
			setPageTotalCountInSQL(page, sql, values);
		else{
			BigInteger bi = (BigInteger)createSQLQuery(countSql[0], values).getResultList().get(0);
			page.setTotalCount(bi.longValue());
		}

		String orderHql = createOrderBy(page, sql);
		Query query = createSQLQuery(String.format("%s%s", sql, orderHql), values);

		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(beanClass));

		page.setResult(setPageParameter(query, page).getResultList());
	}

	public List querySQL(Class beanClass, String sql, Map<String, Object> values) {
		Query query = createSQLQuery(sql, values);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(beanClass));

		return query.getResultList();
	}

	public List querySQL(Class beanClass, String sql, Object ...values) {
		Query query = createSQLQuery(sql, values);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(beanClass));

		return query.getResultList();
	}

	@Override
	@Transactional
	public int executeUpdate(String qlString, Object... values){
		Query query = createQuery(qlString, values);
		Integer result = query.executeUpdate();
		return result == null ? 0 : result;
	}

	@Override
	@Transactional
	public int executeUpdate(String qlString, Map<String, Object> values){
		Query query = createQuery(qlString, values);
		Integer result = query.executeUpdate();
		return result == null ? 0 : result;
	}

	@Override
	@Transactional
	public int executeSQLUpdate(String sql, Object... values){
		Query query = createSQLQuery(sql, values);
		Integer result = query.executeUpdate();
		return result == null ? 0 : result;
	}

	@Override
	@Transactional
	public int executeSQLUpdate(String sql, Map<String, Object> values){
		Query query = createSQLQuery(sql, values);
		Integer result = query.executeUpdate();
		return result == null ? 0 : result;
	}

	/*public void emptyShallowCache() {
		entityManager.flush();
		entityManager.clear();
	}*/

	@Override
	public void refresh(Object entity) {
		if(entity != null)
			entityManager.refresh(entity);
	}

	/**
	 * @param qlString starting from 1, ?1, ?2, ?3...
	 * @param values
	 * @return
	 */
	private Query createQuery(String qlString, Object... values) {
		Assert.hasText(qlString, "queryString must has text");
		Query query = entityManager.createQuery(qlString);
		if (null != values) {
			for (int i = 1; i <= values.length; i++) {
				query.setParameter(i, values[i-1]);
			}
		}
		return query;
	}
	
	private Query createQuery(String qlString, Map<String, Object> values) {
		Assert.hasText(qlString, "queryString must has text");

		Query query = entityManager.createQuery(qlString);
		if (values != null) {
			for (Map.Entry<String, Object> entry : values.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
			
		}
		return query;
	}

	/**
	 * @param sql starting from 1, ?1, ?2, ?3...
	 * @param values
	 * @return
	 */
	private Query createSQLQuery(String sql, Object... values) {
		Assert.hasText(sql, "sql must has text");
		Query query = entityManager.createNativeQuery(sql);
		if (null != values) {
			for (int i = 1; i <= values.length; i++) {
				query.setParameter(i, values[i-1]);
			}
		}
		return query;
	}

	private Query createSQLQuery(String sql, Map<String, Object> values) {
		Assert.hasText(sql, "sql must has text");

		Query query = entityManager.createNativeQuery(sql);
		if (values != null) {
			for (Map.Entry<String, Object> entry : values.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}

		}
		return query;
	}

	/**
	 * @param <T>
	 * @param page
	 * @param qlString
	 * @param values
	 */
	private <T> void setPageTotalCount(Page page, String qlString, Map<String, Object> values) {
		if (page.isAutoCount()) {
			long count = countQLResult(qlString, values);
			page.setTotalCount(count);
		}
	}

	private <T> void setPageTotalCount(Page page, String qlString, Object ...values) {
		if (page.isAutoCount()) {
			long count = countQLResult(qlString, values);
			page.setTotalCount(count);
		}
	}

	private <T> void setPageTotalCountInSQL(Page page,
											String sql, Map<String, Object> values) {
		if (page.isAutoCount()) {
			long count = countSQLResult(sql, values);
			page.setTotalCount(count);
		}
	}
	
	/**
	 * @param qlString
	 * @param values
	 * @return
	 */
	private Long countQLResult(String qlString, Map<String, Object> values) {
		if (isContainsDistinct(qlString)) {
			String countQL = buildDistinctCountHql(qlString);
			return (Long) createQuery(countQL, values).getResultList().get(0);
		}

		String countHql = buildCountHql(qlString);
		return (Long) createQuery(countHql, values).getResultList().get(0);
	}

	private Long countQLResult(String qlString, Object ...values) {
		if (isContainsDistinct(qlString)) {
			String countQL = buildDistinctCountHql(qlString);
			return (Long) createQuery(countQL, values).getResultList().get(0);
		}

		String countHql = buildCountHql(qlString);
		return (Long) createQuery(countHql, values).getResultList().get(0);
	}

	private Long countSQLResult(String sql, Map<String, Object> values) {
		if (isContainsDistinct(sql)) {
			sql = buildDistinctCountHql(sql);
		}else{
			sql = buildCountHql(sql);
		}

		BigInteger bi = (BigInteger)createSQLQuery(sql, values).getResultList().get(0);
		return bi.longValue();
	}
	
	/**
	 * @param qlString
	 * @return
	 */
	private boolean isContainsDistinct(String qlString) {

		if (qlString.indexOf("distinct") == -1) {
			return qlString.contains("DISTINCT");
		}

		return true;
	}
	
	/**
	 * @param qlString
	 * @return
	 */
	private String buildDistinctCountHql(String qlString) {
		String selectItems = findSelectItems(qlString);
		String fromHql = buildFromHql(qlString);
		return String.format("SELECT COUNT(DISTINCT %s) %s", selectItems, fromHql);
	}
	
	/**
	 * @param hql
	 * @return
	 */
	private String buildFromHql(String hql) {
		String fromHql = hql;
		if (fromHql.indexOf("from") == -1) {
			fromHql = StringUtils.substringAfter(fromHql, "FROM");
		}
		else {
			fromHql = StringUtils.substringAfter(fromHql, "from");
		}

		if (fromHql.indexOf("order by") == -1) {
			fromHql = StringUtils.substringBefore(fromHql, "ORDER BY");
		}
		else {
			fromHql = StringUtils.substringBefore(fromHql, "order by");
		}

		return "FROM" + fromHql;
	}
	
	/**
	 * @param hql
	 * @return
	 */
	private String findSelectItems(String hql) {
		Matcher m = DISTINCT_PATTERN.matcher(hql);
		if (m.matches()) {
			return m.group(SELECT_ITEMS_GROUP);
		}
		throw new UnsupportedOperationException("can't count this distintc hql");
	}
	
	/**
	 * @param hql
	 * @return
	 */
	private String buildCountHql(String hql) {
		String fromHql = buildFromHql(hql);
		return String.format("SELECT COUNT(*) %s", fromHql);
	}
	
	protected <T> String createOrderBy(final Page page, String queryString) {
		Assert.notNull(page, "page must not null");

		StringBuilder orderBy = new StringBuilder();
		if (page.isOrderBySetted()) {
			String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
			String[] orderArray = StringUtils.split(page.getOrder(), ',');

			Assert.isTrue(orderByArray.length == orderArray.length, "orderBy.length must equal order.length");

			for (int i = 0; i < orderByArray.length; i++) {
				if (i > 0)
					orderBy.append(",");
				orderBy.append(String.format("%s %s", orderByArray[i], orderArray[i]));
			}

			if (queryString.toLowerCase().indexOf("order by") < 0) {
				orderBy.insert(0, " order by ");
			}
			else {
				orderBy.insert(0, ",");
			}
		}

		return orderBy.toString();
	}
	
	/**
	 * @param <T>
	 * @param query
	 * @param page
	 * @return
	 */
	protected <T> Query setPageParameter(Query query, Page page) {
		
		query.setFirstResult(page.getFirst() - 1);
		
		if (page.getPageSize() != -1) {
			query.setMaxResults(page.getPageSize());
			return query;
		}

		query.setMaxResults((int) page.getTotalCount());
		return query;
	}
	
	private static final Pattern DISTINCT_PATTERN = Pattern.compile("select?(\\s+distinct{1}\\s{1}(.+)\\s{1})from{1}\\s+.+",
			Pattern.CASE_INSENSITIVE);
	private static final int SELECT_ITEMS_GROUP = 2;
}
