<%@ page contentType="text/html;charset=utf-8" %>
<%@include file="/common/taglibs.jsp" %>
<input type="hidden" name="pageNo" id="pageNo" value="${page.pageNo}"/>
<input type="hidden" name="orderBy" id="orderBy" value="${page.orderBy}"/>
<input type="hidden" name="order" id="order" value="${page.order}" />
<div id="page-bar">
	共<span class="red">${page.totalCount }</span>条记录，第${page.pageNo }页, 共有 ${page.totalPages}页&nbsp;
		<c:if test="${page.totalPages > 1 && page.pageNo!=1}"><a href="javascript:;" value="1">首页</a>&nbsp;</c:if>
		<c:if test="${page.hasPrev}"><a href="javascript:;" value="${page.prevPage}">上一页</a>&nbsp;</c:if>
		<c:if test="${page.hasNext}"><a href="javascript:;" value="${page.nextPage }">下一页</a>&nbsp;</c:if>
		<c:if test="${page.totalPages > 1 && page.pageNo!=page.totalPages}"><a href="javascript:;" value="${page.totalPages }">末页</a></c:if>
</div>
