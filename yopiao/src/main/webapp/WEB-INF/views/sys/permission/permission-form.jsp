<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>编辑权限</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>

<body>
	<div class="row">
		<div class="col-sm-12">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>
						权限管理 > ${empty entity.id ? "新增" : "修改"}权限
					</h5>
				</div>
				<div class="ibox-content">
					<form:form id="inputForm" modelAttribute="entity" action="${ctx}/sys/permission/save" method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<div class="form-group">
							<label class="col-sm-2 control-label">权限名称</label>
							<div class="col-sm-6">
								<form:input path="name" cssClass="form-control" placeholder="请输入1-20个英文、数字或下划线"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">权限代码</label>
							<div class="col-sm-6">
								<form:input path="code" cssClass="form-control" placeholder="请输入1-30个字符" autocomplete="off"/>
								<span class="text-info">命名规范为模块:功能:操作,如查看用户则用sys:user:view</span>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">所属模块</label>
							<div class="col-sm-6">
								<form:select id="moduleId" path="moduleId" cssClass="form-control">
									<form:option value="">请选择</form:option>
									<form:options items="${moduleList}" itemValue="id" itemLabel="name"></form:options>
								</form:select>								
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">序号</label>
							<div class="col-sm-6">
								<form:input path="sortNum" cssClass="form-control" placeholder="请输入1-99内的整数"/>
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
	<jsp:include page="/common/operate-msg.jsp" flush="true"></jsp:include>
	<script>
		$(document).ready(function() {
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
						required: true
					},
					code: {
						rangelength: [1, 30],
						remote: "${ctx}/sys/permission/checkCode?oldCode=" + encodeURIComponent('${entity.code}')
					},
					sortNum: {
						numRange: [1, 99]
					}
				},
				messages: {
					name: {
						rangelength: icon + "请输入1-20个字符"
					},
					code: {
						rangelength:icon +  "请输入1-30个字符",
						remote: icon + "权限代码已存在"
					},
					sortNum: {
						numRange:icon +  "请输入1-99内的整数"
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
