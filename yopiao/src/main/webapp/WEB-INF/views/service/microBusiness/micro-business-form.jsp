<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>编辑E购微商</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>
<body>
	<div class="row">
		<div class="col-sm-12">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>
						E购管理 > ${empty entity.id ? "新增" : "修改"}微商
					</h5>
				</div>
				<div class="ibox-content">
					<form:form id="inputForm" modelAttribute="entity" action="${ctx}/service/microBusiness/save" method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<div class="form-group">
							<label class="col-sm-2 control-label">标题</label>
							<div class="col-sm-6">
								<form:input path="title" cssClass="form-control" placeholder="请输入1-64个英文、数字或下划线" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">商品名称</label>
							<div class="col-sm-6">
								<form:input path="name" cssClass="form-control" placeholder="请输入1-64个字符" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">联系人</label>
							<div class="col-sm-6">
								<form:input path="linker" cssClass="form-control" placeholder="请输入2-20个字符" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">联系电话</label>
							<div class="col-sm-6">
								<form:input path="tel" cssClass="form-control" placeholder="请输入11位手机号码" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">介绍</label>
							<div class="col-sm-10">
								<script id="container" name="content" type="text/plain">${entity.content}</script>
								<jsp:include page="/common/ueditor-lib.jsp" flush="true"></jsp:include>
								<c:set var="maxLength" value="40000"/>
								<script type="text/javascript">
									var ue = UE.getEditor('container', {
										maximumWords:${maxLength},
										initialFrameWidth: '98%'
									});
								</script>
							</div>
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
			
		    jQuery.validator.addMethod("isMobile", function(value, element) {
		        var length = value.length;
		        var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
		        return this.optional(element) || (length == 11 && mobile.test(value));
		    }, "请正确填写您的手机号码");
			var icon = "<i class='fa fa-times-circle'></i> ";
			$("#inputForm").validate({
				rules:{
					title:{
						required: true,
	                    minlength:1,
	                    maxlength:64
					},
					name:{
						required: true,
	                    minlength:1,
	                    maxlength:64
					},
					linker:{
						required: true,
	                    minlength:2,
	                    maxlength:20
					},
					tel:{
                        required: true,
                        minlength : 11,
                        isMobile : true
					}
				},
				messages:{
					title:{
	                    required: icon + "请输入您的标题",
	                    minlength: icon + "标题最少1个字符",
	                    maxlength: icon + "标题最多64个字符"
					},
					name: {
	                    required: icon + "请输入商品名称",
	                    minlength: icon + "商品名称最少1个字符",
	                    maxlength: icon + "商品名称最多64个字符"
					},
					linker: {
	                    required: icon + "请输入联系人",
	                    minlength: icon + "联系人最少2个字符",
	                    maxlength: icon + "联系人最多20个字符"
					},
					tel:{
                        required: icon + "请输入手机号码",
                        minlength : "手机号码请输入11位有效数字",
                        isMobile : "请正确填写您的手机号码"
					}
				},
	        	errorPlacement:function(error,element) {  
	        		error.appendTo(element.parent("div").next("label"));
	           }
			});
			$("#submit_btn").click(function(){
				var txt = ue.getContentTxt();
				if(txt.length > ${maxLength}){
					alert("你输入的字符个数已经超出最大允许值！");
					return;
				}
				ue.sync('inputForm');
				$("#inputForm").submit();
			});
		});
	</script>
</body>
</html>
