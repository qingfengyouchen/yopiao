<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
		<c:forEach items="${page.result}" var="entity">
			<tr>
				<td style="text-align: center;"><input type="checkbox" name="ids" value="${entity.id }"/></td>
				<td style="text-align: center;">${entity.member.user.userName}&nbsp;</td>
				<td style="text-align: center;">${entity.createTime}&nbsp;</td>
				<td style="text-align: center;">${entity.message}&nbsp;</td>
				<c:choose>
				    <c:when test="${entity.isread == 1}">
                        <td style="text-align: center;">已读&nbsp;</td>
				   </c:when>
				   <c:otherwise> 
                        <td style="text-align: center;">
                                <a class="btn btn-success adelete read" href="javascript:;" id="${entity.id }"><i class="fa fa-pencil"></i> 阅读</a>
                        </td>
				   </c:otherwise>
				</c:choose>
			</tr>
		</c:forEach>
	<tr>
		<td colspan="12" style="text-align: right;">
			<jsp:include page="/common/ajax-page.jsp"></jsp:include>
		</td>
	</tr>