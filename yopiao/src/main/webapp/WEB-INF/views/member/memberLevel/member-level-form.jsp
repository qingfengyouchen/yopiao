<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>编辑会员等级</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>

<body>
	<div class="row">
		<div class="col-sm-12">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>
						会员管理 > ${empty entity.id ? "新增" : "修改"}会员等级
					</h5>
				</div>
				<div class="ibox-content">
					<form:form id="inputForm" modelAttribute="entity" action="${ctx}/member/memberLevel/save" method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<div class="form-group">
							<label class="col-sm-2 control-label">会员等级名称</label>
							<div class="col-sm-6">
								<form:input path="name" cssClass="form-control" placeholder="请输入1-64个字符" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">最小值</label>
							<div class="col-sm-6">
								<form:input path="minValue" cssClass="form-control" placeholder="请输入1-64个字符" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">最大值</label>
							<div class="col-sm-6">
								<form:input path="maxValue" cssClass="form-control" placeholder="请输入1-64个字符" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<div class="col-sm-6 col-sm-offset-2">
								<button class="btn btn-primary" type="submit" id="submit_btn">保存内容</button>
								<button class="btn btn-white" id="btnBack" type="button">返回</button>
							</div>
						</div>
						<jsp:include page="/common/foot-form-js.jsp" flush="true"></jsp:include>
					</form:form>
				</div>
			</div>
		</div>
	</div>
	
	<script>
		$(document).ready(function() {
			//为inputForm注册validate函数
			$.validator.setDefaults({
		        highlight: function (element) {
		            $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
		        },
		        success: function (element) {
		            element.closest('.form-group').removeClass('has-error').addClass('has-success');
		        },
		        errorElement: "span",
		        errorPlacement: function (error, element) {
		            if (element.is(":radio") || element.is(":checkbox")) {
		                error.appendTo(element.parent().parent().parent());
		            } else {
		                error.appendTo(element.parent());
		            }
		        },
		        errorClass: "help-block m-b-none",
		        validClass: "help-block m-b-none"
		    });
		    
		    var icon = "<i class='fa fa-times-circle'></i> "; 
			$("#inputForm").validate({
				rules: {
					name: {
						rangelength: [1, 20],
						required: true,
						remote: "${ctx}/member/memberLevel/checkName?oldName=" + encodeURIComponent('${entity.name}')
					},
					minValue: {
						required: true,
						isNumber: true,
						numRange: [0, 1000000]
					},
					maxValue: {
						required: true,
						isNumber: true,
						numRange: [1, 1000000]
					}
				},
				messages: {
					name: {
						rangelength: "请输入1-20个字符",
						remote: "名称已存在"
					},
					minValue: {
						numRange: "请输入0-1000000内的整数"
					},
					maxValue: {
						numRange: "请输入1-1000000内的整数"
					}
				},
            	errorPlacement:function(error,element) {  
            		error.appendTo(element.parent("div").next("label"));
               }
			});
		});
	</script>
</body>
</html>
