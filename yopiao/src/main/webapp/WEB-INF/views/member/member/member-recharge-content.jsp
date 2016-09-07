<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
		<c:forEach items="${page.result}" var="entity">
			<tr>
				<td style="text-align: center;">${entity.user.nickName}&nbsp;</td>
				<td style="text-align: center;">${entity.user.userName}&nbsp;</td>
				<c:choose>
				<c:when test="${entity.payWay==1 }">
                    <td style="text-align: center;">支付宝&nbsp;</td>
               </c:when>
               <c:when test="${entity.payWay==2 }">
                    <td style="text-align: center;">微信支付&nbsp;</td>
               </c:when>
               <c:when test="${entity.payWay==3 }">
                    <td style="text-align: center;">红包&nbsp;</td>
               </c:when>
               </c:choose>
				<td style="text-align: center;">${entity.money}&nbsp;</td>
				<td style="text-align: center;">${entity.payTime}&nbsp;</td>
			</tr>
		</c:forEach>
	<tr>
		<td colspan="12" style="text-align: right;">
			<jsp:include page="/common/ajax-page.jsp"></jsp:include>
		</td>
	</tr>