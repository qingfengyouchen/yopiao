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
				<td style="text-align: center;">${booleanMap[entity.canSpeak]}&nbsp;</td>
				<td style="text-align: center;">${booleanMap[entity.canSnatch]}&nbsp;</td>
				<td style="text-align: center;">${stateMap[entity.state]}&nbsp;</td>
				<td style="text-align: center;">
					<shiro:hasPermission name="notice:noticeMessage:view">
						<a class="btn btn-success aedit" href="javascript:;" id="${entity.id }"><i class="fa fa-pencil"></i> 留言</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="notice:noticeWithdraw:view">
						<a class="btn btn-success aview" id="${entity.id}" href="javascript:;"><i class="fa fa-search"></i> 提现</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="notice:noticeOnlySend:view">
						<a class="btn btn-success aview1" id="${entity.id}" href="javascript:;"><i class="fa fa-search"></i> 发送通知</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
	<tr>
		<td colspan="12" style="text-align: right;">
			<jsp:include page="/common/ajax-page.jsp"></jsp:include>
		</td>
	</tr>