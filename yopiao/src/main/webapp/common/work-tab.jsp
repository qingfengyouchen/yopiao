<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<div class="navbar">
	<div class="navbar-inner">
		<div class="brand">待处理 ></div>
		<ul class="nav">
			<c:if test="${canViewPublishZsfp}">
				<li class="${isSelectFangpan ? "active" : ""}">
					<a href="${ctx}<%=Const.AUDIT_FANGPAN_LIST_URL%>">出售/出租委托</a>
				</li>
				<li class="${isSelectBuyRental ? "active" : ""}">
					<a href="${ctx}<%=Const.AUDIT_BUYRENTAL_LIST_URL%>">求购/求租委托</a>
				</li>
			</c:if>

			<shiro:hasPermission name="sales:saler:edit">
				<li class="${isSelectSaler ? "active" : ""}">
					<a href="${ctx}<%=Const.AUDIT_SALER_LIST_URL%>">审核经纪人</a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="zp:zhaopin:audit">
				<li class="${isSelectZp ? "active" : ""}">
					<a href="${ctx}<%=Const.AUDIT_ZHAOPIN_LIST_URL%>">审核招聘信息</a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="tz:tzMarket:audit">
				<li class="${isSelectTz ? "active" : ""}">
					<a href="${ctx}<%=Const.AUDIT_TZ_LIST_URL%>">审核跳蚤信息</a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="jjfw:jjfw:audit">
				<li class="${isSelectJjfw ? "active" : ""}">
					<a href="${ctx}<%=Const.AUDIT_JJFW_LIST_URL%>">审核居家服务</a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="yhhd:yhhd:audit">
				<li class="${isSelectYhhd ? "active" : ""}">
					<a href="${ctx}<%=Const.AUDIT_YHHD_LIST_URL%>">审核商家优惠活动</a>
				</li>
			</shiro:hasPermission>
		</ul>
	</div>
</div>

