<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
		<c:forEach items="${page.result}" var="entity">
			<tr>
				<td style="text-align: center;"><input type="checkbox" name="ids" value="${entity.id }"/></td>
				<td style="text-align: center;">${entity.member.user.userName}&nbsp;</td>
				<td style="text-align: center;">${entity.jifen}&nbsp;</td>
				<td style="text-align: center;">${entity.price}&nbsp;</td>
				<td style="text-align: center;">
				<fmt:formatNumber type="number" value="${entity.price*withDrawFee}" maxFractionDigits="2"/>
				&nbsp;</td>
				<td style="text-align: center;">${entity.createTime}&nbsp;</td>
				<c:choose>
				    <c:when test="${entity.isdone == 1}">
                        <td style="text-align: center;">已处理&nbsp;</td>
				   </c:when>
				   <c:otherwise> 
                        <td style="text-align: center;">
                            <a class="btn btn-success adelete isdone" href="javascript:;" id="${entity.id }"><i class="fa fa-pencil"></i> 处理</a>
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