<%@ page import="com.zx.stlife.constant.Const" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>编辑系统类别</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>

<body>
	<div class="row">
		<div class="col-sm-12">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>
						系统类别管理 > ${empty entity.id ? "新增" : "修改"}系统类别
					</h5>
				</div>
				<div class="ibox-content">
					<form:form id="inputForm" modelAttribute="entity" action="${ctx}/sys/category/save"  method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<div class="form-group">
							<label class="col-sm-2 control-label">系统类别</label>
							<div class="col-sm-6">
								<form:select path="category" cssClass="form-control">
									<form:option value="">请选择</form:option>
									<form:options items="${categoryMap}"></form:options>
								</form:select>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">系统类别名称</label>
							<div class="col-sm-6">
								<form:input path="name" cssClass="form-control" placeholder="请输入1-16个字符" autocomplete="off"/>
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
						<div class="form-group">
							<div class="col-sm-6 col-sm-offset-2">
								<button class="btn btn-primary" type="submit" id="submit_btn">保存内容</button>
								<input id="btnSaveAndAdd" class="btn btn-w-m btn-success" type="button" value="保存并新增"/>&nbsp;
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
			//为inputForm注册validate函数
			$("#inputForm").validate({
				rules: {
					name: {
						rangelength: [1, 16],
						required: true,
						remote: {
							url: "${ctx}/sys/category/checkName?",
							data: {
								oldName: '${entity.name}',
								category: function(){
									return $('#category').val();
								}
							},
							dateType: 'json'
						}
					},
					sortNum: {
						numRange: [1, 99]
					}
				},
				messages: {
					name: {
						rangelength: "请输入1-16个字符",
						remote: "名称已存在"
					},
					sortNum: {
						numRange: "请输入1-99内的整数"
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

		function checkForm(){
			if(!$('#inputForm').valid()){
				return false;
			}

			if($('#max').is(":visible")){
				var min = $('#min').val();
				var max = $('#max').val();

				var $msg =  $('#messageBox');
				if(!Public.isDoubleNumber(min)){
					$msg.show().html("您输入的最小值不是数字");
					return false;
				}
				if(!Public.numRang(min, 1, 100000)){
					$msg.show().html("您输入的最小值必须在1-100000内");
					return false;
				}

				if(!Public.isDoubleNumber(max)){
					$msg.show().html("您输入的最大值不是数字");
					return false;
				}
				if(!Public.numRang(max, 1, 100000)){
					$msg.show().html("您输入的最大值必须在1-100000内");
					return false;
				}

				if(Public.isNotNull(max) && Public.isNotNull(min) && parseFloat(max) < parseFloat(min) ){
					$msg.show().html("最小值必须小于最大值");
					return false;
				}
			}

			return true;
		}

		function beforeSaveAndAdd(){
			return checkForm();
		}
	</script>
</body>
</html>
