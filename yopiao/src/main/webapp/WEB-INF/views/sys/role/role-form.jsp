<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>编辑角色</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>

<body>
	<div class="row">
		<div class="col-sm-12">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>
						角色管理 > ${empty entity.id ? "新增" : "修改"}角色
					</h5>
				</div>
				<div class="ibox-content">
					<form:form id="inputForm" modelAttribute="entity" action="${ctx}/sys/role/save"  method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<div class="form-group">
							<label class="col-sm-2 control-label">角色名称</label>
							<div class="col-sm-6">
								<form:input path="name" cssClass="form-control" placeholder="请输入1-64个字符" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">序号</label>
							<div class="col-sm-6">
								<form:input path="sortNum" cssClass="form-control" placeholder="请输入1-99内的整数" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="alert alert-info text-center">
							权限列表
						</div>
						<div class="hr-line-dashed"></div>
						<c:forEach items="${modulePermissionMap}" var="map">
							<div class="form-group">
								<label class="col-sm-2 control-label">${map.key}</label>
								<div class="col-sm-10">
									<c:forEach items="${map.value}" var="permission">
										<input type="checkbox" name="permissionIds" value="${permission.id}" ${permission.hasSelected ? "checked=\"checked\"" : ""}>${permission.name}
									</c:forEach>
								</div>
								<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
							</div>
							<div class="hr-line-dashed"></div>
						</c:forEach>
						
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
						remote: "${ctx}/sys/role/checkRoleName?oldName=" + encodeURIComponent('${entity.name}')
					},
					sortNum: {
						numRange: [1, 99]
					}
				},
				messages: {
					name: {
						rangelength: icon + "请输入1-20个字符",
						remote: icon + "角色名已存在"
					},
					sortNum: {
						numRange: icon + "请输入1-99内的整数"
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
