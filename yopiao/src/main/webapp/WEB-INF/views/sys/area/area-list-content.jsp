<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
		<c:forEach items="${page.result}" var="entity">
			<tr>
				<td style="text-align: center;"><input type="checkbox" name="ids" value="${entity.id }"/></td>
				<td style="text-align: left;"><span style="margin-left: ${entity.level * 30}px;">${entity.name}</span></td>
				<td style="text-align: center;">${entity.code}&nbsp;</td>
				<td style="text-align: center;">
					<shiro:hasPermission name="sys:area:edit">
						<a class="btn btn-small aedit" href="javascript:;" id="${entity.id }"><i class="icon-pencil"></i> 修改</a>
						<a class="btn btn-small adelete" id="${entity.id}" href="javascript:;"><i class="icon-remove"></i> 删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
	<tr>
		<td colspan="8" style="text-align: right;">
			<jsp:include page="/common/ajax-page.jsp"></jsp:include>
		</td>
	</tr>