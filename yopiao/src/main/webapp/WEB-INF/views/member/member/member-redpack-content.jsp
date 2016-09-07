<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
		<c:forEach items="${page.result}" var="entity">
			<tr>
				<td style="text-align: center;">${entity.user.nickName}&nbsp;</td>
				<td style="text-align: center;">${entity.user.userName}&nbsp;</td>
				<td style="text-align: center;">${entity.total}&nbsp;</td>
				<td style="text-align: center;">${entity.createTime}&nbsp;</td>
			</tr>
		</c:forEach>
	<tr>
		<td colspan="12" style="text-align: right;">
			<jsp:include page="/common/ajax-page.jsp"></jsp:include>
		</td>
	</tr>