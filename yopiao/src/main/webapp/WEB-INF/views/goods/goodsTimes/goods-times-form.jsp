<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>编辑商品</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
	<link rel="stylesheet" type="text/css" href="${ctx}/static/js/webuploader/webuploader.css">
</head>

<body>
	<div class="row">
		<div class="col-sm-12">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>
						商品期号管理 > 查看详细信息
					</h5>
				</div>
				<div class="ibox-content">
					<form:form id="inputForm" modelAttribute="entity"  method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<div class="form-group">
							<label class="col-sm-2 control-label">期号:</label>
							<div class="col-sm-6">
								<label class="col-sm control-label">${entity.times}</label>
							</div>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">商品名称:</label>
							<div class="col-sm-6">
								<label class="col-sm control-label">${entity.goodsName}</label>
							</div>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">信息:</label>
							<div class="col-sm-6">
								<label class="col-sm control-label">${entity.goodsTip}</label>
							</div>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">是否10元夺宝:</label>
							<div class="col-sm-6">
								<label class="col-sm control-label">${booleanMap[entity.isTenYuan]}</label>
							</div>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">总需人次:</label>
							<div class="col-sm-6">
								<label class="col-sm control-label">${entity.totalTimes}</label>
							</div>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">已购买人次:</label>
							<div class="col-sm-6">
								<label class="col-sm control-label">${entity.totalBuyTimes}</label>
							</div>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">夺宝进度:</label>
							<div class="col-sm-6">
								<label class="col-sm control-label">${entity.snatchProgress}%</label>
							</div>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">揭晓时间:</label>
							<div class="col-sm-6">
								<label class="col-sm control-label"><fmt:formatDate value="${entity.openTime}" pattern="yyyy-MM-dd HH:mm:ss"/></label>
							</div>
						</div>
						<div class="hr-line-dashed"></div>
						<c:if test="${entity.state == goodsTimesStateOver}">
							<div class="form-group">
								<label class="col-sm-2 control-label">幸运号码:</label>
								<div class="col-sm-6">
									<label class="col-sm control-label">${entity.luckNum}</label>
								</div>
							</div>
							<div class="hr-line-dashed"></div>
							<div class="form-group">
								<label class="col-sm-2 control-label">中奖用户名:</label>
								<div class="col-sm-6">
									<label class="col-sm control-label">${entity.winngUserIdentity}</label>
								</div>
							</div>
							<div class="hr-line-dashed"></div>
							<div class="form-group">
								<label class="col-sm-2 control-label">中奖用户昵称:</label>
								<div class="col-sm-6">
									<label class="col-sm control-label">${entity.winngUserName}</label>
								</div>
							</div>
							<div class="hr-line-dashed"></div>
							<div class="form-group">
								<label class="col-sm-2 control-label">购买次数:</label>
								<div class="col-sm-6">
									<label class="col-sm control-label">${entity.winngUserBuyTimes}</label>
								</div>
							</div>
							<div class="hr-line-dashed"></div>
							<div class="form-group">
								<label class="col-sm-2 control-label">物流名字:</label>
								<div class="col-sm-6">
									<label class="col-sm control-label"></label>
								</div>
							</div>
							<div class="hr-line-dashed"></div>
							<div class="form-group">
								<label class="col-sm-2 control-label">物流订单号:</label>
								<div class="col-sm-6">
									<label class="col-sm control-label"></label>
								</div>
							</div>
							<div class="hr-line-dashed"></div>
							<div class="form-group">
								<label class="col-sm-2 control-label">收货地址:</label>
								<div class="col-sm-6">
									<label class="col-sm control-label"></label>
								</div>
							</div>
							<div class="hr-line-dashed"></div>
							<div class="form-group">
								<label class="col-sm-2 control-label">中奖状态:</label>
								<div class="col-sm-6">
									<label class="col-sm control-label">${winngState[entity.winngState]}</label>
								</div>
							</div>
							<div class="hr-line-dashed"></div>
							<div class="form-group">
								<label class="col-sm-2 control-label">是否已晒单:</label>
								<div class="col-sm-6">
									<label class="col-sm control-label">${booleanMap[entity.hasShareGoods]}</label>
								</div>
							</div>
							<div class="hr-line-dashed"></div>
						</c:if>
						<div class="form-group">
							<div class="col-sm-6 col-sm-offset-2">
								<button class="btn btn-w-m btn-success" id="btnBack" type="button">返回</button>
							</div>
						</div>
						<jsp:include page="/common/foot-form-js.jsp" flush="true"></jsp:include>
					</form:form>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
