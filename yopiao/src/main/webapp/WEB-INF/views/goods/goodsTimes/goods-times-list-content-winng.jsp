<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
		<c:forEach items="${page.result}" var="entity">
			<tr>
				<td style="text-align: center;">${entity.times}&nbsp;</td>
				<td style="text-align: left;">
					${entity.goodsName}
					<c:if test="${not empty entity.goodsTip}"><span class="tip">${entity.goodsTip}</span></c:if>
				</td>
				<td style="text-align: center;">${entity.totalTimes}&nbsp;</td>
				<td style="text-align: center;">${entity.winngUser.nickName}&nbsp;</td>
				<c:choose>
					<c:when test="${empty entity.exchangeState || entity.exchangeState==0}">
	                    <td style="text-align: center;">未领奖&nbsp;</td>
	               </c:when>
	               <c:when test="${entity.exchangeState==1 }">
	                    <td style="text-align: center;">已积分兑换&nbsp;</td>
	               </c:when>
	               <c:when test="${entity.exchangeState==2 }">
	                    <td style="text-align: center;">未确认地址&nbsp;</td>
	               </c:when>
	               	<c:when test="${entity.exchangeState==3 }">
	                    <td style="text-align: center;">未发货&nbsp;</td>
	               </c:when>
	               <c:when test="${entity.exchangeState==4 }">
	                    <td style="text-align: center;">已发货&nbsp;</td>
	               </c:when>
	               <c:when test="${entity.exchangeState==5 }">
	                    <td style="text-align: center;">已收货&nbsp;</td>
	               </c:when>
	               <c:when test="${entity.exchangeState==6 }">
	                    <td style="text-align: center;">已晒单&nbsp;</td>
	               </c:when>
               </c:choose>
				<!-- 
				<td style="text-align: center;">${winngStateMap[entity.winngState]}&nbsp;</td>
				 -->
				<td style="text-align: center;">
					<shiro:hasPermission name="goods:winner:view">
						<a class="btn btn-success aedit" href="javascript:;" id="${entity.id }"><i class="fa fa-eye"></i>查看</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
	<tr>
		<td colspan="10" style="text-align: right;">
			<jsp:include page="/common/ajax-page.jsp"></jsp:include>
		</td>
	</tr>