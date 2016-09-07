<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>编辑中奖信息</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
	<link rel="stylesheet" type="text/css" href="${ctx}/static/js/webuploader/webuploader.css">
</head>
<body>
	<div class="row">
		<div class="col-sm-12">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>
						商品期号管理 > 编辑查看详细信息
					</h5>
				</div>
				<div class="ibox-content">
					<form:form id="inputForm" action="${ctx}/goods/goodsTimes/saveWinng" modelAttribute="entity"  method="post" class="form-horizontal">
						<input type="hidden" name="params[gid]" value="${entity.id}"/>
						<input type="hidden" name="params[times]" value="${entity.times}">
						<input type="hidden" name="params[userId]" value="${entity.winngUser.id}">
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
							<label class="col-sm-2 control-label">揭晓时间:</label>
							<div class="col-sm-6">
								<label class="col-sm control-label"><fmt:formatDate value="${entity.openTime}" pattern="yyyy-MM-dd HH:mm:ss"/></label>
							</div>
						</div>
						<div class="hr-line-dashed"></div>
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
						<!--  
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">中奖状态:</label>
							<div class="col-sm-6">
								<label class="col-sm control-label">${winngStateMap[entity.winngState]}</label>
							</div>
						</div>
						-->
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">状态:</label>
							<div class="col-sm-6">
				<c:choose>
					<c:when test="${empty entity.exchangeState || entity.exchangeState==0}">
	                    <label class="col-sm control-label">未领奖</label>
	               </c:when>
	               <c:when test="${entity.exchangeState==1 }">
	                    <label class="col-sm control-label">已兑换积分</label>
	               </c:when>
	               <c:when test="${entity.exchangeState==2 }">
	                    <label class="col-sm control-label">未确认地址</label>
	               </c:when>
	               	<c:when test="${entity.exchangeState==3 }">
	                    <label class="col-sm control-label">未发货</label>
	               </c:when>
	               <c:when test="${entity.exchangeState==4 }">
	                    <label class="col-sm control-label">已发货，${entity.logisticsInfo}</label>
	               </c:when>
               </c:choose>
							</div>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">收货人:</label>
							<div class="col-sm-6">
								<label class="col-sm control-label">${receiver}</label>
							</div>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">收货人手机号码:</label>
							<div class="col-sm-6">
								<label class="col-sm control-label">${mobileNo}</label>
							</div>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">收货地址:</label>
							<div class="col-sm-6">
								<label class="col-sm control-label">${receiveAddr}</label>
							</div>
						</div>
						<!--  
						<c:choose>
							<c:when test="${canDispatchGoods}">
								<div class="hr-line-dashed"></div>
								<div class="form-group">
									<label class="col-sm-2 control-label">物流名字:</label>
									<div class="col-sm-6">
										<select name="params[logisticsName]" class="form-control">
											<option value="">请选择</option>
											<c:forEach items="${expressList}" var="name">
												<option value="${name}">${name}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="hr-line-dashed"></div>
								<div class="form-group">
									<label class="col-sm-2 control-label">物流订单号:</label>
									<div class="col-sm-6">
										<input type="text" name="params[logisticsNo]"  class="form-control" placeholder="请输入1-20个字符"/>
									</div>
								</div>
							</c:when>
							<c:otherwise>
							
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
									<label class="col-sm-2 control-label">是否已晒单:</label>
									<div class="col-sm-6">
										<label class="col-sm control-label">${booleanMap[entity.hasShareGoods]}</label>
									</div>
								</div>
							</c:otherwise>
						</c:choose>
							-->
						<c:if test="${entity.exchangeState==3 }">
						<div class="hr-line-dashed"></div>
								<div class="form-group">
									<label class="col-sm-2 control-label">物流订单号:</label>
									<div class="col-sm-6">
										<input type="text" name="params[logisticsInfo]"  class="form-control" placeholder="请输入快递名称及快递单号"/>
									</div>
						</div>
						</c:if>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<div class="col-sm-6 col-sm-offset-2">
								<!--  
								<c:if test="${canDispatchGoods}">
									<button class="btn btn-primary" type="submit" id="submit_btn">发货</button>
								</c:if>
								-->
								<c:if test="${entity.exchangeState==3 }">
								    <shiro:hasAnyPermissions name="goods:winner:dispatch">
									   <button class="btn btn-primary" type="submit" id="submit_btn">发货</button>
									</shiro:hasAnyPermissions>
								</c:if>
								<button class="btn btn-white" id="btnBack" type="button">返回</button>
							</div>
						</div>
						<jsp:include page="/common/foot-form-js.jsp" flush="true"></jsp:include>
					</form:form>
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		$(document).ready(function() {
			//为inputForm注册validate函数
			$("#inputForm").validate({
				rules: {
					'params[logisticsName]': {
						required: true
					},
				'params[logisticsNo]': {
						rangelength: [1, 20]
					}
				},
				messages: {
					'params[logisticsNo]': {
						rangelength: "请输入1-20个字符"
					}
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					if ( element.is(":checkbox") )
						error.appendTo(element.parent().next());
					else
						error.insertAfter(element);
				}
			});
		});
	</script>
	
	
	
</body>
</html>
