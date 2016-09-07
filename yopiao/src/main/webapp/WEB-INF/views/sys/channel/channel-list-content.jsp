<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<c:forEach items="${page.result}" var="entity">
	<tr>
		<td style="text-align: center;"><input type="checkbox" name="ids" value="${entity.id }"/></td>
		<td style="text-align: center;"><img src="${channelImgRootURI}/${entity.thumbImg}" style="width:100px;height:100px;"></td>
		<td style="text-align: center;">${entity.name}</td>
		<td style="text-align: center;">${entity.sortNum}</td>
		<td style="text-align: center;">
			<shiro:hasAnyPermissions name="jjfw:category:edit,query:query:edit,article:article:edit">
				<a class="btn btn-small aedit" href="javascript:;" id="${entity.id }"><i class="icon-pencil"></i> 修改</a>
				<a class="btn btn-small adelete" id="${entity.id}" href="javascript:;"><i class="icon-remove"></i> 删除</a>
			</shiro:hasAnyPermissions>
		</td>
	</tr>
</c:forEach>
<tr>
	<td colspan="8" style="text-align: right;">
		<jsp:include page="/common/ajax-page.jsp"></jsp:include>
	</td>
</tr>