<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<c:forEach items="${list}" var="entity">
	<tr>
		<td style="text-align: center;"><input type="checkbox" name="ids" value="${entity.id }"/></td>
		<td style="text-align: center;"><img src="${topSwitchImgRootUri}/${entity.url}" style="width:150px;"></td>
		<td style="text-align: center;">${imageActionTypeMap[entity.actionType]}</td>
		<td style="text-align: center;">${entity.value}</td>
		<td style="text-align: center;">${entity.sortNum}</td>
		<td style="text-align: center;">
			<shiro:hasAnyPermissions name="sys:imageSetting:edit">
				<a class="btn btn-success aedit" href="javascript:;" id="${entity.id }"><i class="fa fa-pencil"></i> 修改</a>
				<a class="btn btn-success adelete" id="${entity.id}" href="javascript:;"><i class="fa fa-times"></i> 删除</a>
			</shiro:hasAnyPermissions>
		</td>
	</tr>
</c:forEach>