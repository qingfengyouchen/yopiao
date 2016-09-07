<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="com.zx.stlife.base.UserUtils" %>
<%@include file="/common/taglibs.jsp" %>
<%
  boolean hasLogin = UserUtils.hasAuthenticated();
  if(hasLogin){
    response.sendRedirect(request.getContextPath() + "/home");
  }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>登录 - <%=Const.SYSTEM_NAME%></title>

    <link href="${ctx }/static/mould/css/bootstrap.min.css" rel="stylesheet">
    <link href="${ctx }/static/mould/css/font-awesome.css" rel="stylesheet">
    <link href="${ctx }/static/mould/css/animate.css" rel="stylesheet">
    <link href="${ctx }/static/mould/css/style.css" rel="stylesheet">
    <link href="${ctx }/static/mould/css/login.css" rel="stylesheet">
	<script src="${ctx}/static/mould/js/jquery-2.1.1.min.js" type="text/javascript"></script>
</head>

<body class="signin">
    <div class="signinpanel">
        <div class="row">
            <div class="col-sm-7">
                <div class="signin-info">
                    <div class="logopanel m-b">
                        <h1><span><%=Const.SYSTEM_NAME%></span></h1>
                    </div>
                    <div class="m-b"></div>
                    <h4>欢迎使用 <strong><%=Const.SYSTEM_NAME%>登录平台</strong></h4>
                    <!--  
                    <ul class="m-b">
                        <li><i class="fa fa-arrow-circle-o-right m-r-xs"></i>《<%=Const.SYSTEM_NAME%>》是一款将区域零售、区域服务信息发布和区域新闻等功能</li>
                        <li><i class="fa fa-arrow-circle-o-right m-r-xs"></i>模块融为一体的生活类应用，针对相应区域提供了不同权限的通知发布</li>
                        <li><i class="fa fa-arrow-circle-o-right m-r-xs"></i>接收、网上商城购物、商品上架/下架/修改/查询、顺风代驾、E购活动</li>
                        <li><i class="fa fa-arrow-circle-o-right m-r-xs"></i>招聘工作、鲜花速递、美食休闲、E购八卦等一系列功能的综合平台</li>
                        <li><i class="fa fa-arrow-circle-o-right m-r-xs"></i>一句话简介：E购让你的生活更精彩！</li>
                    </ul>
                    -->
                </div>
            </div>
            <div class="col-sm-5">
                <form action="${ctx}/login" method="post" id="signupForm">
                    <h4 class="no-margins">登录：</h4>
                    <div class="form-group" style="margin-top: 20px;display: none;" id="error">
                        <div>
                            <span style="color: #a94442;"><i class="fa fa-times-circle"></i><samp id="error-data"></samp></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div>
                            <input type="text" class="form-control uname" placeholder="用户名/手机号"  name="username" value="admin" autocomplete="off"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div>
                        	<input type="password" class="form-control pword m-b" placeholder="密码" name="password" value="stlife,Abc." autocomplete="off"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div>
                        	<img src="${ctx}/captcha" id="kaptchaImage"  onclick="changeCaptcha()" style="margin-bottom: -3px;height: 34px;"/>
                        	<input type="text" class="form-control uname" placeholder="验证码" name="captcha"  autocomplete="off" style="background-image: none;width: 90px;float: left;margin-top: 0px;margin-right: 22px;"/>
                        </div>
                    </div>
                    <button class="btn btn-success btn-block">登录</button>
                </form>
            </div>
        </div>
        <div class="signup-footer">
            <div class="pull-left">
                <h4>&copy; Copyright 2016 Weekend All Rights Reserved. 陕西乐久久网络科技有限公司</h4>
            </div>
        </div>
    </div>
</body>

    <script src="${ctx }/static/mould/js/bootstrap.min.js"></script>
    
    <!-- jQuery Validation plugin javascript-->
    <script src="${ctx }/static/mould/js/plugins/validate/jquery.validate.min.js"></script>
    <script src="${ctx }/static/mould/js/plugins/validate/messages_zh.min.js"></script>
    
	<script type="text/javascript">
	    //以下为修改jQuery Validation插件兼容Bootstrap的方法，没有直接写在插件中是为了便于插件升级
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
	
	    
        //以下为官方示例
        $(function(){
            var icon = "<i class='fa fa-times-circle'></i> ";
            $("#signupForm").validate({
                rules: {
                    username: {
                        required: true,
                        minlength: 2
                    },
                    password: {
                        required: true,
                        minlength: 5
                    },
                    captcha: {
                        required: true,
                        minlength: 4
                    }
                },
                messages: {
                    username: {
                        required: icon + "请输入您的用户名",
                        minlength: icon + "用户名必须两个字符以上"
                    },
                    password: {
                        required: icon + "请输入您的密码",
                        minlength: icon + "密码必须5个字符以上"
                    },
                    captcha:{
                        required: icon + "请输入验证码",
                        minlength: icon + "验证码必须为4个字符"
                    }
                }
            });
        });
	    
		function changeCaptcha(){
			var captchaUrl = "${ctx}/captcha?";
		    $("#kaptchaImage").attr("src",captchaUrl+Math.random().toString());
		    $("#captcha").focus();
		}
		
		<%
			String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
			String msg = "";
			if(error != null){
				if(error.contains("DisabledAccountException")){
					msg = "用户已被屏蔽，请登录其他用户。";
				}else if(error.contains("UnknownAccountException")){
					 msg = "用户名错误，推荐使用手机号码登陆";
				}else if(error.contains("IncorrectCredentialsException")){
					 msg = "密码错误。";
				}else if(error.contains("IncorrectCaptchaException")){
					msg = "验证码错误。";
				}else if(error.contains("AuthenticationException")){
					msg = "认证失败。";
				}else{
					msg = "登录失败，请重试。";
				}
			}
		%>
		var errorMsg = "<%=msg %>";
		if(errorMsg!=""){
			$("#error").show()
				.find("#error-data").html(errorMsg);
			
			setTimeout(function(){
				$("#error").hide("slow");
			}, 60000);
		}
    </script>
</html>
