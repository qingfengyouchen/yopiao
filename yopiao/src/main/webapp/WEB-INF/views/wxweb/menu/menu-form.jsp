<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>编辑微信公众号菜单</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>

<body>
<div class="row">
	<div class="col-sm-12">
		<div class="ibox float-e-margins">
			<div class="ibox-title">
				<h5>微信公众号菜单管理 > ${empty entity.id ? "新增" : "修改"}微信公众号菜单</h5>
			</div>
			<div class="ibox-content">
				<form:form id="inputForm" modelAttribute="entity" action="${ctx}/wxweb/menu/save" method="post" class="form-horizontal">
					<form:hidden path="id"/>

					<div class="form-group">
						<label class="col-sm-2 control-label">上级菜单</label>
						<div class="col-sm-6">
							<form:select id="fatherId" path="father.id" cssClass="form-control">
								<option value="">无，一级菜单</option>
								<form:options items="${superMenuList}" itemValue="id" itemLabel="name"></form:options>
							</form:select>
						</div>
						<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
					</div>
					<div class="hr-line-dashed"></div>

					<div class="form-group">
						<label class="col-sm-2 control-label">名称</label>
						<div class="col-sm-6">
							<form:input path="name" cssClass="required form-control"/>
						</div>
						<label class="col-sm-4 control-label hint" style="text-align: left;" id="nameTips"> </label>
					</div>
					<div class="hr-line-dashed"></div>

					<div class="form-group">
						<label class="col-sm-2 control-label">动作设置</label>
						<div class="col-sm-6">
							<form:bsradiobuttons path="type" items="${wxMenuTypeMap}" labelCssClass="radio-inline"></form:bsradiobuttons>
						</div>
						<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
					</div>
					<div class="hr-line-dashed"></div>

					<div class="form-group">
						<label class="col-sm-2 control-label">菜单标识</label>
						<div class="col-sm-6">
							<form:input path="mkey" cssClass="form-control" placeholder="请输入1-20个字符"/>
						</div>
						<label class="col-sm-4 control-label hint" style="text-align: left;" id="mkeyTips"> </label>
					</div>
					<div class="hr-line-dashed"></div>

					<div id="typeOptionOfClick" style="display: ${entity.type == "click" ? "block" : "none"};">
					<div class="form-group">
						<label class="col-sm-2 control-label">消息类型</label>
						<div class="col-sm-6">
							<form:bsradiobuttons path="msgType" items="${wxMsgTypeMap}" labelCssClass="radio-inline"></form:bsradiobuttons>
						</div>
						<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
					</div>
					<div class="hr-line-dashed"></div>
					</div>

					<div id="typeOptionOfView" style="display: ${entity.type == "view" ? "block" : "none"};">
					<div class="form-group">
						<label class="col-sm-2 control-label">访问链接</label>
						<div class="col-sm-6">
							<form:input path="url" cssClass="required form-control" placeholder="请输入1-500个字符"/>
						</div>
						<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
					</div>
					<div class="hr-line-dashed"></div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">显示顺序</label>
						<div class="col-sm-6">
							<form:input path="sortNum" cssClass="required form-control" placeholder="请输入1-99内的整数"/>
						</div>
						<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
					</div>
					<div class="hr-line-dashed"></div>

					<div class="form-group">
						<div class="col-sm-6 col-sm-offset-2">
							<button class="btn btn-primary" type="submit" id="submit_btn">保存内容</button>
							<button class="btn btn-primary" type="submit" id="btnSaveAndAdd">保存并新增</button>
							<button class="btn btn-white" id="btnBack" type="button">返回</button>
						</div>
					</div>

					<jsp:include page="/common/foot-form-js.jsp" flush="true"></jsp:include>
					<jsp:include page="/common/operate-msg.jsp" flush="true"></jsp:include>
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
						required: true
					},
					sortNum: {
						numRange: [1, 99]
					}
				},
				messages: {
					name: {
						rangelength: "请输入1-20个字符",
						remote: "名称已存在"
					},
					sortNum: {
						numRange: "请输入1-99内的整数"
					}
				},
				errorPlacement:function(error,element) {
					error.appendTo(element.parent("div").next("label"));
				}
			});

			function setNameInfo($this){
				var msg;
				if($this.val() == ""){
					msg = "* 一级菜单不多于4个汉字或8个字母"
				}else{
					msg = "* 二级菜单不多于8个汉字或16个字母";
					is1stMenu = false;
				}
				$("#nameTips").text(msg);
			}

			var is1stMenu = true;
			setNameInfo($("#fatherId"));
			$("#fatherId").change(function(){
				setNameInfo($(this));
			});

			$("input:radio[name='type']").change(function(){
				var type = $(this).val();
				setMkeyInfo(type);
			});

			function setMkeyInfo(type){
				if("click" == type){
					$("#typeOptionOfClick").show();
					$("#typeOptionOfView").hide();
					$("#mkeyTips").text("* 必填，用于消息接口推送");
				}else{
					$("#typeOptionOfClick").hide();
					$("#typeOptionOfView").show();
					$("#mkeyTips").text("");
				}
			}

			setMkeyInfo($("input:radio[name='type']:checked").val());
		});
	</script>
</body>
</html>
