<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
		<c:forEach items="${page.result}" var="entity">
			<tr>
				<td style="text-align: center;"><input type="checkbox" name="ids" value="${entity.id }"/></td>
				<td style="text-align: center;"><img src="${activityRootUri}/${entity.thumbImgUrl}" style="width: 108px; height: 80px;">&nbsp;</td>
				<td style="text-align: center;">${entity.title}&nbsp;</td>
				<!--  
				<td style="text-align: center;">${entity.activityTime}&nbsp;</td>
				<td style="text-align: center;">${entity.maxJoinAmount}&nbsp;</td>
				<td style="text-align: center;">${entity.hasJoinAmount}&nbsp;</td>
				<td style="text-align: center;">${entity.address}&nbsp;</td>
				<td style="text-align: center;">${activityState[entity.state]}&nbsp;</td>
				-->
				<td style="text-align: center;">
					<shiro:hasPermission name="service:activity:edit">
						<c:if test="${entity.state==1}">
						<a class="btn btn-success aedit" href="javascript:;" id="${entity.id }"><i class="fa fa-pencil"></i> 修改</a>
						<!-- 
							<a class="btn btn-success" href="javascript:updateState(${entity.id})" id="${entity.id}" href="javascript:;"><i class="fa fa-times-circle-o"></i>结束活动</a>
						-->
						</c:if>
						<a class="btn btn-success adelete" id="${entity.id}" href="javascript:;"><i class="fa fa-times"></i> 删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
	<tr>
		<td colspan="10" style="text-align: right;">
			<jsp:include page="/common/ajax-page.jsp"></jsp:include>
		</td>
	</tr>