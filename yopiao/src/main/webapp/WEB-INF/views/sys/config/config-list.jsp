<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>系统设置</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>

<body>
	<div class="row">
		<div class="col-sm-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					<form:form id="inputForm" modelAttribute="paramsEntity" action="${ctx}/sys/config/save" method="post" class="form-horizontal">
						<div class="form-group">
							<label class="col-sm-2 control-label">红包过期天数</label>
							<div class="col-sm-6">
					<form:input path="params[redPackExpireTime]" cssClass="required span3" placeholder="请输入过期天数" />
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="form-group">
							<div class="col-sm-6 col-sm-offset-2">
								<button class="btn btn-primary" type="submit" id="btnSave">保存内容</button>
							</div>
						</div>
						<div class="hr-line-dashed"></div>
						<jsp:include page="/common/foot-form-js.jsp" flush="true"></jsp:include>
					</form:form>
                    <form:form id="feeForm" modelAttribute="paramsEntity" action="${ctx}/sys/config/save" method="post" class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">积分提现手续费</label>
                            <div class="col-sm-6">
                    <form:input path="params[withDrawFee]" cssClass="required span3" placeholder="请输入手续费" />% (例：输入5代表5%的手续费)
                            </div>
                            <label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-6 col-sm-offset-2">
                                <button class="btn btn-primary" type="submit" id="feeBtnSave">保存内容</button>
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                        <jsp:include page="/common/foot-form-js.jsp" flush="true"></jsp:include>
                    </form:form>

                    <form:form id="adminRedPackForm" modelAttribute="paramsEntity" action="${ctx}/sys/config/drawByAdmin" method="post" class="form-horizontal">
                        <div class="form-group">
	                        <div class="col-sm-4">
	                             <label class="col-sm-6 control-label">用户ID</label>
	                             <form:input path="params[redUserId]" cssClass="required span3" placeholder="请输入用户ID" />
	                         </div>
	                        <div class="col-sm-4">
	                            <label class="col-sm-6 control-label">红包金额</label>
			                    <form:input path="params[redPackValue]" cssClass="required span3" placeholder="请输入红包金额" />
	                        </div>
	                         <label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-6 col-sm-offset-2">
                                <button class="btn btn-primary" type="submit" id="redPackBtnSave">保存内容</button>
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                        <jsp:include page="/common/foot-form-js.jsp" flush="true"></jsp:include>
                    </form:form>
                    
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${ctx}/static/js/jquery/jquery.public.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/jqueryui/jquery-ui.js"></script>
	<script>
		$(document).ready(function() {
			//为inputForm注册validate函数
			$("#inputForm").validate({
				debug: true,
				rules: {
					"params[redpackExpireTime]": {
						required: true,
						isNumber: true
					}
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					if( element.is(":checkbox") ) {
						error.appendTo(element.parent().next());
					}else {
						error.insertAfter(element);
					}
				}
			});

	         $("#feeForm").validate({
                debug: true,
                rules: {
                    "params[withDrawFee]": {
                        required: true,
                        numRange: [0, 100]
                    }
                },
                errorContainer: "#messageBox",
                errorPlacement: function(error, element) {
                    if( element.is(":checkbox") ) {
                        error.appendTo(element.parent().next());
                    }else {
                        error.insertAfter(element);
                    }
                }
            });
	         $("#adminRedPackForm").validate({
	                debug: true,
	                rules: {
	                    "params[redUserId]": {
	                        required: true,
	                    },
                        "params[redPackValue]": {
                           required: true,
                           numRange: [1, 100000]
                        }
	                },
	                errorContainer: "#messageBox",
	                errorPlacement: function(error, element) {
	                    if( element.is(":checkbox") ) {
	                        error.appendTo(element.parent().next());
	                    }else {
	                        error.insertAfter(element);
	                    }
	                }
	            });
			
			$("#btnSave").click(function(){
				$("#inputForm").submitForm(function(result){
					if("1" == result){
						$("#paramsredPackExpireTime").attr("value","");
						Public.message("设置成功");
					}else{
						Public.message("设置失败");
					}
				});
				
			});
            $("#feeBtnSave").click(function(){
                $("#feeForm").submitForm(function(result){
                    if("1" == result){
                        $("#paramswithDrawFee").attr("value","");
                        Public.message("设置成功");
                    }else{
                        Public.message("设置失败");
                    }
                });
            });
            $("#redPackBtnSave").click(function(){
                $("#adminRedPackForm").submitForm(function(result){
                    if("1" == result){
                        $("#paramsredPackValue").attr("value","");
                        $("#paramsredUserId").attr("value","");
                        Public.message("红包发送成功");
                    }else{
                        Public.message("红包发送失败");
                    }
                });
            });
		});
	</script>
</body>
</html>
