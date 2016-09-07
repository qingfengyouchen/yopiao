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
						会员管理 > 修改会员信息
					</h5>
				</div>
				<div class="ibox-content">
					<form:form id="inputForm" modelAttribute="entity" action="${ctx}/member/member/save"  method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<div class="form-group">
							<label class="col-sm-2 control-label">用户ID</label>
							<div class="col-sm-6">
								${entity.user.userName}
							</div>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">昵称</label>
							<div class="col-sm-6">
								<form:input path="user.nickName" cssClass="form-control" placeholder="请输入1-30个字符" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">真实姓名</label>
							<div class="col-sm-6">
								<form:input path="user.trueName" cssClass="form-control" placeholder="请输入1-10个字符" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">手机号</label>
							<div class="col-sm-6">
								<form:input path="user.mobileNo" cssClass="form-control" placeholder="请输入11位手机号码" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">出生日期</label>
							<div class="col-sm-6">
								<input type="text" name="birthday" id="birthday" class="laydate-icon form-control"  value='<fmt:formatDate value="${entity.birthday}" pattern="yyyy-MM-dd" />'/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">余额</label>
							<div class="col-sm-6">
								${entity.balance}
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">积分</label>
							<div class="col-sm-6">
								${entity.jifen}
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<!-- 
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">会员等级</label>
							<div class="col-sm-6">
								${entity.memberLevel.name}
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						-->
												<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">QQ</label>
							<div class="col-sm-6">
								${entity.qq}
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
													<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">账号姓名</label>
							<div class="col-sm-6">
								${entity.accountName}
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
												<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">账号</label>
							<div class="col-sm-6">
								${entity.accountCode}
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
												<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">开户行</label>
							<div class="col-sm-6">
								${entity.accountBank}
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">可发言</label>
							<div class="col-sm-6">
								<form:bsradiobuttons path="canSpeak" items="${booleanMap}" labelCssClass="radio-inline"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">可夺宝</label>
							<div class="col-sm-6">
								<form:bsradiobuttons path="canSnatch" items="${booleanMap}" labelCssClass="radio-inline"/>
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
	<!-- layerDate plugin javascript -->
    <script src="${ctx }/static/mould/js/plugins/layer/laydate/laydate.js"></script>
	<script>
		$(function(){
			laydate({
			    elem: '#birthday', 
			    event: 'focus'
			    
			});
		});
		
		
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
	    
		$(document).ready(function() {
			var icon = "<i class='fa fa-times-circle'></i> ";
			$("#inputForm").validate({
				rules: {
					"user.nickName": {
						rangelength: [1, 30]
					},
					"user.trueName":{
						rangelength: [1, 10]
					},
					"user.mobileNo":{
						isMobile: true
					}
				},
				messages: {
					"user.nickName": {
						rangelength: "请输入1-30个字符",
					},
					"user.trueName": {
						rangelength: "请输入1-10个字符"
					},
					"user.mobileNo": {
						rangelength: "手机号码格式不正确"
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
