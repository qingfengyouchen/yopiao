<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<c:set var="shareGoodsState"><%=Const.ShareGoodsState.UNAUDIT%></c:set>
		<c:forEach items="${page.result}" var="entity">
			<tr>
				<td style="text-align: center;"><input type="checkbox" name="ids" value="${entity.id }"/></td>
				<td style="text-align: center;">${entity.title}&nbsp;</td>
				<td style="text-align: center;word-break:break-all;">${entity.content}&nbsp;</td>
				<td style="text-align: center;">${entity.userNickName}&nbsp;</td>
				<td style="text-align: center;">${shareGoodsMap[entity.state]}&nbsp;</td>
				<td style="text-align: center;">${entity.goodsTimesName}&nbsp;</td>
				<td style="text-align: center;">
					<shiro:hasPermission name="goods:shareGoods:edit">
						<a class="btn btn-success aedit" href="javascript:;" id="${entity.id }"><i class="fa fa-edit"></i>查看</a> 
						<!--  ${shareGoodsMap[entity.state] == shareGoodsState ? "审核" : "查看"}</a>-->
						<a class="btn btn-success adelete" id="${entity.id}" href="javascript:;"><i class="fa fa-times"></i> 删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
	<tr>
		<td colspan="8" style="text-align: right;">
			<jsp:include page="/common/ajax-page.jsp"></jsp:include>
		</td>
	</tr>