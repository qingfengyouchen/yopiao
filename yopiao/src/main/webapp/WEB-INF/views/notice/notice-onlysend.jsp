<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>编辑模块</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>

<body>
	<div class="row">
		<div class="col-sm-12">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>
						消息管理 > 向用户   ${entity.senderName }  发送消息
					</h5>
				</div>
				<div class="ibox-content">
					<form:form id="inputForm" modelAttribute="entity" action="${ctx}/notice/noticeMessage/sendMessage" method="post" class="form-horizontal">
						<form:hidden path="id" value=" ${entity.id}"/>
						<div class="form-group">
							<label class="col-sm-2 control-label">标题</label>
							<div class="col-sm-6">
								<form:input path="title" cssClass="form-control" placeholder="请输入1-64个字符" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">内容</label>
							<div class="col-sm-6">
								<form:textarea path="content" cssClass="form-control" style="margin: 0px; height: 175px;" placeholder="请输入1-200字符" autocomplete="off"/>
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
					title: {
	                    required: true,
	                    minlength:1,
	                    maxlength:64
					},
					content:{
	                    required: true,
	                    minlength:1,
	                    maxlength:200
					}
				},
				messages: {
                	title: {
                        required: icon + "请输入您的标题",
                        minlength: icon + "标题最少1个字符",
                        maxlength: icon + "标题最多64个字符"
                    },
                    content:{
                        required: icon + "请输入您的内容",
                        minlength: icon + "内容最少1个字符",
                        maxlength: icon + "内容最多200个字符"
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
