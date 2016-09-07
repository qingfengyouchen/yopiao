package com.base.jpa;

import com.base.jpa.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@NoRepositoryBean
public interface MyJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

	List findTop(int top, String qlString, Object... values);

	List findTop(int top, String qlString, Map<String, Object> values);

	List findTop(int top, Class beanClass, String sql, Object... values);

	List findTop(int top, Class beanClass, String sql, Map<String, Object> values);

	List find(String qlString, Object... values);

	List find(String qlString, Map<String, Object> values);

	void queryPage(Page page, String queryString, Map<String, Object> values, String ...countHql);

	void queryPage(Page page, String queryString, Object ...values);

	void querySQLPage(Page page, Class beanClass, String sql,
							 Map<String, Object> values, String ...countSql);

	List<String> findString(String qlString, Map<String, Object> values);
	List<String> findStringBySql(String sql, Map<String, Object> values);

	List<String> findString(String qlString, Object ...values);
	List<String> findStringBySql(String sql, Object ...values);

	List<Integer> findInteger(String qlString, Map<String, Object> values);
	List<Integer> findIntegerBySql(String sql, Map<String, Object> values);

	List<Integer> findInteger(String qlString, Object ...values);
	List<Integer> findIntegerBySql(String sql, Object ...values);

	List<Byte> findByte(String qlString, Map<String, Object> values);
	List<Byte> findByteBySql(String sql, Map<String, Object> values);

	List<Byte> findByte(String qlString, Object ...values);
	List<Byte> findByteBySql(String sql, Object ...values);

	Object getObject(String qlString, Map<String, Object> values);

	Object getObject(String qlString, Object... values);

	Object getObjectBySQL(String qlString, Map<String, Object> values);

	Object getObjectBySQL(String qlString, Object... values);

	T getObjectBySQL(Class beanClass, String qlString, Map<String, Object> values);

	T getObjectBySQL(Class beanClass, String qlString, Object ...values);

	int executeUpdate(String qlString, Object... values);

	int executeUpdate(String qlString, Map<String, Object> values);

	int executeSQLUpdate(String qlString, Object... values);

	int executeSQLUpdate(String qlString, Map<String, Object> values);

	List querySQL(Class beanClass, String sql, Map<String, Object> values);

	List querySQL(Class beanClass, String sql, Object ...values);

	void refresh(Object entity);
}
