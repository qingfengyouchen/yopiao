package com.base.jpa.query;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @author micheal cao
 * 
 * @param <T>
 */
public class Page<T> implements Serializable {
	private static final long serialVersionUID = -6939529854482165757L;
	public static final String ASC = "asc";
	public static final String DESC = "desc";

	private int pageNo = 1;
	private int pageSize = 16;

	private String orderBy;
	private String order;
	private boolean autoCount = true;
	private long totalCount = 0;

	private List<T> result = Collections.emptyList();

	public Page() {
		
	}
	
	/**
	 * @return the pageNo
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * @param pageNo
	 *            the pageNo to set
	 * @return
	 */
	public Page<T> setPageNo(int pageNo) {
		this.pageNo = pageNo;
		if (this.pageNo < 1) {
			this.pageNo = 1;
		}
		return this;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 * @return
	 */
	public Page<T> setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	/**
	 * @return the orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy
	 *            the orderBy to set
	 * @return
	 */
	public Page<T> setOrderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	/**
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * @param order
	 *            the order to set
	 * @return
	 */
	public Page<T> setOrder(String order) {
		if (order == null)
			order = "";
		String[] orders = StringUtils.split(StringUtils.lowerCase(order), ',');
		for (String orderStr : orders) {
			if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr))
				throw new IllegalArgumentException(
						"");
		}

		this.order = StringUtils.lowerCase(order);
		return this;
	}

	public boolean isOrderBySetted() {
		return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order));
	}

	/**
	 * @return the autoCount
	 */
	public boolean isAutoCount() {
		return autoCount;
	}

	/**
	 * @param autoCount
	 *            the autoCount to set
	 * @return
	 */
	public Page<T> setAutoCount(boolean autoCount) {
		this.autoCount = autoCount;
		return this;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public Page<T> setTotalCount(long totalCount) {
		this.totalCount = totalCount;
		return this;
	}

	/**
	 * @return the result
	 */
	public List<T> getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 * @return
	 */
	public Page<T> setResult(List<T> result) {
		this.result = result;
		return this;
	}

	public long getTotalPages() {
		if (totalCount < 0)
			return 0;

		long count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	public int getFirst() {
		return ((pageNo - 1) * pageSize) + 1;
	}

	public boolean getHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}

	public int getNextPage() {
		if (getHasNext()) {
			return pageNo + 1;
		}
		return pageNo;
	}

	public boolean getHasPrev() {
		return (pageNo - 1 >= 1);
	}

	public int getPrevPage() {
		if (getHasPrev()) {
			return pageNo - 1;
		}
		return pageNo;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
