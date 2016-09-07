<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
		<c:forEach items="${page.result}" var="entity">
			<tr>
				<td style="text-align: center;"><input type="checkbox" name="ids" value="${entity.id }"/></td>
				<td style="text-align: center;">${entity.user.nickName}&nbsp;</td>
				<td style="text-align: center;">${entity.user.userName}&nbsp;</td>
				<td style="text-align: center;">${entity.user.mobileNo}&nbsp;</td>
				<td style="text-align: center;">${entity.balance}&nbsp;</td>
				<td style="text-align: center;">${entity.jifen}&nbsp;</td>
				<td style="text-align: center;">${entity.memberLevel.name}&nbsp;</td>
				<td style="text-align: center;">${entity.trueName}&nbsp;</td>
				<td style="text-align: center;">${booleanMap[entity.canSpeak]}&nbsp;</td>
				<td style="text-align: center;">${booleanMap[entity.canSnatch]}&nbsp;</td>
				<td style="text-align: center;">${stateMap[entity.state]}&nbsp;</td>
				<td style="text-align: center;">
					<shiro:hasPermission name="member:member:edit">
						<a class="btn btn-success aedit" href="javascript:;" id="${entity.id }"><i class="fa fa-pencil"></i> 修改</a>
						<a class="btn btn-success adelete" id="${entity.id}" href="javascript:;"><i class="fa fa-times"></i> 删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
	<tr>
		<td colspan="12" style="text-align: right;">
			<jsp:include page="/common/ajax-page.jsp"></jsp:include>
		</td>
	</tr>