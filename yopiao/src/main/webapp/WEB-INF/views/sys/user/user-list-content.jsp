<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
		<c:forEach items="${page.result}" var="entity">
			<tr>
				<td style="text-align: center;"><input type="checkbox" name="ids" value="${entity.id }"/></td>
				<td style="text-align: center;">${entity.trueName}&nbsp;</td>
				<td style="text-align: center;">${entity.userName}</td>
				<td style="text-align: center;">${entity.nickName}&nbsp;</td>
				<td style="text-align: center;">${entity.mobileNo}&nbsp;</td>
				<td style="text-align: center;">${genderMap[entity.gender]}&nbsp;</td>
				<td style="text-align: center;">${entity.roleNames}&nbsp;</td>
				<td style="text-align: center;">${stateMap[entity.state]}&nbsp;</td>
				<td style="text-align: center;">
 					<shiro:hasPermission name="sys:user:edit">
						<a class="btn btn-success aedit" href="javascript:;" id="${entity.id }"><i class="fa fa-pencil"></i> 修改</a>
						<a class="btn btn-success adelete" id="${entity.id}" href="javascript:;"><i class="fa fa-times"></i> 删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
	<tr>
		<td colspan="9" style="text-align: right;">
			<jsp:include page="/common/ajax-page.jsp"></jsp:include>
		</td>
	</tr>