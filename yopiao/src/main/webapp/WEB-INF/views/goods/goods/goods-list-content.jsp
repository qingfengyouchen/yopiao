<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<c:set var="salesState"><%=Const.GoodsState.ENABLE%></c:set>
		<c:forEach items="${page.result}" var="entity">
			<tr>
				<td style="text-align: center;"><input type="checkbox" name="ids" value="${entity.id }"/></td>
				<td style="text-align: center;"><img src="${goodsRootUri}/${entity.thumbnail}" style="width: 108px; height: 80px;"></td>
				<td style="text-align: center;">${entity.id}</td>
				<td style="text-align: left;">
					${entity.name}
				</td>
				<td style="text-align: center;">${entity.totalTimes}</td>
				<td style="text-align: center;">${entity.goodsCategory.name}&nbsp;</td>
				<td style="text-align: center;">${goodsStateMap[entity.state]}&nbsp;</td>
				<td style="text-align: center;">
					<shiro:hasPermission name="goods:GoodsCategory:edit">
						<a class="btn btn-success aedit" href="javascript:;" id="${entity.id }"><i class="fa fa-pencil"></i> 修改</a>
						<!--  
						<a class="btn btn-success adelete" id="${entity.id}" href="javascript:;"><i class="fa fa-times"></i> 删除</a>
						-->
						<a class="btn btn-success" href="javascript: updateState('${entity.id}', '${entity.state}');"><i class="${entity.state == salesState ? "fa fa-long-arrow-down" : "fa fa-long-arrow-up"}"></i> ${entity.state == salesState ? "下架" : "上架"}</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
	<tr>
		<td colspan="8" style="text-align: right;">
			<jsp:include page="/common/ajax-page.jsp"></jsp:include>
		</td>
	</tr>