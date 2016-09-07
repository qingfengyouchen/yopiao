<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>编辑系统用户</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>

<body>
	<div class="row">
		<div class="col-sm-12">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>
						用户管理 > ${empty user.id ? "新增" : "修改"}用户
					</h5>
				</div>
				<div class="ibox-content">
					<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/save" method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<div class="form-group">
							<label class="col-sm-2 control-label">用户名</label>
							<div class="col-sm-6">
								<c:choose>
									<c:when test="${empty user.userName}">
										<form:input path="userName" cssClass="form-control" placeholder="请输入1-20个英文、数字或下划线"/>
									</c:when>
									<c:otherwise>
										<label class="col-sm control-label">${user.userName}</label>
									</c:otherwise>
								</c:choose>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">真实姓名</label>
							<div class="col-sm-6">
								<form:input path="trueName" cssClass="form-control" placeholder="请输入1-10个字符" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">昵称</label>
							<div class="col-sm-6">
								<form:input path="nickName" cssClass="form-control" placeholder="请输入1-30个字符" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">手机号码</label>
							<div class="col-sm-6">
								<c:choose>
									<c:when test="${empty user.mobileNo}">
										<form:input path="mobileNo" cssClass="form-control" placeholder="请输入11位手机号码"/>
									</c:when>
									<c:otherwise>${user.mobileNo}</c:otherwise>
								</c:choose>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">性别</label>
							<div class="col-sm-6">
								<form:bsradiobuttons path="gender" items="${genderMap}" labelCssClass="radio-inline"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">密码</label>
							<div class="col-sm-6">
								<input type="password" id="plainPassword" name="plainPassword" class="form-control" placeholder="...修改密码请输入新密码"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">角色</label>
							<div class="col-sm-6">
								<c:forEach items="${allRoles}" var="role">
									<input type="checkbox" name="roleIds" value="${role.id}"  ${role.hasSelected == "1" ? "checked=\"checked\"" : ""}>${role.name}
								</c:forEach>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">状态</label>
							<div class="col-sm-6">
								<form:bsradiobuttons path="state" items="${stateMap}" labelCssClass="radio-inline"/>
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
					userName: {
						rangelength: [1, 20],
						remote: "${ctx}/sys/user/checkUserName?oldUserName=" + encodeURIComponent('${user.userName}')
					},
					trueName:{
						rangelength: [1, 10]
					},
					nickName:{
						rangelength: [1, 30]
					},
					mobileNo: {
						isMobile: true,
						remote: "${ctx}/sys/user/checkMobileNo?oldMobileNo=" + encodeURIComponent('${user.mobileNo}')
					},
					qq: {
						numRange: [10000, 99999999999]
					},
					roleList:"required"
				},
				messages: {
					userName: {
						rangelength: icon + "1-20个字符内",
						remote: icon + "用户名已存在"
					},
					trueName: {
						rangelength: icon + "1-10个字符内"
					},
					nickName: {
						rangelength:icon +  "1-30个字符内"
					},
					mobileNo: {
						isMobile: icon + '手机号码格式不正确',
						remote: icon + '手机号已存在'
					},
					qq: {
						numRange: icon + '请输入5-11位数字'
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
