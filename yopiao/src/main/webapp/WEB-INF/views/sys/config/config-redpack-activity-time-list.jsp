<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>红包活动时间设置</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>

<body>
	<div class="row">
		<div class="col-sm-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					<form:form id="inputForm" modelAttribute="paramsEntity" action="${ctx}/sys/config/redpackactivitytime/save" method="post" class="form-horizontal">
						<div class="form-group">
							<label class="col-sm-2 control-label">红包活动开始时间</label>
							<div class="col-sm-6">
								<form:input path="params[redPackActivityOpenTime]" cssClass="required span3" placeholder="请选择时间" 
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d'})"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">红包活动结束时间</label>
							<div class="col-sm-6">
								<input type="text" name="params[redPackActivityEndTime]" placeholder="请选择时间" class="span3" 
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'paramsredPackActivityOpenTime\',{d:1})}'})"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<div class="col-sm-6 col-sm-offset-2">
								<button class="btn btn-primary" type="submit" id="btnSave">保存内容</button>
							</div>
						</div>
						<jsp:include page="/common/foot-form-js.jsp" flush="true"></jsp:include>
					</form:form>
				</div>
			</div>
		</div>
	</div>
		<script type="text/javascript" src="${ctx}/static/js/jquery/jquery.public.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/jqueryui/jquery-ui.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/My97DatePicker/WdatePicker.js"></script>
	<script>
		$(document).ready(function() {
			//为inputForm注册validate函数
			$("#inputForm").validate({
				debug: true,
				rules: {
					"params[redPackActivityOpenTime]": {
						required: true,
						date: true
					},
					"params[redPackActivityEndTime]": {
						required: true,
						date: true
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
						Public.message("设置成功");
					}else{
						Public.message("设置失败");
					}
				});
				
			});
		});
	</script>
</body>
</html>
