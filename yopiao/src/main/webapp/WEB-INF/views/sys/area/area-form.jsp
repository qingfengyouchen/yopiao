<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>编辑地区</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>

<body>
	<form:form id="inputForm" modelAttribute="entity" action="${ctx}/sys/area/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<fieldset>
			<legend><small>地区管理 > ${empty entity.id ? "新增" : "修改"}地区</small></legend>
			<div id="messageBox" class="alert alert-error controls" style="display:none">输入有误，请先更正。</div>


			<div class="control-group">
				<label class="control-label">上级区域:</label>
				<div class="controls" id="area">
				</div>
			</div>

			<div class="control-group">
				<label for="name" class="control-label">名称:</label>
				<div class="controls">
					<form:input path="name" cssClass="required span4" placeholder="请输入1-20个字符"/>
				</div>
			</div>

			<div class="control-group">
				<label for="code" class="control-label">编码:</label>
				<div class="controls">
					<form:input path="code" cssClass="required span4" placeholder="请输入6位数"/>
				</div>
			</div>

			<div class="form-actions">
				<input id="submit_btn" class="btn btn-primary" type="submit" value="保存"/>&nbsp;	
				<input id="btnBack" class="btn" type="button" value="返回" />
			</div>
		</fieldset>
		<jsp:include page="/common/foot-form-js.jsp" flush="true"></jsp:include>
	</form:form>
	
	<script>
		$(document).ready(function() {
			area.init($('#area'), {
				ctx: '${ctx}',
				cityName: 'params[superFatherId]',
				cityIdVal: '${entity.superFather.id}',
				districtName: 'params[fatherId]',
				districtIdVal: '${entity.father.id}',
				zoneId: null,
				createSelect: function(id, name, labelName){
					return $('<span></span><select id="' +
							id + '" name="' + name + '" style="width: 80px;">' +
							'<option value="">请选择</option></select>');
				}
			});

			//为inputForm注册validate函数
			$("#inputForm").validate({
				rules: {
					name: {
						rangelength: [1, 20],
						required: true,
						remote: "${ctx}/sys/area/checkName?oldName=" + encodeURIComponent('${entity.name}')
					},
					code: {
						numRange: [100000, 999999]
					}
				},
				messages: {
					name: {
						rangelength: "请输入1-20个字符",
						remote: "名称已存在"
					},
					code: {
						numRange: "请输入100000 - 999999内的整数"
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
		});
	</script>
</body>
</html>
